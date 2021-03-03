package battles.utils.vector

import T

class VectorLine {

    var p: Vector
    var u: Vector

    constructor(raw: String) {
        if (raw.startsWith("${T}x${T}→${T}=")) {
            p = raw.substringAfter("=").substringBefore("+").let(::Vector)
            u = raw.substringAfter("t⋅").substringBefore(" ").let(::Vector)
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