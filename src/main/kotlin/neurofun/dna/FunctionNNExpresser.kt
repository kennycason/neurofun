package neurofun.dna

import neurofun.function1d.Identity
import neurofun.geometry.Vector3D

object FunctionNNExpresser {

    fun express(dna: DNA): FunctionNN {
        val dimX = dna.dimensions.x.toInt()
        val dimY = dna.dimensions.y.toInt()
        val dimZ = dna.dimensions.z.toInt()
        val nodes = Array(dimX) { x ->
            Array(dimY) { y ->
                Array(dimZ) { z ->
                    val i = (z * dimY * dimX) + y * dimX + x
                    val gene = dna.genes[i]
                    FunctionNN.Node(
                        position = Vector3D(x.toDouble(), y.toDouble(), z.toDouble()),
                        function = Genetic.expressGeneFunction(gene),
                        decay = gene.decay
                    )
                }
            }
        }

        // Connect synapses based on relative coordinates
        for (z in 0 until dimZ) {
            for (y in 0 until dimY) {
                for (x in 0 until dimX) {
                    val node = nodes[x][y][z]
                    val i = (z * dimY * dimX) + y * dimX + x
                    //println("$x,$y,$z > $i")
                    val gene = dna.genes[i] // Retrieve the gene for this node

                    for (synapse in gene.synapses) {
                        val targetX = x + synapse.to.x.toInt()
                        val targetY = y + synapse.to.y.toInt()
                        val targetZ = z + synapse.to.z.toInt()
                        if (targetX in 0 until dimX
                            && targetY in 0 until dimY
                            && targetZ in 0 until dimZ
                            && !(targetX == 0 && targetY == 0 && targetZ == 0)
                            ) {
                            val targetNode = nodes[targetX][targetY][targetZ]
                            node.synapses.add(
                                FunctionNN.Synapse(
                                    node = targetNode,
                                    weight = synapse.weight
                                )
                            )
                        }
                    }
                }
            }
        }

        return FunctionNN(
            inputs = Array(dna.inputCount) { FunctionNN.Node(function = Identity()) },
            nodes = nodes,
            outputs = Array(size = 3) { FunctionNN.Node(function = Identity()) }
        )
    }

}