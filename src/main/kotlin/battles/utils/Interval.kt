package battles.utils

import T
import Tl

class Interval {

    val a: Double
    val b: Double

    constructor(raw_: String) {
        var raw = raw_.substringAfter("[").substringBefore(")")
        if (raw.endsWith(T)) raw = raw.substring(0, raw.length - Tl)

        val rawSplit = raw.split(";")
        val ma = if ("π" in rawSplit[0]) Math.PI else 1.0
        val mb = if ("π" in rawSplit[1]) Math.PI else 1.0

        var rawA = rawSplit[0].replace("π", "")
        if (rawA.startsWith(T)) rawA = rawA.substring(Tl)
        if (rawA.endsWith(T)) rawA = rawA.substring(0, rawA.length - Tl)

        val rawAsplit = rawA.split(T)
        a = ma * when {
            rawA.isEmpty() -> 1.0
            rawAsplit.size == 1 -> rawA.toDouble()
            rawAsplit.size == 2 -> rawAsplit[0].toDouble() / rawAsplit[1].toDouble()
            rawAsplit.size == 3 -> rawAsplit[1].toDouble() / rawAsplit[2].toDouble() * -1
            else -> error("cannot interpret $rawA")
        }

        var rawB = rawSplit[1].replace("π", "")
        if (rawB.startsWith(T)) rawB = rawB.substring(Tl)
        if (rawB.endsWith(T)) rawB = rawB.substring(0, rawB.length - Tl)

        val rawBsplit = rawB.split(T)
        b = mb * when {
            rawB.isEmpty() -> 1.0
            rawBsplit.size == 1 -> rawB.toDouble()
            rawBsplit.size == 2 -> rawBsplit[0].toDouble() / rawBsplit[1].toDouble()
            rawBsplit.size == 3 -> rawBsplit[1].toDouble() / rawBsplit[2].toDouble() * -1
            else -> error("cannot interpret $rawB")
        }
    }

    constructor(a: Double, b: Double) {
        this.a = a
        this.b = b
    }

    override fun toString() = "[$a;$b]"

}