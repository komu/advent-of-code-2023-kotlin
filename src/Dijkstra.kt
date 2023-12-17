package utils

import java.util.*

fun <T> shortestPathWithCost(from: T, isTarget: (T) -> Boolean, edges: (T) -> List<Pair<T, Int>>): Pair<List<T>, Int>? {
    val initial = PathNode(from, null, 0)
    val nodes = mutableMapOf(from to initial)
    val queue = PriorityQueue(setOf(initial))
    val targets = mutableSetOf<T>()

    while (queue.isNotEmpty()) {
        val u = queue.remove()

        for ((v, cost) in edges(u.point)) {
            if (isTarget(v))
                targets += v

            val newDistance = u.distance + cost
            val previousNode = nodes[v]
            if (previousNode == null || newDistance < previousNode.distance) {
                val newNode = PathNode(v, u, newDistance)
                nodes[v] = newNode
                queue += newNode
            }
        }
    }

    val targetNode = targets.map { nodes[it]!! }.minByOrNull { it.distance }
    if (targetNode != null) {
        val result = mutableListOf<T>()
        var node: PathNode<T>? = targetNode
        while (node?.previous != null) {
            result += node.point
            node = node.previous
        }
        return result.asReversed() to targetNode.distance
    }
    return null
}

private class PathNode<T>(val point: T, val previous: PathNode<T>?, val distance: Int) : Comparable<PathNode<T>> {
    override fun compareTo(other: PathNode<T>) = distance.compareTo(other.distance)
}

fun <T> shortestPathWithCost2(from: T, isTarget: (T) -> Boolean, edges: (T) -> List<Pair<T, Int>>): Pair<List<T>, Int>? {
    val initial = PathNode(from, null, 0)
    val nodes = mutableMapOf(from to initial)
    val queue = PriorityQueue(setOf(initial))
    val targets = mutableSetOf<T>()

    while (queue.isNotEmpty()) {
        val u = queue.remove()

        for ((v, cost) in edges(u.point)) {
            if (isTarget(v))
                targets += v

            val newDistance = u.distance + cost
            val previousNode = nodes[v]
            if (previousNode == null || newDistance < previousNode.distance) {
                val newNode = PathNode(v, u, newDistance)
                nodes[v] = newNode
                queue += newNode
            }
        }
    }

    val targetNode = targets.map { nodes[it]!! }.minByOrNull { it.distance }
    if (targetNode != null) {
        val result = mutableListOf<T>()
        var node: PathNode<T>? = targetNode
        while (node?.previous != null) {
            result += node.point
            node = node.previous
        }
        return result.asReversed() to targetNode.distance
    }
    return null
}
