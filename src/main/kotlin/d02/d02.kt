package d02

import java.io.File

fun main() {
    val file = File("src/main/resources/d02/input.txt")

    val totalPoints = file.readLines()
        .map { parseRoundInput(it) }
        .map { it.first to shapeForRequiredOutcome(it.first, it.second) }
        .sumOf { calculatePointsForSingleRound(it.first, it.second) }

    println(totalPoints)
}

private fun parseRoundInput(line: String): Pair<Shape, Outcome> {
    val (opponentSymbol, outcomeSymbol) = line.split(" ")
    val opponentShape = when (opponentSymbol) {
        "A" -> Shape.ROCK
        "B" -> Shape.PAPER
        "C" -> Shape.SCISSORS
        else -> error("Unexpected input symbol $opponentSymbol")
    }

    val requiredOutcome = when (outcomeSymbol) {
        "X" -> Outcome.LOSE
        "Y" -> Outcome.DRAW
        "Z" -> Outcome.WIN
        else -> error("Unexpected input symbol $outcomeSymbol")
    }

    return Pair(opponentShape, requiredOutcome)
}

private fun calculatePointsForSingleRound(opponentShape: Shape, ownShape: Shape) =
    ownShape.points + roundOutcome(opponentShape, ownShape).points

private fun roundOutcome(opponentShape: Shape, ownShape: Shape): Outcome {
    return when {
        opponentShape == ownShape -> Outcome.DRAW
        ownShape.beats(opponentShape) -> Outcome.WIN
        else -> Outcome.LOSE
    }
}

private fun shapeForRequiredOutcome(opponentShape: Shape, requiredOutcome: Outcome): Shape =
    when (requiredOutcome) {
        Outcome.DRAW -> opponentShape
        Outcome.WIN -> Shape.values().single { it.beats(opponentShape) }
        Outcome.LOSE -> Shape.values().single { opponentShape.beats(it) }
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
    LOSE(0);

}