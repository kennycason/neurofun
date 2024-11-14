package neurofun.function1d

import neurofun.random.Dice

data class Scale(
    private val scaleX: Double = Dice.randomDouble()
) : Function1D {
    override fun apply(x: Double): Double {
        return x * scaleX
    }
}