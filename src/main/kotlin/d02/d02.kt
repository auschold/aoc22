package d02

import java.io.File

fun main() {
    val file = File("src/main/resources/d02/input.txt")

    val totalPoints = file.readLines()
        .map { parseRoundInput(it) }
        .sumOf { calculatePointsForSingleRound(it.first, it.second) }

    println(totalPoints)
}

private fun parseRoundInput(line: String): Pair<Shape, Shape> {
    val (opponentSymbol, ownSymbol) = line.split(" ")
    val opponentShape = when (opponentSymbol) {
        "A" -> Shape.ROCK
        "B" -> Shape.PAPER
        "C" -> Shape.SCISSORS
        else -> error("Unexpected input symbol $opponentSymbol")
    }

    val ownShape = when (ownSymbol) {
        "X" -> Shape.ROCK
        "Y" -> Shape.PAPER
        "Z" -> Shape.SCISSORS
        else -> error("Unexpected input symbol $opponentSymbol")
    }

    return Pair(opponentShape, ownShape)
}

private fun calculatePointsForSingleRound(opponentShape: Shape, ownShape: Shape) =
    ownShape.points + roundOutcome(opponentShape, ownShape).points

private fun roundOutcome(opponentShape: Shape, ownShape: Shape): Outcome {
    return when {
        opponentShape == ownShape -> Outcome.DRAW
        ownShape.beats(opponentShape) -> Outcome.WIN
        else -> Outcome.LOSS
    }
}

enum class Shape(val points: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

}

private fun Shape.beats(other: Shape): Boolean = when (this) {
        Shape.ROCK -> other == Shape.SCISSORS
        Shape.PAPER -> other == Shape.ROCK
        Shape.SCISSORS -> other == Shape.PAPER
    }

enum class Outcome(val points: Int) {
    WIN(6),
    DRAW(3),
    LOSS(0);

}