private fun wins(time: Long, distance: Long): Int =
    (1..time).count { charge ->
        charge * (time - charge) > distance
    }

fun main() {
    fun part1(input: List<String>): Int {
        val times = input[0].removePrefix("Time:").trim().words().map { it.toLong() }
        val distances = input[1].removePrefix("Distance:").trim().words().map { it.toLong() }

        return times.zip(distances).productOf { (time, distance) -> wins(time, distance) }
    }

    fun part2(input: List<String>): Int {
        val time = input[0].removePrefix("Time:").trim().words().joinToString("").toLong()
        val distance = input[1].removePrefix("Distance:").trim().words().joinToString("").toLong()

        return wins(time, distance)
    }

    check(part1(readInput("Day06_test")) == 288)
    check(part2(readInput("Day06_test")) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
