package d10

import java.io.File
import java.util.LinkedList

fun main() {
    val file = File("src/main/resources/d10/input.txt")
    val instructions = file.readLines()
        .map { parseInstruction(it) }

    val relevantSignalStrengths = captureSignalStrengthDuringProgramExecution(instructions)
    println(relevantSignalStrengths.sum())
}

private fun captureSignalStrengthDuringProgramExecution(operations: List<Operation>): List<Int> {
    val state = CpuState()
    val iterator = operations.iterator()

    val result = LinkedList<Int>()

    var cycleNo = 0
    var currentInstruction: Operation? = iterator.next()

    while (currentInstruction != null || iterator.hasNext()) {
        if (currentInstruction == null) {
            currentInstruction = iterator.next()
        }

        cycleNo++

        if ((cycleNo - 20) % 40 == 0) {
            result.add(state.registerValue() * cycleNo)
        }

        currentInstruction.tick(state)

        if (currentInstruction.completed()) {
            currentInstruction = null
        }
    }

    return result
}

private fun parseInstruction(line: String) =
    when (line) {
        "noop" -> Noop()
        else -> AddOperation(line.split(" ")[1].toInt())
    }

private class CpuState {
    private var registerValue: Int = 1

    fun accumulate(value: Int) {
        registerValue += value
    }

    fun registerValue(): Int = registerValue

    override fun toString(): String = "CpuState [register=$registerValue]"
}

private interface Operation {
    fun tick(state: CpuState)
    fun completed(): Boolean
}

private class Noop: Operation {
    override fun tick(cpuState: CpuState) { /* noop*/ }
    override fun completed() = true
    override fun toString(): String = "noop"
}

private class AddOperation(val value: Int): Operation {
    private var ticksRemaining = 2

    override fun tick(state: CpuState) {
        ticksRemaining--
        if (ticksRemaining == 0) {
            state.accumulate(value)
        }
    }

    override fun completed(): Boolean = ticksRemaining == 0

    override fun toString(): String = "Add [v=$value;r=$ticksRemaining]"
}