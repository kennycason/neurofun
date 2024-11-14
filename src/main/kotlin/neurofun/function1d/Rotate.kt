package neurofun.function1d


import neurofun.random.Dice
import kotlin.math.cos
import kotlin.math.sin

data class Rotate(
    private val theta: Double = Dice.randomDouble(),
    private val centerX: Double = 0.0
) : Function1D {
    override fun apply(x: Double): Double {
        return cos(theta) * (x - centerX) + sin(theta) * (x - centerX) + centerX
    }
}