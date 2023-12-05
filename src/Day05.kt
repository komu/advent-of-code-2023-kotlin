private typealias Range = ClosedRange<Long>

private val Range.length: Long
    get() = endInclusive - start + 1

/** If ranges overlap, returns the overlapping part, otherwise `null` */
private fun Range.overlap(other: Range): Range? =
    if (start <= other.endInclusive && other.start <= endInclusive)
        maxOf(start, other.start)..minOf(endInclusive, other.endInclusive)
    else
        null

private fun rangeWithStartAndLength(start: Long, length: Long) =
    start..<start + length

/** Merge adjacent and overlapping ranges to minimize the number of total ranges */
private fun Collection<Range>.mergeRanges(): List<Range> {
    if (isEmpty()) return emptyList()

    val sortedRanges = this.sortedBy { it.start }

    val result = mutableListOf(sortedRanges.first())
    for (nextRange in sortedRanges.drop(1)) {
        val currentRange = result.last()
        if (currentRange.endInclusive + 1 >= nextRange.start)
            result[result.lastIndex] = currentRange.start..maxOf(currentRange.endInclusive, nextRange.endInclusive)
        else
            result += nextRange
    }

    return result
}

/** Defines a mapping from a range to another */
private class RangeMapping(private val destination: Long, private val source: Range) {

    /** Return a [Move] describing this mapping or `null` if the mapping is not applicable */
    operator fun invoke(range: ClosedRange<Long>): Move? {
        val overlap = source.overlap(range)
            ?: return null

        val offset = overlap.start - source.start
        return Move(
            source = source,
            destination = rangeWithStartAndLength(destination + offset, overlap.length)
        )
    }

    class Move(val source: Range, val destination: Range)
}

/** A collection of mappings for various sets */
private data class MappingSet(private val rangeMappings: List<RangeMapping>) {

    /** Apply the mappings in this set to all ranges */
    operator fun invoke(ranges: List<Range>): List<Range> =
        ranges.flatMap { apply(it) }.mergeRanges()

    private fun apply(range: Range): List<Range> {
        val moves = rangeMappings.mapNotNull { mapping -> mapping(range) }
        val movedRegions = moves.map { it.source }
        val moved = moves.map { it.destination }
        val nonMoved = range.nonOverlappingSubRanges(movedRegions)

        return moved + nonMoved
    }
}

/**
 * Returns sub-ranges of `this` range that don't overlap with any of the ranges in [ranges].
 */
private fun Range.nonOverlappingSubRanges(ranges: Collection<Range>): List<Range> {
    val result = mutableListOf<Range>()

    var current = this
    for (range in ranges.mergeRanges()) {
        if (current.start < range.start) {
            result += current.start..<range.start

            current = range.endInclusive + 1..current.endInclusive
            if (current.isEmpty())
                return result
        }
    }

    if (ranges.isEmpty())
        result += current

    return result
}

private fun parse(input: List<String>): Pair<List<Long>, List<MappingSet>> {
    fun parseRangeMapping(s: String): RangeMapping {
        val (dst, src, len) = s.split(" ")
        return RangeMapping(dst.toLong(), rangeWithStartAndLength(src.toLong(), len.toLong()))
    }

    fun parseMappingSet(s: String) =
        MappingSet(s.split("\n").drop(1).map { parseRangeMapping(it) })

    val seeds = input.first().removePrefix("seeds: ").split(" ").map { it.toLong() }
    val mappings = input.drop(2).joinToString("\n").split("\n\n").map { parseMappingSet(it) }

    return Pair(seeds, mappings)
}

private fun List<MappingSet>.lowest(seedRanges: List<Range>) =
    fold(seedRanges) { ranges, mapping -> mapping(ranges) }.minOf { it.start }

fun main() {
    fun part1(input: List<String>): Long {
        val (seeds, mappings) = parse(input)
        val seedRanges = seeds.map { rangeWithStartAndLength(it, 1) }
        return mappings.lowest(seedRanges)
    }

    fun part2(input: List<String>): Long {
        val (seeds, mappings) = parse(input)
        val seedRanges = seeds.chunked(2).map { (start, length) -> rangeWithStartAndLength(start, length) }
        return mappings.lowest(seedRanges)
    }

    check(part1(readInput("Day05_test")) == 35L)
    check(part2(readInput("Day05_test")) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
