import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs
import kotlin.math.absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Iterable<Int>.product(): Int =
    fold(1) { a, b -> a * b }

inline fun <T> Iterable<T>.productOf(block: (T) -> Int): Int =
    fold(1) { a, b -> a * block(b) }

private val SPACE = Regex("\\s+")

fun String.words(): List<String> = split(SPACE)

fun <T> List<T>.choosePairs(): List<Pair<T, T>> =
    withIndex().flatMap { (index, a) -> subList(index + 1, size).map { b -> a to b } }

fun <T> assertEquals(a: T, b: T) {
    check(a == b) { "$a != $b" }
}

fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long =
    a / gcd(a, b) * b

fun lcd(numbers: List<Long>): Long =
    numbers.fold(1L) { lcm, number -> lcm(lcm, number) }

fun <T> Sequence<T>.repeated(): Sequence<T> = sequence {
    while (true) {
        yieldAll(this@repeated)
    }
}

fun String.toIntList() = words().map { it.toInt() }

fun IntRange.normalize() =
    minOf(start, endInclusive)..maxOf(start, endInclusive)
