package neurofun.function1d

import neurofun.random.Dice

data class Curve(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(), private val d: Double = Dice.randomDouble()
) : Function1D {
    override fun apply(x: Double): Double {
        val rSquared = (a * x + b) * (a * x + b)
        return c * x / (rSquared + 1e-10) + d
    }
}
