package neurofun.function1d

import neurofun.random.Dice
import kotlin.math.abs

data class Horseshoe(
    private val a: Double = Dice.randomDouble(), private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(), private val d: Double = Dice.randomDouble(),
) : Function1D {
    override fun apply(x: Double): Double {
        return a * ((x - b) * (x + c)) / abs(x) + d
    }

}