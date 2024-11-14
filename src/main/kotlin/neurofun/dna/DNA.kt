package neurofun.dna

import neurofun.geometry.Vector3D

data class DNA(
    val inputCount: Int,
    val dimensions: Vector3D,
    val genes: MutableList<Gene>,
    val colorGene: ColorGene,
) {
    val size = genes.size

    data class Gene(
        var function: GeneFunction,
        var a: Double, var b: Double, var c: Double, var d: Double,
        var e: Double, var f: Double, var g: Double, var h: Double,
        var decay: Double,
        var synapses: List<Synapse>
    )

    enum class GeneFunction {
        ABS,
        CURVE,
        GUASSIAN,
        HORSESHOE,
        IDENTITY,
        PARABOLA,
        POPCORN,
        ROTATE,
        SIGMOID,
        SIN,
        SPIRAL,
        SQUARED,
        SCALE,
        TRANSLATE
    }

    data class Synapse(
        val to: Vector3D,
        var weight: Double
    )

    data class ColorGene(
        var algorithm: ColorAlgorithm,
        var genes: MutableList<Gene>,
        var alpha: Double
    ) {
        enum class ColorAlgorithm {
            DEFAULT,
            FUNCTIONS,
            NEON,
            COSMIC_PULSE
        }
    }

    override fun toString(): String {
        return "DNA(\n  inputCount=$inputCount," +
                "\n  dimensions=$dimensions," +
                "\n  genes=\n    ${genes.joinToString("\n    ")}," +
                "\n  colorGene=$colorGene," +
                "\n  size=$size\n)"
    }


}


