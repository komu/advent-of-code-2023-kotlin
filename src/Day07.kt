import CardType.JOKER
import HandType.*

private enum class CardType(private val code: Char) {
    A('A'), K('K'), Q('Q'), J('J'), T('T'), N9('9'), N8('8'), N7('7'), N6('6'), N5('5'), N4('4'), N3('3'), N2('2'),
    JOKER('*');

    companion object {
        fun forCode(code: Char, jokers: Boolean) =
            if (code == 'J' && jokers) JOKER else entries.first { it.code == code }
    }
}

private enum class HandType {
    FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIRS, ONE_PAIR, HIGH
}

private data class Hand(val cards: List<CardType>) : Comparable<Hand> {

    fun analyze(): HandType {
        val jokers = cards.count { it == JOKER }
        val counts = cards.filter { it != JOKER }.groupingBy { it }.eachCount().values.sortedDescending()
        if (counts.isEmpty())
            return FIVE_OF_A_KIND
        val bestCount = counts.first() + jokers

        return when {
            bestCount == 5 -> FIVE_OF_A_KIND
            bestCount == 4 -> FOUR_OF_A_KIND
            counts.size == 2 -> FULL_HOUSE
            bestCount == 3 -> THREE_OF_A_KIND
            counts.size == 3 && counts[0] == 2 && counts[1] + jokers == 2 -> TWO_PAIRS
            bestCount == 2 -> ONE_PAIR
            else -> HIGH
        }
    }

    override fun compareTo(other: Hand): Int {
        val v = analyze().compareTo(other.analyze())
        if (v != 0)
            return v

        for ((c1, c2) in cards.zip(other.cards)) {
            val v2 = c1.compareTo(c2)
            if (v2 != 0)
                return v2
        }

        return 0
    }
}

private data class HandAndBid(val hand: Hand, val bid: Int) {

    companion object {
        fun parse(s: String, jokers: Boolean) = HandAndBid(
            hand = Hand(s.take(5).map { CardType.forCode(it, jokers) }),
            bid = s.drop(6).toInt()
        )
    }
}

private fun List<HandAndBid>.winnings(): Int =
    sortedByDescending { it.hand }
        .withIndex()
        .sumOf { (i, h) -> (i + 1) * h.bid }

fun main() {

    fun part1(input: List<String>) =
        input.map { HandAndBid.parse(it, jokers = false) }.winnings()

    fun part2(input: List<String>) =
        input.map { HandAndBid.parse(it, jokers = true) }.winnings()

    check(part1(readInput("Day07_test")) == 6440)
    check(part2(readInput("Day07_test")) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
