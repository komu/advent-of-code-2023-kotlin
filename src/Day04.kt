private data class Card(val id: Int, val winningNumbers: List<Int>, val actualNumbers: List<Int>) {

    val matches = actualNumbers.count { it in winningNumbers }
    val score = if (matches == 0) 0 else 1 shl (matches - 1)
    val copies = (1..matches).map { id + it }

    companion object {

        private val PATTERN = Regex("""Card\s+(\d+): (.+) \| (.+)""")
        private val SPACE = Regex("""\s+""")
        fun parse(s: String): Card {
            val (id, winning, actual) = PATTERN.matchEntire(s)!!.destructured
            return Card(
                id = id.toInt(),
                winningNumbers = winning.trim().split(SPACE).map { it.trim().toInt() },
                actualNumbers = actual.trim().split(SPACE).map { it.trim().toInt() }
            )
        }
    }
}

fun main() {
    fun part1(input: List<String>) =
        input.sumOf { Card.parse(it).score }

    fun part2(input: List<String>): Int {
        val cardsById = input.map { Card.parse(it) }.associateBy { it.id }

        var total = 0
        val queue = cardsById.keys.toMutableList()

        while (queue.isNotEmpty()) {
            queue += cardsById[queue.removeLast()]!!.copies
            total += 1
        }

        return total
    }

    check(part1(readInput("Day04_test")) == 13)
    check(part2(readInput("Day04_test")) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
