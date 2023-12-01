/** Returns a list of all possible tail substrings of a given string. */
private fun String.tails() = indices.mapNotNull { subSequence(it, length) }

/** Mapping from "0" -> 0, "1" -> 1 etc. */
private val numericDigits = (0..9).associateBy { it.toString() }

/** Mapping from "one" -> 1, "two" -> 2 etc. */
private val writtenDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    .withIndex().associate { (i, d) -> d to (i + 1) }

/** Using given digit mappings, return digit matching start of string, or `null` */
private fun CharSequence.startingDigit(digits: Map<String, Int>): Int? =
    digits.entries.firstNotNullOfOrNull { (prefix, digit) -> digit.takeIf { startsWith(prefix) } }

/** Calculates the calibration value for a line, using given digit mappings */
private fun calibrationValue(line: String, digits: Map<String, Int>): Int {
    val lineDigits = line.tails().mapNotNull { it.startingDigit(digits) }
    return lineDigits.first() * 10 + lineDigits.last()
}

/** Sum of all calibration values */
private fun calibrationValueSum(input: List<String>, digits: Map<String, Int>) =
    input.sumOf { calibrationValue(it, digits) }

fun main() {
    fun part1(input: List<String>) = calibrationValueSum(input, digits = numericDigits)
    fun part2(input: List<String>) = calibrationValueSum(input, digits = numericDigits + writtenDigits)

    check(part1(readInput("Day01_test")) == 142)
    check(part2(readInput("Day01_test2")) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
