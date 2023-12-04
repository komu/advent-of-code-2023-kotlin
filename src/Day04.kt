private class Card(val id: Int, winningNumbers: List<Int>, actualNumbers: List<Int>) {

    val matches = actualNumbers.intersect(winningNumbers).size
    val score = if (matches == 0) 0 else 1 shl (matches - 1)
    val copies = (1..matches).map { id + it }

    companion object {

        private val PATTERN = Regex("""Card\s+(\d+): (.+) \| (.+)""")
        private val SPACE = Regex("""\s+""")

        fun parse(s: String): Card {
            val (id, winning, actual) = PATTERN.matchEntire(s)!!.destructured

            return Card(
                id = id.toInt(),
                winningNumbers = winning.trim().split(SPACE).map { it.toInt() },
                actualNumbers = actual.trim().split(SPACE).map { it.toInt() }
            )
        }
    }
}

fun main() {
    fun part1(input: List<String>) =
        input.sumOf { Card.parse(it).score }

    fun part2(input: List<String>): Int {
        val cards = input.map { Card.parse(it) }
        val counts = Array(cards.size) { 1 }

        for (card in cards.reversed())
            for (copyId in card.copies)
                counts[card.id - 1] += counts[copyId - 1]

        return counts.sum()
    }

    check(part1(readInput("Day04_test")) == 13)
    check(part2(readInput("Day04_test")) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
