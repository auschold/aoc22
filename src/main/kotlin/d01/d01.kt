package d01

import java.io.File

fun main() {
    val file = File("src/main/resources/d01/input.txt")

    val descendingCaloriesPerElf = file.readText()
        .split("\n\n")
        .asSequence()
        .map { caloriesOfElf(it) }
        .toList()

    val mostCaloriesPerElf = descendingCaloriesPerElf.first()
    val sumOfMostThreeCalories = descendingCaloriesPerElf.take(3).sum()

    println(mostCaloriesPerElf)
    println(sumOfMostThreeCalories)
}

private fun caloriesOfElf(input: String) = input
    .split("\n")
    .sumOf { it.toInt() }