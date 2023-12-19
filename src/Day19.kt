private typealias Candidate = Map<String, IntRange>
private typealias Part = Map<String, Int>

private sealed class Rule(val target: String) {

    abstract fun evaluate(part: Part): Boolean
    abstract fun split(candidate: Candidate): Pair<Candidate, Candidate?>

    class Transition(target: String) : Rule(target) {
        override fun evaluate(part: Part) = true
        override fun split(candidate: Candidate) = candidate to null
    }

    class Lt(val key: String, val value: Int, target: String) : Rule(target) {

        override fun evaluate(part: Part) = part[key]!! < value

        override fun split(candidate: Candidate): Pair<Candidate, Candidate> {
            val range = candidate[key]!!

            return Pair(
                candidate.withReplacedKey(key, range.first..<value),
                candidate.withReplacedKey(key, value..range.last)
            )
        }
    }

    class Gt(val key: String, val value: Int, target: String) : Rule(target) {
        override fun evaluate(part: Part) = part[key]!! > value

        override fun split(candidate: Candidate): Pair<Candidate, Candidate> {
            val range = candidate[key]!!

            return Pair(
                candidate.withReplacedKey(key, value + 1..range.last),
                candidate.withReplacedKey(key, range.first..value)
            )
        }
    }
}


private fun parseInput(input: String): Pair<Map<String, List<Rule>>, List<Map<String, Int>>> {
    val workflowPattern = Regex("""(.+)\{(.+)}""")
    val rulePattern = Regex("""(.+)([<>])(\d+):(.+)""")

    fun parseRule(s: String): Rule =
        rulePattern.matchEntire(s)?.destructured?.let { (key, op, value, target) ->
            when (op) {
                "<" -> Rule.Lt(key, value.toInt(), target)
                ">" -> Rule.Gt(key, value.toInt(), target)
                else -> error("invalid op $op")
            }

        } ?: Rule.Transition(s)

    val (workflows, parts) = input.split("\n\n")
    return Pair(
        workflows.lines()
            .map { workflowPattern.matchEntire(it)!!.destructured }
            .associate { (n, rules) -> n to (rules.split(",").map(::parseRule)) },

        parts.lines()
            .map { line ->
                line.trim('{', '}')
                    .split(",")
                    .associate { it.splitOnce("=") }
                    .mapValues { (_, v) -> v.toInt() }
            }
    )
}

fun main() {

    fun part1(input: String): Int {
        val (workflows, parts) = parseInput(input)

        fun recurse(current: List<Rule>, part: Part, index: Int): Boolean {
            val rule = current[index]

            return if (rule.evaluate(part))
                when (rule.target) {
                    "A" -> true
                    "R" -> false
                    else -> recurse(workflows[rule.target]!!, part, 0)
                }
            else
                recurse(current, part, index + 1)
        }

        val init = workflows["in"]!!
        return parts.filter { recurse(init, it, 0) }.sumOf { it.values.sum() }
    }

    fun part2(input: String): Long {
        val (workflows, _) = parseInput(input)

        fun recurse(current: List<Rule>, candidate: Candidate?, index: Int): Long {
            if (candidate == null) return 0L

            val rule = current[index]
            val (ok, nok) = rule.split(candidate)

            return when (rule.target) {
                "A" -> ok.values.productOf { it.length.toLong() } + recurse(current, nok, index + 1)
                "R" -> recurse(current, nok, index + 1)
                else -> recurse(workflows[rule.target]!!, ok, 0) + recurse(current, nok, index + 1)
            }
        }

        return recurse(workflows["in"]!!, listOf("x", "m", "a", "s").associateWith { 1..4000 }, 0)
    }

    assertEquals(part1(readInputString("Day19_test")), 19114)
    assertEquals(part2(readInputString("Day19_test")), 167409079868000)

    val input = readInputString("Day19")
    part1(input).println()
    part2(input).println()
}
