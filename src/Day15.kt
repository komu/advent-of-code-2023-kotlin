fun main() {
    fun hash(s: String) =
        s.fold(0) { n, c -> ((n + c.code) * 17) % 256 }

    fun part1(input: String) =
        input.trim().split(",").sumOf { hash(it) }

    fun part2(input: String): Int {
        val boxes = Array(256) { mutableMapOf<String, Int>() }

        for (op in input.trim().split(",")) {
            if (op.endsWith("-")) {
                val label = op.removeSuffix("-")
                boxes[hash(label)].remove(label)
            } else {
                val (label, focalLength) = op.split("=")
                boxes[hash(label)][label] = focalLength.toInt()
            }
        }

        return boxes.withIndex().sumOf { (boxIndex, box) ->
            box.values.withIndex().sumOf { (slotIndex, focalLength) ->
                (boxIndex + 1) * (slotIndex + 1) * focalLength
            }
        }
    }

    check(hash("HASH") == 52)
    check(part1(readInputString("Day15_test")) == 1320)
    check(part2(readInputString("Day15_test")) == 145)

    val input = readInputString("Day15")
    part1(input).println()
    part2(input).println()
}
