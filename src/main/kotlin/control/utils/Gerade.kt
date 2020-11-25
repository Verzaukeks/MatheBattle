package control.utils

import T

class Gerade {

    var p: Vector
    var u: Vector

    constructor(raw: String) {
        if (raw.startsWith("${T}x${T}→${T}=")) {
            p = raw.substringAfter("=").substringBefore("+").let { Vector(it) }
            u = raw.substringAfter("t⋅").substringBefore(" ").let { Vector(it) }
        } else {
            p = Vector()
            u = Vector()
        }
    }

    constructor(p: Vector, u: Vector) {
        this.p = p
        this.u = u
    }

    override fun toString() = "x→=$p+t*$u"

}