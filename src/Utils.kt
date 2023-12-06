import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

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

inline fun <T> Iterable<T>.productOf(block: (T) -> Int ): Int =
    fold(1) { a, b -> a * block(b) }

fun <T> assertEquals(a: T, b: T) {
    check(a == b) { "$a != $b" }
}
