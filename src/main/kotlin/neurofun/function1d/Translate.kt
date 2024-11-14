package neurofun.function1d

import neurofun.random.Dice

data class Translate(
    private val dx: Double = Dice.randomDouble()
) : Function1D {
    override fun apply(x: Double) = x + dx
}