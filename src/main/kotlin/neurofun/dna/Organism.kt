package neurofun.dna

import neurofun.color.ColorFunction
import neurofun.util.array2d
import java.awt.Color
import java.awt.Graphics
import kotlin.math.max
import kotlin.math.min

class Organism(
    val dna: DNA,
    val width: Int,
    val height: Int,
    var entropy: Double = 0.0
) {
    val nn: FunctionNN = FunctionNNExpresser.express(dna)
    private val colorFunction: ColorFunction = ColorFunctionExpresser.express(dna.colorGene)
    private val expressBuffer: Array<Array<Double>> = array2d(width, height) { 0.0 } // RGB
//    private val expressBuffer: Array<Array<Array<Double>>> = array2d(width, height) { arrayOf(0.0, 0.0, 0.0) } // RGB
//    private var maxOutputs = arrayOf(0.0, 0.0, 0.0) // max RGBs
    private var maxOutput = 0.0

    fun step() {
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                // scale x and y to range [-1.0, 1.0]
                val scaledX = 2 * (x.toDouble() / (width - 1)) - 1
                val scaledY = 2 * (y.toDouble() / (height - 1)) - 1
//                println("($x,$y) -> ($scaledX, $scaledY)")
                val output = nn.feedforward(scaledX, scaledY)

                expressBuffer[x][y] = output[0]

//                if (output[0] > maxOutputs[0]) {
//                    maxOutputs[0] = output[0]
//                }
//                if (output[1] > maxOutputs[1]) {
//                    maxOutputs[1] = output[1]
//                }
//                if (output[2] > maxOutputs[2]) {
//                    maxOutputs[2] = output[2]
//                }
            }
        }
    }

    fun express(canvasGraphics: Graphics) {
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                val rgb = expressBuffer[x][y]
//                canvasGraphics.color = Color(squash(rgb[0]), squash(rgb[1]), squash(rgb[2]))
                canvasGraphics.color = colorFunction.apply(x, y, expressBuffer, maxOutput)
                canvasGraphics.fillRect(x, y, 1, 1)
            }
        }
    }

    private fun squash(v: Double): Float {
        return max(0.0, min(1.0, v)).toFloat()
    }

    override fun toString(): String {
        return "Organism(\n" +
                "  dna=$dna,\n" +
                "  width=$width, height=$height,\n" +
                "  entropy=$entropy,\n" +
                "  nn=$nn,\n" +
                "  colorFunction=$colorFunction,\n" +
//                "  maxCount=$maxOutputs\n" +
                ")"
    }


}