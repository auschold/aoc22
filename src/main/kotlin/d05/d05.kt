package d05

import java.io.File
import java.util.Stack
import java.util.TreeMap

fun main() {
    val file = File("src/main/resources/d05/input.txt")

    val cargoInpit = file.readLines()
        .takeWhile { it.trim().isNotEmpty() }

    val instructionInput = file.readLines()
        .mapNotNull { Instruction.parse(it) }

    val shipCargo = parseCargo(cargoInpit)

    instructionInput.forEach { shipCargo.execute(it) }

    println(shipCargo.topOfAllStacks())
}

private fun parseCargo(inputLines: List<String>): ShipCargo {
    val cargo = ShipCargo()

    inputLines.reversed()
        .subList(1, inputLines.size)
        .forEach { parseCargoLine(it, cargo) }

    return cargo
}

private fun parseCargoLine(inputLine: String, shipCargo: ShipCargo) {
    // 1   2   3   4   5   6   7   8   9
    //[C]     [Q] [R] [D] [Z]     [H] [Q]
    // -> 1 + (index-1) * 4 => char position

    for (i in 1..9) {
        val itemPosition = 1 + (i - 1) * 4
        val item = inputLine.getOrNull(itemPosition)
            .takeIf { it?.isLetter() ?: false }

        if (item != null) shipCargo.stack(i).push(item.toString())
    }
}

class ShipCargo {
    private val cargoStacks = TreeMap<Int, Stack<String>>()

    fun stack(stackNo: Int): Stack<String> =
        cargoStacks.getOrPut(stackNo) { Stack<String>() }

    fun execute(instruction: Instruction) {
        for (i in 1..instruction.amount) {
            val poppedItem = stack(instruction.sourceStack).pop()
            stack(instruction.targetStack).push(poppedItem)
        }
    }

    fun topOfAllStacks() =
        cargoStacks
            .map { it.value.peek() }
            .joinToString(separator = "")

    override fun toString(): String =
        cargoStacks.map { e -> e.key.toString() + " -> " + e.value.joinToString() }.joinToString(separator = "\n")
}


data class Instruction(
    val amount: Int,
    val sourceStack: Int,
    val targetStack: Int,
) {
    companion object {
        private val INSTRUCTION_REGEX = """move (?<amount>\d+) from (?<source>\d+) to (?<target>\d+)""".toRegex()

        fun parse(line: String): Instruction? =
            INSTRUCTION_REGEX.matchEntire(line)?.let {
                Instruction(
                    amount = it.groups["amount"]?.value?.toInt()!!,
                    sourceStack = it.groups["source"]?.value?.toInt()!!,
                    targetStack = it.groups["target"]?.value?.toInt()!!,
                )
            }
    }
}