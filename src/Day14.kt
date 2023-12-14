private class Platform private constructor(private val buf: CharArray, private val width: Int) {

    private val height = buf.size / width

    constructor(grid: List<String>) : this(
        buf = grid.joinToString("").toCharArray(),
        width = grid[0].length
    )

    override fun equals(other: Any?): Boolean =
        other is Platform && buf.contentEquals(other.buf) && width == other.width

    override fun hashCode() = buf.contentHashCode()

    operator fun get(p: Point) = buf[p.y * width + p.x]

    operator fun set(p: Point, c: Char) {
        buf[p.y * width + p.x] = c
    }

    operator fun contains(p: Point) = p.y in 0..<height && p.x in 0..<width

    fun tilt(v: Vector) {
        val xs = when {
            v.dx < 0 -> 1..<width
            v.dx > 0 -> width - 2 downTo 0
            else -> 0..<width
        }
        val ys = when {
            v.dy < 0 -> 1..<height
            v.dy > 0 -> height - 2 downTo 0
            else -> 0..<height
        }

        for (y in ys) {
            for (x in xs) {
                val p = Point(x, y)
                if (this[p] == 'O') {
                    var pp = p + v

                    while (pp in this && this[pp] == '.')
                        pp += v

                    this[p] = '.'
                    this[pp - v] = 'O'
                }
            }
        }
    }

    fun cycle(): Platform {
        val copy = Platform(buf.clone(), width)
        copy.tilt(Vector.UP)
        copy.tilt(Vector.LEFT)
        copy.tilt(Vector.DOWN)
        copy.tilt(Vector.RIGHT)
        return copy
    }

    fun load() =
        (0..<height).sumOf { y ->
            val count = (0..<width).count { x ->
                buf[y * width + x] == 'O'
            }
            count * (height - y)
        }
}

fun main() {
    fun part1(input: List<String>): Int {
        val platform = Platform(input)
        platform.tilt(Vector(dx = 0, dy = -1))
        return platform.load()
    }

    fun part2(input: List<String>): Int {
        val platform = Platform(input)

        val seenIndices = mutableMapOf<Platform, Int>()
        val seen = mutableListOf<Platform>()
        var current = platform
        repeat(1_000_000_000) { i ->
            val prefix = seenIndices[current]
            if (prefix != null) {
                val period = i - prefix
                val index = ((1_000_000_000 - prefix) % period) + prefix
                return seen[index].load()
            } else {
                seen.add(current)
                seenIndices[current] = i
            }

            current = current.cycle()
        }

        error("could not find period")
    }

    check(part1(readInput("Day14_test")) == 136)
    check(part2(readInput("Day14_test")) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
