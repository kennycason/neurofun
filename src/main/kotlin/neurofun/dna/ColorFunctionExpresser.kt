package neurofun.dna

import neurofun.color.ColorFunction
import neurofun.color.CosmicPulseColorFunction
import neurofun.color.DefaultColorFunction
import neurofun.color.NeonColorFunction
import neurofun.color.WeightedRGBColorFunction
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
import neurofun.geometry.Vector3D

object ColorFunctionExpresser {

    fun express(colorGene: DNA.ColorGene): ColorFunction {
        return when (colorGene.algorithm) {
            DNA.ColorGene.ColorAlgorithm.DEFAULT -> DefaultColorFunction(
                f1 = colorGene.genes.first().a,
                f2 = colorGene.genes.first().b,
                f3 = colorGene.genes.first().c,
                p1 = colorGene.genes.first().d,
                p2 = colorGene.genes.first().e,
                p3 = colorGene.genes.first().f,
                center = 0.5,
                width = 0.5,
                alpha = colorGene.alpha
            )

            DNA.ColorGene.ColorAlgorithm.FUNCTIONS -> WeightedRGBColorFunction(
                colorFunctions = Genetic.expressGeneFunctions(colorGene.genes),
                alpha = colorGene.alpha.toFloat()
            )

            DNA.ColorGene.ColorAlgorithm.NEON -> NeonColorFunction(
                f1 = colorGene.genes.first().a,
                f2 = colorGene.genes.first().b,
                f3 = colorGene.genes.first().c,
                p1 = colorGene.genes.first().d,
                p2 = colorGene.genes.first().e,
                p3 = colorGene.genes.first().f,
                intensity = colorGene.genes[1].a * 5.0,
                alpha = colorGene.alpha
            )

            DNA.ColorGene.ColorAlgorithm.COSMIC_PULSE -> CosmicPulseColorFunction(
                rFrequency = colorGene.genes.first().a,
                gFrequency = colorGene.genes.first().a,
                bFrequency = colorGene.genes.first().a,
                rPhase = colorGene.genes.first().a,
                gPhase = colorGene.genes.first().a,
                bPhase = colorGene.genes.first().a,
                brightness = colorGene.genes[1].a * 2,
                alpha = colorGene.alpha
            )
        }
    }

}