private class Race(val time: Long, val distance: Long) {

    fun wins(): Int =
        (1..time).count { charge ->
            charge * (time - charge) > distance
        }

    companion object {

        val tests = listOf(
            Race(7, 9),
            Race(15, 40),
            Race(30, 200),
        )

        val real = listOf<Race>() // replace with real data
    }
}

fun main() {
    fun part1(races: List<Race>) =
        races.productOf { it.wins() }

    fun part2(races: List<Race>): Int {
        val race = Race(
            time = races.joinToString("") { it.time.toString() }.toLong(),
            distance = races.joinToString("") { it.distance.toString() }.toLong()
        )
        return race.wins()
    }

    check(part1(Race.tests) == 288)
    check(part2(Race.tests) == 71503)

    part1(Race.real).println()
    part2(Race.real).println()
}
