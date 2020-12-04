package battles.utils

import T
import Tl
import kotlin.math.cos
import kotlin.math.sin

/**
 * a*sin(b*x)+c
 * a*cos(b*x)+c
 */
class TrigonometricFunction {

    val a: Double
    val b: Double
    val c: Double
    val cos: Boolean

    constructor(raw_: String) {
        val raw = raw_.substringAfter("f(x)=$T").substringBefore("$T ")

        var beforeBracket = raw.substringBefore("cos(").substringBefore("sin(")
        val inBracket = raw.substringAfter("($T").substringBefore("${T}x").substringBefore("x")
        var afterBracket = raw.substringAfter(")")
        if (beforeBracket.endsWith(T)) beforeBracket = beforeBracket.substring(0, beforeBracket.length - Tl)
        if (afterBracket.startsWith(T)) afterBracket = afterBracket.substring(Tl)

        a = when {
            beforeBracket == "" -> 1.0
            beforeBracket == "-" -> -1.0
            else -> beforeBracket.toDouble()
        }

        val inBracketSplit = inBracket.split(T)
        b = when {
            inBracket == "" -> 1.0
            inBracket == "-" -> -1.0
            inBracketSplit.size == 1 -> inBracket.toDouble()
            inBracketSplit.size == 2 -> inBracketSplit[0].toDouble() / inBracketSplit[1].toDouble()
            inBracketSplit.size == 3 -> inBracketSplit[1].toDouble() / inBracketSplit[2].toDouble() * -1
            else -> error("cannot interpret factor: $inBracket")
        }

        c = when {
            afterBracket == "" -> 0.0
            else -> afterBracket.toDouble()
        }

        cos = "cos" in raw
    }

    constructor(a: Double, b: Double, c: Double, cos: Boolean) {
        this.a = a
        this.b = b
        this.c = c
        this.cos = cos
    }

    fun replaceX(x: Double): Double {
        return a * (if (cos) cos(b * x) else sin(b * x)) + c
    }

    fun differentiate(): TrigonometricFunction {
        val a = this.a * b * if (cos) -1 else 1
        return TrigonometricFunction(a, b, 0.0, !cos)
    }

    override fun toString(): String {
        var function = ""

        if (a != 1.0) function += "$a*"
        function += if (cos) "cos" else "sin"
        function += "("
        if (b != 1.0) function += b
        function += "x)"
        if (c == 0.0);
        else if (c > 0.0) function += "+$c"
        else function += c

        return function
    }

}