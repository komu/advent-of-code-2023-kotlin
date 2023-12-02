private typealias Color = String

private class Constraint(val color: String, val count: Int) {
    companion object {
        private val COUNT_PATTERN = Regex("""(\d+) (.+)""")

        fun parse(s: String): Constraint {
            val (count, name) = COUNT_PATTERN.matchEntire(s)!!.destructured
            return Constraint(name, count.toInt())
        }

        fun parseConstraints(s: String): List<Constraint> {
            // Note that we don't to keep the constraints grouped, we can just list them all.
            // Therefore, we can just split using both ';' and ',' at the same time.
            return s.split("; ", ", ").map { parse(it) }
        }
    }
}

private class Game(val id: Int, val constraints: List<Constraint>) {

    fun isPossible(candidate: Map<Color, Int>) =
        constraints.all { it.count <= (candidate[it.color] ?: 0) }

    fun simplestSetProduct() =
        constraints
            .groupBy { it.color }
            .map { (_, constraintsForColor) -> constraintsForColor.maxOf { it.count } }
            .product()

    companion object {

        private val GAME_PATTERN = Regex("""Game (\d+): (.+)""")

        fun parse(line: String): Game {
            val (id, data) = GAME_PATTERN.matchEntire(line)!!.destructured
            return Game(
                id = id.toInt(),
                constraints = Constraint.parseConstraints(data)
            )
        }
    }
}

private fun Iterable<Int>.product() = fold(1) { a, b -> a * b }

fun main() {
    fun part1(input: List<String>): Int {
        val candidate = mapOf("red" to 12, "green" to 13, "blue" to 14)
        return input.map { Game.parse(it) }
            .filter { it.isPossible(candidate) }
            .sumOf { it.id }
    }

    fun part2(input: List<String>): Int =
        input.sumOf { Game.parse(it).simplestSetProduct() }

    check(part1(readInput("Day02_test")) == 8)
    check(part2(readInput("Day02_test")) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
