private fun parseNetwork(input: List<String>): Pair<String, Map<String, Pair<String, String>>> {
    val regex = Regex("""(\w+) = \((\w+), (\w+)\)""")

    val transitions = input.drop(2).associate { s ->
        val (id, l, r) = regex.matchEntire(s)!!.destructured
        id to Pair(l, r)
    }

    return Pair(input.first(), transitions)
}

private fun period(start: String, instructions: String, transitions: Map<String, Pair<String, String>>): Int {
    val seen = mutableSetOf<Pair<String, Int>>()
    var node = start

    for ((index, instruction) in instructions.asSequence().repeated().withIndex()) {
        if (!seen.add(Pair(node, index % instructions.length))) {
            val prefixLength = index % instructions.length
            return index - prefixLength
        }

        val (left, right) = transitions[node]!!
        node = if (instruction == 'L') left else right
    }

    error("no period")
}

fun main() {

    fun part1(input: List<String>): Int {
        val (instructions, transitions) = parseNetwork(input)

        var node = "AAA"
        for ((index, instruction) in instructions.asSequence().repeated().withIndex()) {
            if (node == "ZZZ")
                return index

            val (left, right) = transitions[node]!!
            node = if (instruction == 'L') left else right
        }

        error("no end")
    }

    fun part2(input: List<String>): Long {
        val (instructions, transitions) = parseNetwork(input)

        val startNodes = transitions.keys.filter { it.endsWith("A") }

        return lcd(startNodes.map { period(it, instructions, transitions).toLong() })
    }

    check(part1(readInput("Day08_test1")) == 2)
    check(part1(readInput("Day08_test2")) == 6)
    check(part2(readInput("Day08_test3")) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
