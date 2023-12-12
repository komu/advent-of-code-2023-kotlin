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
            when {
                group < groupLengths.size -> {
                    val length = groupLengths[group]

                    (offset..pattern.length - length).sumOf { i ->
                        val segment = i..<(i + length)
                        val gapBefore = pattern.subSequence(offset, segment.first)
                        val charAfter = pattern.getOrNull(segment.last + 1)

                        if ('#' !in gapBefore && charAfter != '#' && segment.all { pattern[it] != '.' })
                            recurse(group + 1, segment.last + 2)
                        else
                            0
                    }
                }

                '#' !in pattern.drop(offset) -> 1
                else -> 0
            }
        }

        return recurse(0, 0)
    }

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
