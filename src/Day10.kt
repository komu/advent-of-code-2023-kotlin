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

    private fun findLoopAndPointsOnLeftOfLoop(start: Point, initialDirection: CardinalDirection): Pair<Set<Point>, MutableSet<Point>>? {
        val loop = mutableSetOf<Point>()
        val left = mutableSetOf<Point>()

        var p = start
        var d = initialDirection

        while (loop.add(p)) {
            val ch = get(p)
                ?: return null

            // @formatter:off
            when (ch) {
                'S' -> p = p.towards(d)
                '|' -> when (d) {
                    N -> { left += p.left; p = p.up }
                    S -> { left += p.right; p = p.down }
                    else -> return null
                }
                '-' -> when (d) {
                    E -> { left += p.up; p = p.right }
                    W -> { left += p.down; p = p.left }
                    else -> return null
                }
                'L' -> when (d) {
                    S -> { p = p.right; d = E }
                    W -> { left += p.down; left += p.left; p = p.up; d = N }
                    else -> return null
                }
                'J' -> when (d) {
                    S -> { left += p.right; left += p.down; p = p.left; d = W }
                    E -> { p = p.up; d = N }
                    else -> return null
                }
                '7' -> when (d) {
                    N -> { p = p.left; d = W }
                    E -> { left += p.up; left += p.right; p = p.down; d = S }
                    else -> return null
                }
                'F' -> when (d) {
                    N -> { left += p.left; left += p.up; p = p.right; d = E }
                    W -> { p = p.down; d = S }
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

    fun floodFill(points: MutableSet<Point>, loop: Set<Point>) {
        val queue = points.toMutableList()

        while (queue.isNotEmpty())
            for (n in queue.removeLast().neighbors)
                if (inBounds(n) && n !in loop && points.add(n))
                    queue.add(n)
    }

    fun isOnBorder(p: Point) =
        p.neighbors.any { !inBounds(it) }
}

fun main() {
    fun part1(input: List<String>): Int =
        PipeGrid(input).findLoopAndPointsOnLeftOfLoop().first.size / 2

    fun part2(input: List<String>): Int {
        val grid = PipeGrid(input)

        val (loop, pointsOnLeft) = grid.findLoopAndPointsOnLeftOfLoop()

        grid.floodFill(pointsOnLeft, loop)

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
