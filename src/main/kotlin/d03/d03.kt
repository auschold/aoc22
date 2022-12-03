package d03

import java.io.File

fun main() {
    val file = File("src/main/resources/d03/input.txt")

    val sumOfPriorities = file.readLines()
        .map { parseRucksackFromInput(it) }
        .map { it.commonItem() }
        .sumOf { it.priority() }

    println(sumOfPriorities)
}

class Rucksack(val firstCompartment: Set<Item>, val secondCompartment: Set<Item>) {
    fun commonItem() = firstCompartment.intersect(secondCompartment).single()
}

data class Item(val id: Char) {
    fun priority() = when {
        id in 'a'..'z' -> id.minus('a') + 1
        id in 'A' .. 'Z' -> id.minus('A') + 27
        else -> error("Unexpected input '$id'")
    }
}

private fun parseRucksackFromInput(line: String): Rucksack {
    val firstHalf = line.substring(0, line.length / 2)
    val secondHalf = line.substring(line.length / 2, line.length)

    return Rucksack(
        firstHalf.map { Item(it) }.toSet(),
        secondHalf.map { Item(it) }.toSet()
    )
}
