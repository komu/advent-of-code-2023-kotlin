import CardinalDirection.*

private data class Beam(val p: Point, val v: CardinalDirection)

fun main() {
    fun energized(input: List<String>, start: Beam): Int {
        val beams = mutableListOf(start)
        val energized = mutableSetOf<Point>()
        val states = mutableSetOf<Beam>()

        while (beams.isNotEmpty()) {
            var beam = beams.removeLast()

            while ((beam.p.y in input.indices && beam.p.x in input[0].indices) && states.add(beam)) {
                energized += beam.p

                val v = when (input[beam.p.y][beam.p.x]) {
                    '/' -> when (beam.v) {
                            N -> E
                            S -> W
                            W -> S
                            E -> N
                        }

                    '\\' -> when (beam.v) {
                            N -> W
                            S -> E
                            W -> N
                            E -> S
                        }

                    '|' -> if (beam.v.vector.dx != 0) {
                        beams.add(Beam(beam.p + S, S))
                        N
                    } else {
                        beam.v
                    }

                    '-' -> if (beam.v.vector.dy != 0) {
                        beams.add(Beam(beam.p + E, E))
                        W
                    } else {
                        beam.v
                    }
                    else ->
                        beam.v
                }

                beam = Beam(beam.p + v, v)
            }
        }

        return energized.size
    }

    fun part1(input: List<String>): Int =
        energized(input, Beam(Point(0, 0), E))

    fun part2(input: List<String>): Int {
        val startStates = mutableListOf<Beam>()
        input[0].indices.mapTo(startStates) { x -> Beam(Point(x, 0), S) }
        input[0].indices.mapTo(startStates) { x -> Beam(Point(x, input.lastIndex), N) }
        input.indices.mapTo(startStates) { y -> Beam(Point(0, y), E) }
        input.indices.mapTo(startStates) { y -> Beam(Point(input[0].lastIndex, y), W) }

        return startStates.maxOf { energized(input, it) }
    }

    assertEquals(part1(readInput("Day16_test")), 46)
    assertEquals(part2(readInput("Day16_test")), 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
