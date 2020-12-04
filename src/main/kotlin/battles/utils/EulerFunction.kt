package battles.utils

import T
import Tl
import kotlin.math.ln
import kotlin.math.pow

/**
 * (ax^2+bx+c)e^(dx)+fe^(gx)
 */
class EulerFunction {

    val a: Double
    val b: Double
    val c: Double
    val d: Double
    val f: Double
    val g: Double

    /**
     * type=1: (ax^2+bx+c)e^(dx)
     * type=2: e^(dx)+fe^(gx)
     */
    constructor(raw: String, type: Int) {
        when (type) {
            1 -> {
                var beforeBracket = raw.substringBefore("(")
                var inBracket = raw.substringAfter("($T").substringBefore("$T)")
                var afterBracket = raw.substringAfter(") ")
                if (beforeBracket.endsWith(T)) beforeBracket = beforeBracket.substring(0, beforeBracket.length - Tl)

                if ("(" !in raw) {
                    beforeBracket = ""
                    inBracket = raw.substringBefore(" $T ·")
                    afterBracket = raw.substringAfter("·")
                }

                val beforeBracketSplit = beforeBracket.split(T)
                val bracketMultiplier = when {
                    beforeBracket == "" -> 1.0
                    beforeBracket == "-" -> -1.0
                    beforeBracketSplit.size == 1 -> inBracket.toDouble()
                    beforeBracketSplit.size == 2 -> beforeBracketSplit[0].toDouble() / beforeBracketSplit[1].toDouble()
                    beforeBracketSplit.size == 3 -> beforeBracketSplit[1].toDouble() / beforeBracketSplit[2].toDouble() * -1
                    else -> error("cannot interpret multiplier: $beforeBracket")
                }

                val bracketPolynom = PolynomialFunction(inBracket, 3)
                a = bracketMultiplier * (if (bracketPolynom.factors.size >= 3) bracketPolynom.factors[2] else 0.0)
                b = bracketMultiplier * (if (bracketPolynom.factors.size >= 2) bracketPolynom.factors[1] else 0.0)
                c = bracketMultiplier * (if (bracketPolynom.factors.size >= 1) bracketPolynom.factors[0] else 0.0)


                val exponent = afterBracket.substringAfter("e$T").substringBefore("${T}x").substringBefore("x")

                val exponentSplit = exponent.split(T)
                d = when {
                    exponent == "" -> 1.0
                    exponent == "-" -> -1.0
                    exponentSplit.size == 1 -> exponent.toDouble()
                    exponentSplit.size == 2 -> exponentSplit[0].toDouble() / exponentSplit[1].toDouble()
                    exponentSplit.size == 3 -> exponentSplit[1].toDouble() / exponentSplit[2].toDouble() * -1
                    else -> error("cannot interpret factor: $exponentSplit")
                }

                f = 0.0
                g = 0.0
            }
            2 -> {
                val rawD = raw.substringAfter("e$T").substringBefore("${T}x").substringBefore("x")
                val rawDsplit = rawD.split(T)
                d = when {
                    rawD == "" -> 1.0
                    rawD == "-" -> -1.0
                    rawDsplit.size == 1 -> rawD.toDouble()
                    rawDsplit.size == 2 -> rawDsplit[0].toDouble() / rawDsplit[1].toDouble()
                    rawDsplit.size == 3 -> rawDsplit[1].toDouble() / rawDsplit[2].toDouble() * -1
                    else -> error("cannot interpret factor: $rawD")
                }

                val rawF = raw.substringAfter("x$T").substringBefore("${T}e").substringBefore("e").trim()
                f = when {
                    rawF == "+" -> 1.0
                    rawF == "-" -> -1.0
                    else -> rawF.toDouble()
                }

                val rawG = raw.substringAfter("e").substringAfter("e$T").substringBefore("${T}x").substringBefore("x")
                val rawGsplit = rawG.split(T)
                g = when {
                    rawG == "" -> 1.0
                    rawG == "-" -> -1.0
                    rawGsplit.size == 1 -> rawG.toDouble()
                    rawGsplit.size == 2 -> rawGsplit[0].toDouble() / rawGsplit[1].toDouble()
                    rawGsplit.size == 3 -> rawGsplit[1].toDouble() / rawGsplit[2].toDouble() * -1
                    else -> error("cannot interpret factor: $rawG")
                }

                a = 0.0
                b = 0.0
                c = 1.0
            }
            else -> {
                a = 0.0
                b = 0.0
                c = 0.0
                d = 0.0
                f = 0.0
                g = 0.0
            }
        }
    }

    constructor(a: Double, b: Double, c: Double, d: Double, f: Double, g: Double) {
        this.a = a
        this.b = b
        this.c = c
        this.d = d
        this.f = f
        this.g = g
    }

    fun replaceX(x: Double): Double {
        return (a * x * x + b * x + c) * Math.E.pow(d * x) + f * Math.E.pow(g * x)
    }

    fun replaceYwith0(): ArrayList<Double> {
        if (f == 0.0)
            return PolynomialFunction(doubleArrayOf(c, b, a)).replaceYwith0()
        else if (a == 0.0 && b == 0.0 && c > 0.0 && f < 0) {
            val positions = ArrayList<Double>()
            positions += ln(-f / c) / (d - g)
            return positions
        }
        else
            error("not implemented yet: $this")
    }

    fun differentiate(): EulerFunction {
        return EulerFunction(d * a, 2 * a + d * b, b + d * c, d, f * g, g)
    }

    // (ax^2+bx+c)e^(dx)+fe^(gx)
    override fun toString(): String {
        var function = ""

        function += "("
        if (a != 0.0) function += "${a}x${Unicodes.superscriptNumber[2]}"
        if (b != 0.0) {
            if (b > 0) function += "+"
            function += "${b}x"
        }
        if (c != 0.0) {
            if (c > 0) function += "+"
            function += "$c"
        }
        function += ")e^"
        function += if (d == 1.0) "x" else "(${d}x)"
        if (f != 0.0) {
            if (f > 0) function += "+"
            if (f == -1.0) function += "-"
            else if (f != 1.0) function += "$f"
            function += "e^"
            function += if (g == 1.0) "x" else "(${g}x)"
        }

        return function
    }

}