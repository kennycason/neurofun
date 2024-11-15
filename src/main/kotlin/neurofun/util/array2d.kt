package neurofun.util

inline fun <reified INNER> array2d(sizeOuter: Int, sizeInner: Int,
                                   noinline innerInit: (Int)->INNER): Array<Array<INNER>> = Array(sizeOuter) { Array(sizeInner, innerInit) }