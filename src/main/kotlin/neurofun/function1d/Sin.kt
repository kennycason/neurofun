package neurofun.function1d

import neurofun.random.Dice
import kotlin.math.sin

data class Sin(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(), private val c: Double = Dice.randomDouble()
) : Function1D {
    override fun apply(x: Double) = a * sin(b * x) + c
}