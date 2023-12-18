import CardinalDirection.*
import kotlin.math.absoluteValue

private class DigInstruction(val dir: CardinalDirection, val meters: Int) {

    companion object {

        private val directionDigits = listOf(E, S, W, N)
        private val directionCodes = mapOf('U' to N, 'D' to S, 'L' to W, 'R' to E)

        fun parse1(s: String) = DigInstruction(
            dir = directionCodes[s[0]]!!,
            meters = s.substring(2, s.length - 10).toInt()
        )

        fun parse2(s: String) = DigInstruction(
            dir = directionDigits[s[s.length - 2].digitToInt()],
            meters = s.substring(s.length - 7, s.length - 2).toInt(radix = 16)
        )
    }
}

private fun shoelace(ps: List<Point>): Long =
    ps.indices.sumOf { i ->
        val p1 = ps[i]
        val p2 = ps[(i + 1) % ps.size]

        p1.x.toLong() * p2.y.toLong() - p1.y.toLong() * p2.x.toLong()
    }.absoluteValue / 2

fun main() {

    fun solve(instructions: List<DigInstruction>): Long {
        val vertices = instructions.scan(Point(0, 0)) { p, i -> p + i.meters * i.dir }.drop(1)
        val circumference = instructions.sumOf { it.meters.toLong() }

        return shoelace(vertices) + circumference / 2 + 1
    }

    fun part1(input: List<String>) = solve(input.map { DigInstruction.parse1(it) })
    fun part2(input: List<String>) = solve(input.map { DigInstruction.parse2(it) })

    assertEquals(part1(readInput("Day18_test")), 62L)
    assertEquals(part2(readInput("Day18_test")), 952408144115L)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
