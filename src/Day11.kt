private class Universe(private val grid: List<String>) {

    private val emptyRows = grid.indices.filter { y -> grid[0].indices.all { x -> grid[y][x] == '.' } }.toSet()
    private val emptyCols = grid[0].indices.filter { x -> grid.indices.all { y -> grid[y][x] == '.' } }.toSet()

    val galaxies = grid.indices.flatMap { y ->
        grid[0].indices.mapNotNull { x ->
            if (grid[y][x] == '#') Point(x, y) else null
        }
    }

    fun shortestPath(a: Point, b: Point, expansion: Long): Long {
        val xs = minOf(a.x, b.x)..maxOf(a.x, b.x)
        val ys = minOf(a.y, b.y)..maxOf(a.y, b.y)

        return (xs.last - xs.first) +
                (ys.last - ys.first) +
                xs.intersect(emptyCols).size * expansion +
                ys.intersect(emptyRows).size * expansion
    }
}

fun main() {
    fun solve(input: List<String>, expansion: Long): Long {
        val universe = Universe(input)

        return universe.galaxies.choosePairs().sumOf { (a, b) ->
            universe.shortestPath(a, b, expansion)
        }
    }

    fun part1(input: List<String>) = solve(input, expansion = 1)
    fun part2(input: List<String>) = solve(input, expansion = 999_999)

    check(part1(readInput("Day11_test")) == 374L)
    check(solve(readInput("Day11_test"), expansion = 9) == 1030L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
