package battles.utils

fun main(args: Array<String>) {
    println(Stochastic.probabilityScFlMmaxPb(1, 2, 2, 1))
}

object Stochastic {

    /**
     * @return how high is the probability that after given moves the success takes place (without putting back)
     */
    fun probabilityScFlM(sizeSuccess: Int, sizeFailure: Int, moves: Int): Pair<Int, Int> {
        var numerator = sizeSuccess
        var denominator = 1

        repeat(moves - 1) { numerator *= sizeFailure - it }
        repeat(moves) { denominator *= (sizeSuccess + sizeFailure) - it }

        val ggt = MathUtil.greatestCommonDivisor(numerator, denominator)
        numerator /= ggt
        denominator /= ggt

        return Pair(numerator, denominator)
    }

    /**
     * @return how high is the probability that after given moves that success takes place for an exact amount (without putting back)
     */
    fun probabilityScFlMExact(sizeSuccess: Int, sizeFailure: Int, moves: Int, exactSuccess: Int): Pair<Int, Int> {
        var numerator = 1
        var denominator = 1

        repeat(exactSuccess) { numerator *= sizeSuccess - it }
        repeat(moves - exactSuccess) { numerator *= sizeFailure - it }
        repeat(moves) { denominator *= (sizeSuccess + sizeFailure) - it }

        numerator *= MathUtil.possibleEvents(moves, exactSuccess)

        val ggt = MathUtil.greatestCommonDivisor(numerator, denominator)
        numerator /= ggt
        denominator /= ggt

        return Pair(numerator, denominator)
    }

    /**
     * @return how high is the probability that after given moves that success takes place for an exact amount (with putting back)
     */
    fun probabilityScFlMExactPb(sizeSuccess: Int, sizeFailure: Int, moves: Int, exactSuccess: Int): Pair<Int, Int> {
        var numerator = 1
        var denominator = 1

        repeat(exactSuccess) { numerator *= sizeSuccess }
        repeat(moves - exactSuccess) { numerator *= sizeFailure }
        repeat(moves) { denominator *= (sizeSuccess + sizeFailure) }

        numerator *= MathUtil.possibleEvents(moves, exactSuccess)

        val ggt = MathUtil.greatestCommonDivisor(numerator, denominator)
        numerator /= ggt
        denominator /= ggt

        return Pair(numerator, denominator)
    }

    /**
     * @return how high is the probability that after given moves that success takes place to a max amount(including) (with putting back)
     */
    fun probabilityScFlMmaxPb(sizeSuccess: Int, sizeFailure: Int, moves: Int, maxSuccess: Int): Pair<Int, Int> {
        var numerator = 0
        var denominator = 1

        for (i in 0..maxSuccess) {
            val pair = probabilityScFlMExactPb(sizeSuccess, sizeFailure, moves, i)

            val tmp = denominator
            numerator *= pair.second
            denominator *= pair.second

            numerator += pair.first * tmp

            val ggt = MathUtil.greatestCommonDivisor(numerator, denominator)
            numerator /= ggt
            denominator /= ggt
        }

        return Pair(numerator, denominator)
    }

}