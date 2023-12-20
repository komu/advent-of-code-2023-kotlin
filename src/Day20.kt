private sealed class Module(val name: String, val destinations: List<String>) {

    abstract fun receive(signal: Signal): List<Signal>
    open fun init(senders: List<Module>) {}

    protected fun emit(b: Boolean) = destinations.map { Signal(name, it, b) }

    class Broadcaster(name: String, destinations: List<String>) : Module(name, destinations) {
        override fun receive(signal: Signal) = emit(signal.value)
    }

    class FlipFlop(name: String, destinations: List<String>) : Module(name, destinations) {
        private var state = false

        override fun receive(signal: Signal): List<Signal> {
            if (signal.value)
                return emptyList()

            state = !state
            return emit(state)
        }
    }

    class Conjunction(name: String, destinations: List<String>) : Module(name, destinations) {

        private var states = mutableMapOf<String, Boolean>()

        override fun init(senders: List<Module>) {
            for (s in senders)
                states[s.name] = false
        }

        override fun receive(signal: Signal): List<Signal> {
            states[signal.sender] = signal.value
            return emit(!states.values.all { it })
        }
    }
}

private data class Signal(val sender: String, val target: String, val value: Boolean)

private data class ModuleGraph(val modules: List<Module>) {

    val modulesByName = modules.associateBy { it.name }

    init {
        for (module in modules)
            module.init(modules.filter { module.name in it.destinations })
    }

    fun loop(init: Signal, max: Int = Int.MAX_VALUE, pred: (Signal) -> Boolean): Long {
        val signals = ArrayDeque<Signal>()
        var presses = 0L

        repeat(max) {
            presses++
            signals += init

            while (signals.isNotEmpty()) {
                val signal = signals.removeFirst()
                if (!pred(signal))
                    return presses

                signals += modulesByName[signal.target]?.receive(signal).orEmpty()
            }
        }

        return presses
    }

    companion object {
        fun load(input: List<String>) = ModuleGraph(input.map {
            val (head, tail) = it.split(" -> ")
            val destinations = tail.split(", ")
            when (head.first()) {
                '%' -> Module.FlipFlop(head.drop(1), destinations)
                '&' -> Module.Conjunction(head.drop(1), destinations)
                else -> Module.Broadcaster(head, destinations)
            }
        })
    }
}

fun main() {

    fun part1(input: List<String>): Long {
        val graph = ModuleGraph.load(input)

        var high = 0L
        var low = 0L

        graph.loop(Signal("button", "broadcaster", false), max = 1000) { signal ->
            if (signal.value) high++ else low++
            true
        }

        return high * low
    }

    fun part2(input: List<String>): Long {
        val graph = ModuleGraph.load(input)

        // In the input data, graph splits into four independent sub-graphs immediately after
        // the broadcaster, and they join just before 'rx' (check out Day20.gv). Therefore, we
        // can just compute the period the periods separately and join them with lcd.
        val startNodes = graph.modulesByName["broadcaster"]!!.destinations
        val target = graph.modules.first { "rx" in it.destinations }.name

        return lcd(startNodes.map { start ->
            graph.loop(Signal("broadcaster", start, false)) { signal ->
                !signal.value || signal.target != target
            }
        })
    }

    assertEquals(part1(readInput("Day20_test1")), 32000000L)
    assertEquals(part1(readInput("Day20_test2")), 11687500L)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
