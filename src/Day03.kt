private data class Schematic(val lines: List<String>) {

    operator fun get(p: Point) =
        lines.getOrNull(p.y)?.getOrNull(p.x) ?: '.'

    fun containsSymbol(coordinates: Iterable<Point>) =
        coordinates.any { get(it).isSymbol() }

    fun adjacentNumbers(p: Point) =
        numbers.filter { p in it.adjacentCoordinates }

    val numbers = buildList {
        for ((y, line) in lines.withIndex()) {
            var x = 0
            while (x < line.length) {
                val digits = line.substring(x).takeWhile { it.isDigit() }
                if (digits.isNotEmpty())
                    add(Number(digits, x, y))

                x += digits.length + 1
            }
        }
    }

    val coordinates: List<Point>
        get() = Point.inRange(xRange = lines[0].indices, yRange = lines.indices)

    companion object {
        private fun Char.isSymbol() = this != '.' && !isDigit()
    }
}

private class Number(digits: String, x: Int, y: Int) {

    val value = digits.toInt()

    val adjacentCoordinates = buildList {
        val range = (x - 1)..<(x + digits.length + 1)
        add(Point(range.first, y))
        add(Point(range.last, y))
        range.mapTo(this) { x -> Point(x, y - 1) }
        range.mapTo(this) { x -> Point(x, y + 1) }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val schematic = Schematic(input)
        return schematic.numbers
            .filter { schematic.containsSymbol(it.adjacentCoordinates) }
            .sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val schematic = Schematic(input)
        return schematic.coordinates
            .filter { schematic[it] == '*' }
            .map { schematic.adjacentNumbers(it) }
            .filter { it.size == 2 }
            .sumOf { (a, b) -> a.value * b.value }
    }

    check(part1(readInput("Day03_test")) == 4361)
    check(part2(readInput("Day03_test")) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
