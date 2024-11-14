package neurofun.function1d

import neurofun.random.Dice
import kotlin.math.exp

data class Sigmoid(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble()
) : Function1D {
    private val scale = 1.0
    override fun apply(x: Double): Double {
        return 2 / (1 + exp(-x * scale)) - 1
    }
}