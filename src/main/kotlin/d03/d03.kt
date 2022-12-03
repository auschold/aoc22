package d03

import java.io.File

fun main() {
    val file = File("src/main/resources/d03/input.txt")

    val sumOfPriorities = file.readLines()
        .map { parseRucksackFromInput(it) }
        .map { it.commonItem() }
        .sumOf { it.priority() }

    println(sumOfPriorities)


    val sumOfBadgePriorities = file.readLines()
        .map { parseRucksackFromInput(it) }
        .windowed(size = 3, step = 3)
        .map { commonItemInAllRucksacks(it) }
        .sumOf { it.priority() }

    println(sumOfBadgePriorities)
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

private fun commonItemInAllRucksacks(rucksacks: Iterable<Rucksack>) =
    rucksacks
        .map { it.firstCompartment.plus(it.secondCompartment).toSet() }
        .reduce { acc, items -> acc.intersect(items) }
        .single()

private fun parseRucksackFromInput(line: String): Rucksack {
    val firstHalf = line.substring(0, line.length / 2)
    val secondHalf = line.substring(line.length / 2, line.length)

    return Rucksack(
        firstHalf.map { Item(it) }.toSet(),
        secondHalf.map { Item(it) }.toSet()
    )
}
