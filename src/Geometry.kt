import Vector.Companion.DOWN
import Vector.Companion.LEFT
import Vector.Companion.RIGHT
import Vector.Companion.UP
import kotlin.math.abs

data class Point(val x: Int, val y: Int) {

    operator fun plus(v: Vector) =
        Point(x + v.dx, y + v.dy)

    operator fun plus(d: CardinalDirection) =
        this + d.vector

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

    companion object {
        fun inRange(xRange: IntRange, yRange: IntRange) =
            yRange.flatMap { y -> xRange.map { x -> Point(x, y) } }
    }
}

data class Vector(val dx: Int, val dy: Int) {

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


}
