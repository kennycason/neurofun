package neurofun.dna

import neurofun.geometry.Vector3D
import neurofun.random.Dice
import kotlin.random.Random

object RandomDNAGenerator {

    fun buildOrganism(
        inputCount: Int,
        dimX: Int, dimY: Int, dimZ: Int,
        width: Int, height: Int
    ): Organism {
        return Organism(
            dna = build(inputCount, dimX, dimY, dimZ),
            width = width,
            height = height
        )
    }

    fun build(
        inputCount: Int,
        dimX: Int,
        dimY: Int,
        dimZ: Int
    ): DNA {
        return DNA(
            inputCount = inputCount,
            dimensions = Vector3D(dimX.toDouble(), dimY.toDouble(), dimZ.toDouble()),
            genes = MutableList(size = dimX * dimY * dimZ) { i ->
                val isTerminal = i >= dimX * dimY * dimZ - dimX * dimY
                buildRandomGene(isTerminal)
            },
            colorGene = buildRandomColorGene(),
        )
    }

    private fun buildRandomColorGene(): DNA.ColorGene {
        return DNA.ColorGene(
            algorithm = DNA.ColorGene.ColorAlgorithm.FUNCTIONS, // DNA.ColorGene.ColorAlgorithm.entries.random(),
            genes = MutableList(size = 3) { buildRandomGene(true) },
            alpha = Dice.nextDouble()
        )
    }

    private fun buildRandomGene(isTerminal: Boolean) = DNA.Gene(
        function = DNA.GeneFunction.entries.random(),
        a = Dice.randomDouble(),
        b = Dice.randomDouble(),
        c = Dice.randomDouble(),
        d = Dice.randomDouble(),
        e = Dice.randomDouble(),
        f = Dice.randomDouble(),
        g = Dice.randomDouble(),
        h = Dice.randomDouble(),
        decay = Dice.nextDouble(),
        synapses = if (isTerminal) listOf()
//        else listOf(
//            DNA.Synapse(to = Vector3D(x=-1.0, y=1.0, z=1.0), weight = Dice.randomDouble()),
//            DNA.Synapse(to = Vector3D(x=-1.0, y=0.0, z=1.0), weight = Dice.randomDouble()),
//            DNA.Synapse(to = Vector3D(x=-1.0, y=-1.0, z=1.0), weight = Dice.randomDouble()),
//
//            DNA.Synapse(to = Vector3D(x=0.0, y=1.0, z=1.0), weight = Dice.randomDouble()),
//            DNA.Synapse(to = Vector3D(x=0.0, y=0.0, z=1.0), weight = Dice.randomDouble()),
//            DNA.Synapse(to = Vector3D(x=0.0, y=-1.0, z=1.0), weight = Dice.randomDouble()),
//
//            DNA.Synapse(to = Vector3D(x=1.0, y=1.0, z=1.0), weight = Dice.randomDouble()),
//            DNA.Synapse(to = Vector3D(x=1.0, y=0.0, z=1.0), weight = Dice.randomDouble()),
//            DNA.Synapse(to = Vector3D(x=1.0, y=-1.0, z=1.0), weight = Dice.randomDouble()),
//        )
        else List(size = Dice.nextInt(16) + 1) {
            DNA.Synapse(
                to = Vector3D(
                    x = Random.nextInt(-2, 3).toDouble(),
                    y = Random.nextInt(-2, 3).toDouble(),
                    z = Random.nextInt(-2, 3).toDouble()
                ),
                weight = Dice.randomDouble()
            )
        }
            .filter { it.to.x != 0.0 && it.to.y != 0.0 && it.to.z != 0.0 }
    )

}