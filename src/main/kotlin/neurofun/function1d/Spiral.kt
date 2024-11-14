package neurofun.function1d

import neurofun.random.Dice
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Spiral(
    private val a: Double = Dice.randomDouble(),
    private val b: Double = Dice.randomDouble(),
    private val c: Double = Dice.randomDouble(),
    private val d: Double = Dice.randomDouble()
) : Function1D {
    override fun apply(x: Double): Double {
        // simulates an expanding wave effect
        val r = sqrt(x * x) // distance from origin
        val angle = r * b // introduce angular rotation in 1D as a scaling factor

        // apply sinusoidal expansion and contraction
        return (a * r * cos(angle)) + (c * sin(angle)) + d
    }

}