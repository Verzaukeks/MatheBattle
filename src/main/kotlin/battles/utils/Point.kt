package battles.utils

class Point {

    var a: Double
    var b: Double
    var c: Double

    constructor(raw_: String) {
        var raw = raw_
        if (raw.isNotEmpty()) {
            if (raw.startsWith("(")) raw = raw.substring(1)
            if (raw.endsWith(")")) raw = raw.substring(0, raw.length-1)

            val split = raw.split("|")
            a = split[0].toDouble()
            b = split[1].toDouble()
            c = split[2].toDouble()
        } else {
            a = 0.0
            b = 0.0
            c = 0.0
        }
    }

    constructor(a: Double = 0.0, b: Double = 0.0, c: Double = 0.0) {
        this.a = a
        this.b = b
        this.c = c
    }

    operator fun plus(v: Vector) = Point(a + v.a, b + v.b, c + v.c)

    fun vectorTo(p: Point) = Vector(p.a - a, p.b - b, p.c - c)

    override fun toString() = "($a|$b|$c)"

}