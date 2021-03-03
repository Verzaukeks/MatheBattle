package battles.utils.function

import T
import Tl
import battles.utils.Unicodes
import kotlin.math.pow
import kotlin.math.sqrt

class PolynomialFunction {

    val factors: DoubleArray

    /**
     * type=1: a(bx+c)^n+dx^z
     * type=2: x^n*(ax+b)^z
     * type=3: +loop[ax^n]
     */
    constructor(raw_: String, type: Int) {
        when (type) {
            1 -> {
                val raw = raw_.substringAfter("f(x)=$T").substringBefore("$T:$T")

                var beforeBracket = raw.substringBefore("(")
                val inBracket = raw.substringAfter("( $T").substringBefore("$T )")
                val afterBracket = raw.substringAfter(")$T")
                if (beforeBracket.endsWith(T)) beforeBracket = beforeBracket.substring(0, beforeBracket.length - Tl)

                val beforeBracketSplit = beforeBracket.split(T)
                val bracketMultiplier = when {
                    beforeBracket == "" -> 1.0
                    beforeBracket == "-" -> -1.0
                    beforeBracketSplit.size == 1 -> beforeBracket.toDouble()
                    beforeBracketSplit.size == 2 -> beforeBracketSplit[0].toDouble() / beforeBracketSplit[1].toDouble()
                    beforeBracketSplit.size == 3 -> beforeBracketSplit[1].toDouble() / beforeBracketSplit[2].toDouble() * -1
                    else -> error("cannot interpret: $beforeBracket")
                }

                val rawInBracketXfactor = inBracket.substringBefore("x")
                val inBracketXfactor = when (rawInBracketXfactor) {
                    "" -> 1.0
                    "-" -> -1.0
                    else -> rawInBracketXfactor.toDouble()
                }

                val inBracketD = inBracket.substringAfter("x$T").toDouble()

                val exponent = afterBracket.substringBefore(T).toInt()

                val rawAfterBracketAdd = afterBracket.substringAfter(T).substringBefore("${T}x").substringBefore("x")
                val afterBracketAddSplit = rawAfterBracketAdd.split(T)
                val afterBracketAdd = when {
                    rawAfterBracketAdd == "+" -> 1.0
                    rawAfterBracketAdd == "-" -> -1.0
                    afterBracketAddSplit.size == 1 -> rawAfterBracketAdd.toDouble()
                    afterBracketAddSplit.size == 3 && afterBracketAddSplit[0] == "+" -> afterBracketAddSplit[1].toDouble() / afterBracketAddSplit[2].toDouble()
                    afterBracketAddSplit.size == 3 && afterBracketAddSplit[0] == "-" -> afterBracketAddSplit[1].toDouble() / afterBracketAddSplit[2].toDouble() * -1
                    else -> error("cannot interpret: $rawAfterBracketAdd")
                }

                val afterBracketExponent = when {
                    "x" !in afterBracket -> 0
                    else -> afterBracket.substringAfter("x$T", "1").toInt()
                }

                factors = DoubleArray(4)
                when (exponent) {
                    2 -> {
                        factors[0] = bracketMultiplier * (inBracketD * inBracketD)
                        factors[1] = bracketMultiplier * (2 * inBracketXfactor * inBracketD)
                        factors[2] = bracketMultiplier * (inBracketXfactor * inBracketXfactor)
                    }
                    3 -> {
                        factors[0] = bracketMultiplier * (inBracketD * inBracketD * inBracketD)
                        factors[1] = bracketMultiplier * (3 * inBracketXfactor * inBracketD * inBracketD)
                        factors[2] = bracketMultiplier * (3 * inBracketXfactor * inBracketXfactor * inBracketD)
                        factors[3] = bracketMultiplier * (inBracketXfactor * inBracketXfactor * inBracketXfactor)
                    }
                    else -> error("cannot interpret exponent: $exponent")
                }

                when (afterBracketExponent) {
                    0 -> factors[0] += afterBracketAdd
                    1 -> factors[1] += afterBracketAdd
                    2 -> factors[2] += afterBracketAdd
                    else -> error("cannot interpret exponent: $afterBracketExponent")
                }
            }
            2 -> {
                val raw = raw_.substringAfter("f(x)=$T").substringBefore("$T:$T")

                val beforeBracketExponent = raw
                        .substringAfter("x$T").substringBefore(" $T ·")
                        .substringBefore(T, "1").toInt()
                val inBracket = raw
                        .substringAfter("( $T").substringBefore("$T )")
                        .substringAfter("($T").substringBefore("$T)")
                val afterBracketExponent = raw
                        .substringAfter(")").substringAfter(T, "1").toInt()


                val rawInBracketXfactor = inBracket.substringBefore("x")
                val inBracketXfactor = when (rawInBracketXfactor) {
                    "" -> 1.0
                    "-" -> -1.0
                    else -> rawInBracketXfactor.toDouble()
                }

                val inBracketD = inBracket.substringAfter("x$T").toDouble()

                factors = DoubleArray(5)
                when (afterBracketExponent) {
                    1 -> {
                        factors[0] = inBracketD
                        factors[1] = inBracketXfactor
                    }
                    2 -> {
                        factors[0] = inBracketD * inBracketD
                        factors[1] = 2 * inBracketXfactor * inBracketD
                        factors[2] = inBracketXfactor * inBracketXfactor
                    }
                    else -> error("cannot interpret exponent: $afterBracketExponent")
                }

                when (beforeBracketExponent) {
                    1 -> {
                        factors[3] = factors[2]
                        factors[2] = factors[1]
                        factors[1] = factors[0]
                        factors[0] = 0.0
                    }
                    2 -> {
                        factors[4] = factors[2]
                        factors[3] = factors[1]
                        factors[2] = factors[0]
                        factors[1] = 0.0
                        factors[0] = 0.0
                    }
                    else -> error("cannot interpret exponent: $beforeBracketExponent")
                }
            }
            3 -> {
                var raw = raw_.substringAfter("f(x)=$T").substringBefore("$T $T").substringBefore("$T:$T")
                raw = raw.replace(" ", "")
                factors = DoubleArray(8)

                while (raw.isNotEmpty()) {
                    val toIndexPlus = raw.indexOf("+", 1)
                    val toIndexMinus = raw.indexOf("-", 1)
                    val toIndex = when {
                        toIndexPlus == -1 && toIndexMinus == -1 -> raw.length
                        toIndexPlus == -1 -> toIndexMinus
                        toIndexMinus == -1 -> toIndexPlus
                        toIndexMinus < toIndexPlus -> toIndexMinus
                        else -> toIndexPlus
                    }

                    val arg = raw.substring(0, toIndex)
                    raw = raw.substring(toIndex)

                    val beforeX = arg.substringBefore("${T}x").substringBefore("x")
                    var afterX = arg.substringAfter("x", "0")
                    if (afterX.startsWith(T)) afterX = afterX.substring(Tl)
                    if (afterX.endsWith(T)) afterX = afterX.substring(0, afterX.length - Tl)

                    val beforeXsplit = beforeX.split(T)
                    val factor = when {
                        beforeX == "" -> 1.0
                        beforeX == "+" -> 1.0
                        beforeX == "-" -> -1.0
                        beforeXsplit.size == 1 -> beforeX.toDouble()
                        beforeXsplit.size == 2 -> beforeXsplit[0].toDouble() / beforeXsplit[1].toDouble()
                        beforeXsplit.size == 3 -> beforeXsplit[1].toDouble() / beforeXsplit[2].toDouble() * when {
                            beforeXsplit[0] == "+" -> 1
                            beforeXsplit[0] == "-" -> -1
                            else -> error("cannot interpret factor: $beforeX")
                        }
                        else -> error("cannot interpret factor: $beforeX")
                    }

                    val exponent = when {
                        afterX == "" -> 1
                        else -> afterX.toInt()
                    }

                    if (exponent >= factors.size)
                        error("cannot interpret exponent: $exponent")

                    factors[exponent] = factor
                }
            }
            else -> {
                factors = doubleArrayOf(0.0)
            }
        }
    }

