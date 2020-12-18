package battles.utils

import kotlin.math.absoluteValue

object MathUtil {

    fun greatestCommonDivisor(_a: Int, _b: Int): Int {
        if (_a == 0) return _b.absoluteValue
        if (_b == 0) return _a.absoluteValue

        var h: Int
        var a = _a
        var b = _b

        do {
            h = a % b
            a = b
            b = h
        } while (b != 0)

        return a.absoluteValue
    }

    fun possibleEvents(size: Int, exact: Int): Int {
        var events = 1
        var buffer = ""
        repeat(exact) { buffer += 'X' }
        repeat(size - exact) { buffer += '0' }

        while (true) {
            val reset = buffer.endsWith('X')
            val last0 = buffer.lastIndexOf('0')
            val lastXafter0 = buffer.lastIndexOf('X', last0)

            if (lastXafter0 == -1) return events

            if (!reset) {
                buffer = buffer.replaceRange(lastXafter0..(lastXafter0+1), "0X")
            }
            else {
                buffer = buffer.substring(0, lastXafter0)
                buffer += '0'
                repeat(size - last0) { buffer += 'X' }
                repeat(size - buffer.length) { buffer += '0' }
            }

            events++
        }
    }

}