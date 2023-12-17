import utils.shortestPathWithCost

private class HeatMap(val grid: List<String>, straight: IntRange) {

    val height = grid.size
    val width = grid[0].length
    val end = Point(width - 1, height - 1)
    val straightMin = straight.first
    val straightMax = straight.last

    fun cost(point: Point) =
        grid[point.y][point.x].digitToInt()

    fun isInBounds(p: Point) =
        p.x in 0..<width && p.y in 0..<height

    fun transitions(state: SearchState): List<Pair<SearchState, Int>> {
        val result = mutableListOf<Pair<SearchState, Int>>()

        fun move(direction: CardinalDirection, straightMoves: Int) {
            val newState = SearchState(state.point + direction, direction, straightMoves)
            if (isInBounds(newState.point))
                result.add(newState to cost(newState.point))
        }

        if (state.straightMoves < straightMax)
            move(state.direction, straightMoves = state.straightMoves + 1)

        if (state.straightMoves >= straightMin) {
            move(state.direction.left(), straightMoves = 1)
            move(state.direction.right(), straightMoves = 1)
        }

        return result
    }

    fun isEnd(state: SearchState) =
        state.point == end && state.straightMoves >= straightMin
}

private data class SearchState(val point: Point, val direction: CardinalDirection, val straightMoves: Int)

private fun solve(map: HeatMap): Int {
    val start = SearchState(Point(0, 0), CardinalDirection.E, straightMoves = 1)

    val (_, cost) = shortestPathWithCost(start, { map.isEnd(it) }) { map.transitions(it) } ?: error("no path")
    return cost
}

fun main() {
    fun part1(input: List<String>) = solve(HeatMap(input, straight = 0..3))
    fun part2(input: List<String>) = solve(HeatMap(input, straight = 4..10))

    assertEquals(part1(readInput("Day17_test")), 102, "part1")
    assertEquals(part2(readInput("Day17_test")), 94, "part2 A")
    assertEquals(part2(readInput("Day17_test2")), 71, "part2 B")

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
