private class Universe(private val grid: List<String>) {

    private val emptyRows = grid.indices.filter { y -> grid[0].indices.all { x -> grid[y][x] == '.' } }.toSet()
    private val emptyCols = grid[0].indices.filter { x -> grid.indices.all { y -> grid[y][x] == '.' } }.toSet()

    val galaxies = grid.indices.flatMap { y ->
        grid[0].indices.mapNotNull { x ->
            if (grid[y][x] == '#') Point(x, y) else null
        }
    }

    fun shortestPath(a: Point, b: Point, expansion: Long): Long {
        val empty = (a.x..b.x).normalize().intersect(emptyCols).size +
                (a.y..b.y).normalize().intersect(emptyRows).size

        return a.manhattanDistance(b) + empty * (expansion - 1)
    }
}

fun main() {
    fun solve(input: List<String>, expansion: Long): Long {
        val universe = Universe(input)

        return universe.galaxies.choosePairs().sumOf { (a, b) ->
            universe.shortestPath(a, b, expansion)
        }
    }

    fun part1(input: List<String>) = solve(input, expansion = 2)
    fun part2(input: List<String>) = solve(input, expansion = 1_000_000)

    check(part1(readInput("Day11_test")) == 374L)
    check(solve(readInput("Day11_test"), expansion = 10) == 1030L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
