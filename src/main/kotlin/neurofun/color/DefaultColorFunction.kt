package neurofun.color

import java.awt.Color
import kotlin.math.sin

data class DefaultColorFunction(
    var algorithm: Algorithm = Algorithm.DEFAULT,
    var f1: Double = 0.4,  // frequency for Red
    var f2: Double = 0.3,  // frequency for Green
    var f3: Double = 0.2,  // frequency for Blue
    var p1: Double = 0.0,  // phase shift for Red
    var p2: Double = 0.0,  // phase shift for Green
    var p3: Double = 0.0,  // phase shift for Blue
    var center: Double = 0.5,
    var width: Double = 0.5,
    var alpha: Double = 1.0
) : ColorFunction {
    enum class Algorithm {
        DEFAULT,
        GRADIENT,
        NEIGHBOR_AVG,
        NEIGHBOR_AVG_AND_DEFAULT
    }

    override fun apply(x: Int, y: Int, buffer: Array<Array<Double>>, maxCount: Double): Color {
        if (buffer[x][y] == 0.0) return Color.BLACK
        return when (algorithm) {
            Algorithm.DEFAULT -> applyDefault(buffer[x][y])
            Algorithm.GRADIENT -> gradientColor(buffer[x][y], maxI = maxCount)
            Algorithm.NEIGHBOR_AVG -> applyNeighborAverage(x, y, buffer)
            Algorithm.NEIGHBOR_AVG_AND_DEFAULT -> {
                val c1 = applyDefault(buffer[x][y])
                val c2 = gradientColor(buffer[x][y], maxI = maxCount)
                Color(
                    (c1.red + c2.red) / 2, (c1.green + c2.green) / 2,
                    (c1.blue + c2.blue) / 2, (c1.alpha + c2.alpha) / 2
                )
            }
        }
    }

    private fun applyNeighborAverage(x: Int, y: Int, buffer: Array<Array<Double>>): Color {
        val i = buffer[x][y]
        val neighbors = listOf(
            buffer.getOrNull(x - 1)?.getOrNull(y), buffer.getOrNull(x + 1)?.getOrNull(y),
            buffer.getOrNull(x)?.getOrNull(y - 1), buffer.getOrNull(x)?.getOrNull(y + 1)
        ).filterNotNull()

        val avgCount = neighbors.map { it }.average()

        val r = (avgCount * f1 * i).coerceIn(0.0, 1.0).toFloat()
        val g = (avgCount * f2 * i).coerceIn(0.0, 1.0).toFloat()
        val b = (avgCount * f3 * i).coerceIn(0.0, 1.0).toFloat()

        return Color(r, g, b, alpha.toFloat())
    }

    private fun gradientColor(i: Double, maxI: Double): Color {
        val ratio = (i / maxI)
        val r = 1.0f * ratio
        val g = 1.0f * (1 - ratio)
        return Color(r.toFloat(), g.toFloat(), (0.5 + 0.5 * sin(i * 0.1)).toFloat(), alpha.toFloat())
    }

    private fun applyDefault(i: Double): Color {
        val r = sin(f1 * i + p1) * width + center
        val g = sin(f2 * i + p2 * 2) * width + center
        val b = sin(f3 * i + p3 * 4) * width + center
//        if ((r > 0xFF || r < 0) || (g > 0xFF || g < 0) || (b > 0xFF || b < 0))
//            println("problem")
        return Color(r.toFloat(), g.toFloat(), b.toFloat(), alpha.toFloat())
    }

}