@Suppress("unused")
fun drawMap(points: Collection<Point>, markings: Map<Point, Char> = emptyMap()): String {
    if (points.isEmpty()) return "<empty>"
    val pts = points + markings.keys
    val minX = pts.minOf { it.x }
    val maxX = pts.maxOf { it.x }
    val minY = pts.minOf { it.y }
    val maxY = pts.maxOf { it.y }

    return buildString {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val p = Point(x, y)
                append(markings[p] ?: if (p in points) '#' else '.')
            }
            append('\n')
        }
    }
}
