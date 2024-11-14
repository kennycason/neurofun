package neurofun.dna

import neurofun.function1d.Function1D
import neurofun.function1d.Sigmoid
import neurofun.geometry.Vector3D
import java.text.DecimalFormat

typealias Cube = Array<Array<Array<FunctionNN.Node>>>

class FunctionNN(
    private val inputs: Array<Node>,
    val nodes: Cube,
    private val outputs: Array<Node>
) {
    private val squashFunction = Sigmoid()
    private var iteration = 0

    init {
        // connect the input node to each node in the z = 0 slice
        for (x in nodes.indices) {
            for (y in nodes[0].indices) {
                for (input in inputs) {
                    input.synapses.add(Synapse(nodes[x][y][0], weight = 1.0))
                }
            }
        }

        // connect each node in the last z slice to the output node
        val zLast = nodes[0][0].lastIndex
        for (x in nodes.indices) {
            for (y in nodes[0].indices) {
                for (output in outputs) {
                    nodes[x][y][zLast].synapses.add(Synapse(output, weight = 1.0))
                }
            }
        }
    }

    fun feedforward(vararg xs: Double): Array<Double> {
        if (xs.size != inputs.size) throw IllegalArgumentException("xs must be of length ${inputs.size}")

        inputs.forEachIndexed { i, input ->
            val x = xs[i]
            input.value = input.function.apply(x)
            for (synapse in input.synapses) {
                synapse.node.value += input.value * synapse.weight
            }
//            for (synapse in input.synapses) {
//                synapse.node.value = squashFunction.apply(synapse.node.value)
//            }
        }

        // iterate over slices along the z-axis
        for (z in nodes[0][0].indices) {
            for (x in nodes.indices) {
                for (y in nodes[0].indices) {
                    val node = nodes[x][y][z]
                    node.value = squashFunction.apply(node.value)
                    for (synapse in node.synapses) {
                        synapse.node.value += node.value * synapse.weight
                    }

                    // dampen
                    node.value *= node.decay
                    node.value -= 0.5
                    if (node.value < 0) {
                        node.value = 0.0
                    }
//                    node.value = 0.0
                }
            }
        }

        iteration++
        return outputs
            .map { it.value }
            .toTypedArray()
    }

    data class Node(
        var function: Function1D,
        val position: Vector3D = Vector3D(),
        val synapses: MutableList<Synapse> = mutableListOf(),
        var value: Double = 0.0,
        var decay: Double = 0.0,
        var iteration: Int = 0
    )

    data class Synapse(
        val node: Node,
        val weight: Double
    )

    override fun toString() = toString(this)


    companion object {
        private val df = DecimalFormat("#.##")

        fun toString(nn: FunctionNN): String {
            val nodes = nn.nodes
            val builder = StringBuilder()

            // Print input node information
            builder.append("input:\n")
            nn.inputs.forEachIndexed { i, node ->
                val inputConnections = buildSynapseString(node.synapses, nn.outputs)
                builder
                    .append("i$i: (v=${df.format(node.value)},decay=${df.format(node.decay)}) -> [")
                    .append(inputConnections)
                    .append("]\n")
            }

            // Iterate over each layer (layer is the z-coordinate in the cube)
            for (z in nodes[0][0].indices) {
                builder.append("layer ${z + 1}:\n")
                for (x in nodes.indices) {
                    for (y in nodes[0].indices) {
                        val node = nodes[x][y][z]
                        val functionName = node.function.javaClass.simpleName
                        val connections = buildSynapseString(node.synapses, nn.outputs)
                        builder.append("$functionName(${toString(node.position)},v=${df.format(node.value)},decay=${df.format(node.decay)}) -> " +
                                "[$connections],")
                    }
                    builder.setLength(builder.length - 1) // remove trailing comma and add a newline
                    builder.append("\n")
                }
            }

            // Print output node information
            builder.append("output:\n")
            nn.outputs.forEachIndexed { i, node ->
                builder.append("output: ${node.function.javaClass.simpleName}(v=${df.format(node.value)})")
            }

            return builder.toString()
        }

        private fun buildSynapseString(synapses: List<Synapse>, outputs: Array<Node> = arrayOf(), isInput: Boolean = false): String {
            return synapses.joinToString(",") {
                if (it.node in outputs) "(OUT,w=${df.format(it.weight)})"
                else "(${toString(it.node.position)},w=${df.format(it.weight)})"
            }
        }

        private fun toString(position: Vector3D): String {
            return "${position.x},${position.y},${position.z}"
        }
    }

}


