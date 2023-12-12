private data class ConditionRecord(
    val pattern: String,
    val groupLengths: List<Int>
) {

    fun unfold() = ConditionRecord(
        pattern = (1..5).joinToString("?") { pattern },
        groupLengths = (1..5).flatMap { groupLengths }
    )

    fun arrangements(): Long {
        val cache = mutableMapOf<Pair<Int, Int>, Long>()

        fun recurse(group: Int, offset: Int): Long = cache.getOrPut(Pair(group, offset)) {
            if (group == groupLengths.size) {
                if (offset > pattern.lastIndex || '#' !in pattern.subSequence(offset, pattern.length)) 1 else 0
            } else {
                (offset..pattern.length).sumOf { i ->
                    val segment = i..<(i + groupLengths[group])

                    if (isValidNextSegment(segment, previous = offset))
                        recurse(group + 1, segment.last + 2)
                    else
                        0
                }
            }
        }

        return recurse(0, 0)
    }

    private fun isValidNextSegment(segment: IntRange, previous: Int) =
        segment.first in pattern.indices &&
                segment.last in pattern.indices &&
                pattern.getOrNull(segment.last + 1) != '#' &&
                '#' !in pattern.subSequence(previous..<segment.first) &&
                segment.all { pattern[it] != '.' }

    companion object {
        fun parse(s: String): ConditionRecord {
            val (data, groups) = s.split(" ")
            return ConditionRecord(data, groups.split(",").map { it.toInt() })
        }
    }
}

fun main() {
    fun part1(input: List<String>) =
        input.sumOf { ConditionRecord.parse(it).arrangements() }

    fun part2(input: List<String>) =
        input.sumOf { ConditionRecord.parse(it).unfold().arrangements() }

    assertEquals(part1(readInput("Day12_test")), 21)
    assertEquals(part2(readInput("Day12_test")), 525152)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
