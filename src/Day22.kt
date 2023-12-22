private data class Brick(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int, val minZ: Int, val maxZ: Int) {

    fun down(): Brick = Brick(minX, maxX, minY, maxY, minZ - 1, maxZ - 1)

    fun supports(b: Brick) =
        minX <= b.maxX && maxX >= b.minX && minY <= b.maxY && maxY >= b.minY && minZ <= b.maxZ - 1 && maxZ >= b.minZ - 1

    fun isUnsupported(stack: List<Brick>, disintegrated: Set<Brick> = emptySet()) =
        minZ > 1 && stack.none { it.supports(this) && it !== this && it !in disintegrated }

    companion object {
        fun parse(s: String): Brick {
            val (x1, y1, z1, x2, y2, z2) = s.split(',', '~').map { it.toInt() }
            return Brick(minOf(x1, x2), maxOf(x1, x2), minOf(y1, y2), maxOf(y1, y2), minOf(z1, z2), maxOf(z1, z2))
        }
    }
}

private class BrickStack(input: List<String>) {

    val bricks = buildList<Brick> {
        for (brick in input.map { Brick.parse(it) }.sortedBy { it.minZ }) {
            var pos = brick
            while (pos.isUnsupported(this))
                pos = pos.down()
            add(pos)
        }

        sortBy { it.minZ }
    }

    fun canDisintegrate(disintegratedBrick: Brick): Boolean {
        val disintegrated = setOf(disintegratedBrick)
        return bricks.none { it.isUnsupported(bricks, disintegrated) }
    }

    fun countFalls(initial: Brick): Int {
        val work = mutableListOf(initial)
        val disintegrated = mutableSetOf(initial)

        while (work.isNotEmpty()) {
            val current = work.removeLast()

            for (brick in bricks) {
                // If brick is below other, it can't possibly fall
                if (brick.maxZ < current.minZ)
                    continue

                // Brick can't be directly supported by any remaining since the stack is sorted by Z
                if (brick.minZ > current.maxZ + 1)
                    break

                if (brick !in disintegrated && brick.isUnsupported(bricks, disintegrated)) {
                    disintegrated.add(brick)
                    work.add(brick)
                }
            }
        }

        return disintegrated.size - 1
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val stack = BrickStack(input)
        return stack.bricks.count { stack.canDisintegrate(it) }
    }

    fun part2(input: List<String>): Int {
        val stack = BrickStack(input)
        return stack.bricks.sumOf { stack.countFalls(it) }
    }

    assertEquals(part1(readInput("Day22_test")), 5)
    assertEquals(part2(readInput("Day22_test")), 7)

    val input = readInput("Day22")

    part1(input).println()
    part2(input).println()
}
