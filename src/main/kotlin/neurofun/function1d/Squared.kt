package neurofun.function1d

import neurofun.random.Dice

data class Squared(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble()
) : Function1D {
    override fun apply(x: Double) = a * (x * x) + b
}