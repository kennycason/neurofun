package neurofun.color

import java.awt.Color

interface ColorFunction {
    fun apply(x: Int, y: Int, buffer: Array<Array<Double>>, maxCount: Double): Color
}