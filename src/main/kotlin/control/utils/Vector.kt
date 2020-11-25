package control.utils

import T
import Tl
import kotlin.math.sqrt

class Vector {

    var a: Double
    var b: Double
    var c: Double

    constructor(raw_: String) {
        var raw = raw_
        if (raw.startsWith(T)) raw = raw.substring(Tl)
        if (raw.endsWith(T)) raw = raw.substring(0, raw.length-Tl)
        if (raw.startsWith("(")) raw = raw.substring(1)
        if (raw.endsWith(")")) raw = raw.substring(0, raw.length-1)
        if (raw.startsWith(T)) raw = raw.substring(Tl)
        if (raw.endsWith(T)) raw = raw.substring(0, raw.length-Tl)

        val split = raw.split(T)
        a = split[0].toDouble()
        b = split[1].toDouble()
        c = split[2].toDouble()
    }

    constructor(a: Double = 0.0, b: Double = 0.0, c: Double = 0.0) {
        this.a = a
        this.b = b
        this.c = c
    }

    fun product(v: Vector) = a * v.a + b * v.b + c * v.c

    fun crossProduct(v: Vector): Vector {
        return Vector(
            b * v.c - c * v.b,
            c * v.a - a * v.c,
            a * v.b - b * v.a,
        )
    }

    fun length() = sqrt(a*a + b*b + c*c)

    override fun toString() = "($a;$b;$c)"

}