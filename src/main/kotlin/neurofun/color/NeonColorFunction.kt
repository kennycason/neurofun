package neurofun.color

import java.awt.Color
import kotlin.math.sin

data class NeonColorFunction(
    var f1: Double = 0.4,  // frequency for Red
    var f2: Double = 0.3,  // frequency for Green
    var f3: Double = 0.2,  // frequency for Blue
    var p1: Double = 0.0,  // phase shift for Red
    var p2: Double = 0.0,  // phase shift for Green
    var p3: Double = 0.0,  // phase shift for Blue
    var intensity: Double = 1.5,  // intensity factor for brightening the colors
    var alpha: Double = 1.0
) : ColorFunction {

    override fun apply(x: Int, y: Int, buffer: Array<Array<Double>>, maxCount: Double): Color {
        if (buffer[x][y] == 0.0) return Color.BLACK

        // calculate intensity modulation based on cell count (simulating neon pulse)
        val mod = (buffer[x][y] / maxCount).coerceIn(0.0, 1.0)
        val i = buffer[x][y] * intensity * mod  // Adjust intensity based on count

        // use sin function to create pulsing, neon-like effects for RGB channels
        val r = sin(f1 * i + p1) * 0.5 + 0.5
        val g = sin(f2 * i + p2) * 0.5 + 0.5
        val b = sin(f3 * i + p3) * 0.5 + 0.5

        return Color(
            r.coerceIn(0.0, 1.0).toFloat(),
            g.coerceIn(0.0, 1.0).toFloat(),
            b.coerceIn(0.0, 1.0).toFloat(),
            alpha.toFloat()
        )
    }
}