    constructor(factors: DoubleArray) {
        this.factors = factors
    }

    fun getHighestExponent(): Int {
        for (index in factors.size-1 downTo 0)
            if (factors[index] != 0.0)
                return index
        return 0
    }

    fun replaceX(x: Double): Double {
        var value = 0.0
        for (index in factors.indices)
            value += factors[index] * x.pow(index)
        return value
    }

    fun replaceYwith0(): ArrayList<Double> {
        val positions = ArrayList<Double>()
        when (getHighestExponent()) {
            1 -> {
                positions += factors[0] * -1 / factors[1]
            }
            2 -> {
                val d = factors[1] * factors[1] - 4 * factors[2] * factors[0]
                if (d == 0.0) {
                    positions += (-1 * factors[1]) / (2 * factors[2])
                } else if (d > 0.0) {
                    positions += (-1 * factors[1] - sqrt(d)) / (2 * factors[2])
                    positions += (-1 * factors[1] + sqrt(d)) / (2 * factors[2])
                }
            }
            3 -> replaceYwith0(1, positions)
            4 -> replaceYwith0(2, positions)
            5 -> replaceYwith0(3, positions)
            else -> error("not implemented yet: replaceYwith0::exponent=${getHighestExponent()}")
        }
        return positions
    }

    // factorise x^shift*(...) if possible
    private fun replaceYwith0(shift: Int, positions: ArrayList<Double>) {
        for (index in 0 until shift)
            if (factors[index] != 0.0)
                error("not implemented yet: replaceYwith0::$this")

        val bracketF = PolynomialFunction(doubleArrayOf(
                factors[0 + shift],
                factors[1 + shift],
                factors[2 + shift],
        ))
        positions.add(0.0)
        positions.addAll(bracketF.replaceYwith0())
    }

    fun differentiate(): PolynomialFunction {
        if (factors.size <= 1)
            return PolynomialFunction(doubleArrayOf(0.0))

        val fac = DoubleArray(factors.size - 1)
        for (index in 0..(factors.size - 2))
            fac[index] = factors[index + 1] * (index + 1)
        return PolynomialFunction(fac)
    }

    override fun toString(): String {
        var function = ""

        for (index in factors.size - 1 downTo 0) {
            val factor = factors[index]

            if (factor == 0.0) continue
            if (factor > 0) function += "+"
            if (factor == factor.toInt().toDouble()) function += factor.toInt()
            else function += factor

            if (index == 0) continue
            function += "x"

            if (index == 1) continue
            function += Unicodes.superscriptNumber[index]
        }
        return function
    }

}