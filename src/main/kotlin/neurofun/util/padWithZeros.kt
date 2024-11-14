package neurofun.util

fun Int.padWithZeros(totalLength: Int): String {
    return toString().padStart(totalLength, '0')
}