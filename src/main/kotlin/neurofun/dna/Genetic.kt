package neurofun.dna

import neurofun.function1d.Abs
import neurofun.function1d.Curve
import neurofun.function1d.Function1D
import neurofun.function1d.Guassian
import neurofun.function1d.Horseshoe
import neurofun.function1d.Identity
import neurofun.function1d.Parabola
import neurofun.function1d.Popcorn
import neurofun.function1d.Rotate
import neurofun.function1d.Scale
import neurofun.function1d.Sigmoid
import neurofun.function1d.Sin
import neurofun.function1d.Spiral
import neurofun.function1d.Squared
import neurofun.function1d.Translate
import neurofun.random.Dice

object Genetic {

    fun mutateOrganism(organism: Organism, probability: Double): Organism {
        val newOrganism = Organism(
            dna = clone(organism.dna),
            width = organism.width,
            height = organism.height,
            entropy = organism.entropy
        )
        mutateDna(newOrganism.dna, probability)
        return newOrganism
    }

    fun mutateDna(dna: DNA, probability: Double) {
        mutateColorGene(dna.colorGene, probability)
        for (gene in dna.genes) {
            mutateGene(gene, probability, min = -1.0, max = 1.0)
        }
    }

    private fun mutateGene(gene: DNA.Gene, probability: Double, min: Double = -10.0, max: Double = 10.0) {
        gene.function = if (Dice.nextDouble() < probability) {
            DNA.GeneFunction.entries.random()
        } else gene.function
        gene.a = mutateDouble(gene.a, probability, min = min, max = max)
        gene.b = mutateDouble(gene.b, probability, min = min, max = max)
        gene.c = mutateDouble(gene.c, probability, min = min, max = max)
        gene.d = mutateDouble(gene.d, probability, min = min, max = max)
        gene.e = mutateDouble(gene.e, probability, min = min, max = max)
        gene.f = mutateDouble(gene.f, probability, min = min, max = max)
        gene.g = mutateDouble(gene.g, probability, min = min, max = max)
        gene.h = mutateDouble(gene.h, probability, min = min, max = max)
        gene.decay = mutateDouble(gene.h, probability, min = min, max = max)
        gene.synapses.forEach { synapse ->
            synapse.weight = mutateDouble(synapse.weight, probability, min = min, max = max)
        }
    }

    private fun mutateColorGene(colorGene: DNA.ColorGene, probability: Double) {
        colorGene.algorithm = if (Dice.nextDouble() < probability) {
            println("mutate color algorithm")
            DNA.ColorGene.ColorAlgorithm.entries.random()
        } else colorGene.algorithm
        colorGene.genes.forEach {
            mutateGene(it, probability * 2, min = -10.0, max = 10.0)
        }
        colorGene.alpha = mutateDouble(colorGene.alpha, probability, min = 0.5, max = 1.0)
    }

    private fun mutateDouble(value: Double, probability: Double, min: Double = -1.0, max: Double = 1.0): Double {
        if (Dice.nextDouble() >= probability) return value
        val newValue = value + Dice.randomDouble() / 10
        return if (newValue < min) min
        else if (newValue > max) max
        else newValue
    }

    fun reproduce(dna1: DNA, dna2: DNA): DNA {
        val longestDna = if (dna1.genes.size >= dna2.genes.size) dna1 else dna2
        val shortestDna = if (dna1.genes.size < dna2.genes.size) dna1 else dna2
        val clone = clone(longestDna)
        for (i in 0 until shortestDna.genes.size) {
            if (Dice.nextBoolean()) {
                clone.genes[i] = shortestDna.genes[i]
            }
        }
        for (i in 0 until shortestDna.colorGene.genes.size) {
            if (Dice.nextBoolean()) {
                clone.colorGene.genes[i] = shortestDna.colorGene.genes[i]
            }
        }
        return clone
    }

    fun expressGeneFunctions(genes: List<DNA.Gene>): List<Function1D> {
        return genes.map(Genetic::expressGeneFunction)
    }

    fun expressGeneFunction(gene: DNA.Gene): Function1D {
        return when (gene.function) {
            DNA.GeneFunction.ABS -> Abs(a = gene.a, b = gene.b)
            DNA.GeneFunction.CURVE -> Curve(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            DNA.GeneFunction.GUASSIAN -> Guassian(x0 = gene.a, sigmaX = gene.b, amplitude = gene.c)
            DNA.GeneFunction.HORSESHOE -> Horseshoe(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            DNA.GeneFunction.IDENTITY -> Identity()
            DNA.GeneFunction.PARABOLA -> Parabola(a = gene.a, b = gene.b, c = gene.c)
            DNA.GeneFunction.POPCORN -> Popcorn(a = gene.a, b = gene.b)
            DNA.GeneFunction.ROTATE -> Rotate(theta = gene.a, centerX = gene.b)
            DNA.GeneFunction.SCALE -> Scale(scaleX = gene.a)
            DNA.GeneFunction.SIGMOID -> Sigmoid(a = gene.a, b = gene.b)
            DNA.GeneFunction.SIN -> Sin(a = gene.a, b = gene.b, c = gene.c)
            DNA.GeneFunction.SPIRAL -> Spiral(a = gene.a, b = gene.b, c = gene.c, d = gene.d)
            DNA.GeneFunction.SQUARED -> Squared(a = gene.a, b = gene.b)
            DNA.GeneFunction.TRANSLATE -> Translate(dx = gene.a)
        }
    }

    fun clone(dna: DNA): DNA {
        return DNA(
            inputCount = dna.inputCount,
            dimensions = dna.dimensions.copy(),
            genes = dna.genes.map {
                DNA.Gene(
                    function = it.function,
                    a = it.a,
                    b = it.b,
                    c = it.c,
                    d = it.d,
                    e = it.e,
                    f = it.f,
                    g = it.g,
                    h = it.h,
                    decay = it.decay,
                    synapses = it.synapses.toList()
                )
            }.toMutableList(),
            colorGene = DNA.ColorGene(
                algorithm = dna.colorGene.algorithm,
                genes = dna.genes.map {
                    DNA.Gene(
                        function = it.function,
                        a = it.a,
                        b = it.b,
                        c = it.c,
                        d = it.d,
                        e = it.e,
                        f = it.f,
                        g = it.g,
                        h = it.h,
                        decay = it.decay,
                        synapses = it.synapses.toList()
                    )
                }.toMutableList(),
                alpha = dna.colorGene.alpha
            )
        )
    }

}