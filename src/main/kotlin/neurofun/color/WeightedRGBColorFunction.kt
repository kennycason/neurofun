package neurofun.color

import neurofun.function1d.Function1D
import java.awt.Color
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class WeightedRGBColorFunction(
    private val colorFunctions: List<Function1D>,
    private val alpha: Float
) : ColorFunction {
    private val r = colorFunctions[0]
    private val g = colorFunctions[1]
    private val b = colorFunctions[2]

    override fun apply(x: Int, y: Int, buffer: Array<Array<Double>>, maxCount: Double): Color {
        if (buffer[x][y] == 0.0) return Color.BLACK
        val p = buffer[x][y]
        val r = r.apply(p)
        val g = g.apply(p)
        val b = b.apply(p)
        return Color(clamp(r), clamp(g), clamp(b), alpha)
    }

    private fun clamp(v: Double): Float {
        return max(min(abs(v), 1.0), 0.0).toFloat()
    }

}