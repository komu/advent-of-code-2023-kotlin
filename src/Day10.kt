import CardinalDirection.*

private class PipeGrid(private val lines: List<String>) {

    val height = lines.size
    val width = lines[0].length

    fun get(p: Point): Char? =
        lines.getOrNull(p.y)?.getOrNull(p.x)

    fun inBounds(p: Point) =
        p.x in (0..<width) && p.y in (0..<height)

    fun findLoopAndPointsOnLeftOfLoop(): Pair<Set<Point>, MutableSet<Point>> {
        val points = (0..<height).flatMap { y -> (0..<width).map { x -> Point(x, y) } }
        val start = points.first { get(it) == 'S' }

        return CardinalDirection.entries.firstNotNullOf { findLoopAndPointsOnLeftOfLoop(start, it) }
    }

    private fun findLoopAndPointsOnLeftOfLoop(
        start: Point,
        initialDirection: CardinalDirection
    ): Pair<Set<Point>, MutableSet<Point>>? {
        val loop = mutableSetOf<Point>()
        val left = mutableSetOf<Point>()

        var position = start
        var direction = initialDirection

        fun collectLeft(vararg ds: CardinalDirection) {
            for (d in ds)
                left.add(position + d)
        }

        fun move(newDirection: CardinalDirection) {
            position += newDirection
            direction = newDirection
        }

        while (loop.add(position)) {
            val ch = get(position)
                ?: return null

            // @formatter:off
            when (ch) {
                'S' -> move(direction)
                '|' -> when (direction) {
                    N -> { collectLeft(W); move(N) }
                    S -> { collectLeft(E); move(S) }
                    else -> return null
                }
                '-' -> when (direction) {
                    E -> { collectLeft(N); move(E) }
                    W -> { collectLeft(S); move(W) }
                    else -> return null
                }
                'L' -> when (direction) {
                    S -> { move(E) }
                    W -> { collectLeft(S, W); move(N) }
                    else -> return null
                }
                'J' -> when (direction) {
                    S -> { collectLeft(E, S); move(W) }
                    E -> { move(N) }
                    else -> return null
                }
                '7' -> when (direction) {
                    N -> { move(W) }
                    E -> { collectLeft(N, E); move(S) }
                    else -> return null
                }
                'F' -> when (direction) {
                    N -> { collectLeft(W, N); move(E) }
                    W -> { move(S) }
                    else -> return null
                }
                '.' -> return null
                else -> error("unknown char '$ch'")
            }
            // @formatter:on
        }

        left.removeIf { !inBounds(it) || it in loop }

        return Pair(loop, left)
    }

    fun isOnBorder(p: Point) =
        p.neighbors.any { !inBounds(it) }
}

private inline fun MutableSet<Point>.floodFill(predicate: (Point) -> Boolean) {
    val queue = toMutableList()

    while (queue.isNotEmpty())
        for (n in queue.removeLast().neighbors)
            if (predicate(n) && add(n))
                queue.add(n)
}

fun main() {
    fun part1(input: List<String>): Int =
        PipeGrid(input).findLoopAndPointsOnLeftOfLoop().first.size / 2

    fun part2(input: List<String>): Int {
        val grid = PipeGrid(input)

        val (loop, pointsOnLeft) = grid.findLoopAndPointsOnLeftOfLoop()

        pointsOnLeft.floodFill { neighbor -> neighbor !in loop && grid.inBounds(neighbor) }

        val leftIsInside = pointsOnLeft.none { grid.isOnBorder(it) }
        return if (leftIsInside)
            pointsOnLeft.size
        else
            (grid.width * grid.height) - (pointsOnLeft.size + loop.size)
    }

    check(part1(readInput("Day10_test")) == 8)
    check(part2(readInput("Day10_test2")) == 4)
    check(part2(readInput("Day10_test3")) == 4)
    check(part2(readInput("Day10_test4")) == 8)
    check(part2(readInput("Day10_test5")) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
