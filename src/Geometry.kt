import Vector.Companion.DOWN
import Vector.Companion.LEFT
import Vector.Companion.RIGHT
import Vector.Companion.UP
import kotlin.math.abs
import kotlin.math.sign

data class Point(val x: Int, val y: Int) {

    operator fun plus(v: Vector) =
        Point(x + v.dx, y + v.dy)

    operator fun plus(d: CardinalDirection) =
        this + d.vector

    operator fun minus(v: Point) =
        Vector(x - v.x, y - v.y)

    operator fun minus(v: Vector) =
        Point(x - v.dx, y - v.dy)

    fun manhattanDistance(p: Point) =
        abs(x - p.x) + abs(y - p.y)

    val neighbors: List<Point>
        get() = buildList {
            for (dy in listOf(-1, 0, 1))
                for (dx in listOf(-1, 0, 1))
                    if (dx != 0 || dy != 0)
                        add(Point(x + dx, y + dy))
        }

    val cardinalNeighbors: List<Point>
        get() = CardinalDirection.entries.map { this + it }

    companion object {
        val ORIGIN = Point(0, 0)
        fun inRange(xRange: IntRange, yRange: IntRange) =
            yRange.flatMap { y -> xRange.map { x -> Point(x, y) } }
    }
}

data class Vector(val dx: Int = 0, val dy: Int = 0) {

    operator fun times(s: Int) = Vector(s * dx, s * dy)
    operator fun plus(v: Vector) = Vector(dx + v.dx, dy + v.dy)
    operator fun unaryMinus() = Vector(-dx, -dy)

    companion object {
        val UP = Vector(0, -1)
        val DOWN = Vector(0, 1)
        val LEFT = Vector(-1, 0)
        val RIGHT = Vector(1, 0)
    }
}

operator fun Int.times(v: Vector) = v * this

enum class CardinalDirection(val vector: Vector) {
    N(UP), W(LEFT), S(DOWN), E(RIGHT);

    fun left() = when (this) {
        N -> W
        W -> S
        S -> E
        E -> N
    }

    fun right() = when (this) {
        N -> E
        W -> N
        S -> W
        E -> S
    }

    companion object {
        fun between(a: Point, b: Point): CardinalDirection? {
            val dx = (b.x - a.x).sign
            val dy = (b.y - a.y).sign

            require(dx == 0 || dy == 0)

            return when {
                dx == 1 -> E
                dx == -1 -> W
                dy == -1 -> N
                dy == 1 -> S
                else -> null
            }
        }
    }
}

operator fun Int.times(d: CardinalDirection) = this * d.vector
