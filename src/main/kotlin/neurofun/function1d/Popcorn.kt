package neurofun.function1d

import neurofun.random.Dice
import kotlin.math.sin
import kotlin.math.tan

data class Popcorn(val a: Double = Dice.randomDouble(), val b: Double = Dice.randomDouble()) : Function1D {
    override fun apply(x: Double): Double {
        return x + a * sin(tan(3 * b))
    }
}