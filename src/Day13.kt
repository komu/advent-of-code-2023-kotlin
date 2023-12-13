private data class MirrorPattern(val rows: List<String>) {

    private fun rowDiff(i: Int, j: Int) = rows[0].indices.count { x -> rows[i][x] != rows[j][x] }
    private fun colDiff(i: Int, j: Int) = rows.indices.count { y -> rows[y][i] != rows[y][j] }

    fun findHorizontalReflection(disallowed: Int = 0, smudges: Int = 0) =
        findReflection(1..<rows.size, disallowed, smudges, ::rowDiff)

    fun findVerticalReflection(disallowed: Int = 0, smudges: Int = 0): Int =
        findReflection(1..<rows[0].length, disallowed, smudges, ::colDiff)

    private fun findReflection(range: IntRange, disallowed: Int, smudges: Int, diff: (Int, Int) -> Int): Int =
        range.firstOrNull { pos -> pos != disallowed && hasReflectionAt(pos, range, smudges, diff) } ?: 0

    private fun hasReflectionAt(pos: Int, range: IntRange, smudges: Int, diff: (Int, Int) -> Int): Boolean {
        var remainingSmudges = smudges

        var i = pos - 1
        var j = pos
        while (i >= 0 && j <= range.last) {
            remainingSmudges -= diff(i--, j++)
            if (remainingSmudges < 0)
                return false
        }

        return remainingSmudges == 0
    }
}

private fun parsePatterns(input: List<String>) =
    input.joinToString("\n").split("\n\n").map { MirrorPattern(it.lines()) }

fun main() {
    fun part1(input: List<String>) =
        parsePatterns(input).sumOf { p ->
            val v = p.findVerticalReflection()
            val h = p.findHorizontalReflection()
            100 * h + v
        }

    fun part2(input: List<String>) =
        parsePatterns(input).sumOf { p ->
            val oldV = p.findVerticalReflection()
            val oldH = p.findHorizontalReflection()
            val v = p.findVerticalReflection(disallowed = oldV, smudges = 1)
            val h = p.findHorizontalReflection(disallowed = oldH, smudges = 1)
            100 * h + v
        }

    check(part1(readInput("Day13_test")) == 405)
    check(part2(readInput("Day13_test")) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
