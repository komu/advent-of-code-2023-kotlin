fun main() {

    fun List<Int>.deltas(): List<List<Int>> {
        var h = this

        val deltas = mutableListOf(h)
        while (!h.all { it == 0 }) {
            h = h.windowed(2).map { it[1] - it[0] }
            deltas.add(h)
        }

        return deltas
    }

    fun List<List<Int>>.extrapolate(): Int =
        foldRight(0) { ds, x -> ds.last() + x }

    fun List<List<Int>>.extrapolateBackwards(): Int =
        foldRight(0) { ds, x -> ds.first() - x }

    fun part1(input: List<String>): Int =
        input.sumOf { it.toIntList().deltas().extrapolate() }

    fun part2(input: List<String>): Int =
        input.sumOf { it.toIntList().deltas().extrapolateBackwards() }

    check(part1(readInput("Day09_test")) == 114)
    check(part2(readInput("Day09_test")) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
