package control.utils

import T
import Tl

class Ebene {

    companion object {
        fun extraCut(ebene: String): String {
            if (ebene.startsWith("$T ")) return ebene.substring(Tl+1)
            return ebene
        }
    }

    var n: Vector
    val d: Double

    constructor(raw_: String) {
        if (raw_.startsWith(T)) {
            var raw = raw_
                    .substringBefore("=")
                    .replace(T, "")
            var x1 = raw.substringBefore("x1")
            var x2 = raw.substringAfter("x1").substringBefore("x2")
            var x3 = raw.substringAfter("x1").substringAfter("x2").substringBefore("x3")
            if (x1.isEmpty()) x1 = "1"
            if (x2.isEmpty()) x2 = "1"
            if (x3.isEmpty()) x3 = "1"
            if ("x1" !in raw) x1 = "0"
            if ("x2" !in raw) x2 = "0"
            if ("x3" !in raw) x3 = "0"
            if (x1 == "+") x1 = "1"
            if (x2 == "+") x2 = "1"
            if (x3 == "+") x3 = "1"
            if (x1 == "-") x1 = "-1"
            if (x2 == "-") x2 = "-1"
            if (x3 == "-") x3 = "-1"

            n = Vector(
                x1.toDouble(),
                x2.toDouble(),
                x3.toDouble(),
            )

            raw = raw_.substringAfter("=")
            if (raw.startsWith(T)) raw = raw.substring(Tl)
            if (raw.endsWith(T)) raw = raw.substring(0, raw.length-Tl)
            d = raw.toDouble()
        } else {
            n = Vector()
            d = 0.0
        }
    }

    constructor(n: Vector, d: Double) {
        this.n = n;
        this.d = d;
    }

    override fun toString(): String {
        val a = if (n.a == 0.0) "" else { if (n.a > 0) "+" else { "" } + n.a + "(x1)" }
        val b = if (n.b == 0.0) "" else { if (n.b > 0) "+" else { "" } + n.b + "(x2)" }
        val c = if (n.c == 0.0) "" else { if (n.c > 0) "+" else { "" } + n.c + "(x3)" }
        return "$a$b$c=$d"
    }

}