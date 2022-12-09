package d09

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

fun main() {
    val file = File("src/main/resources/d09/input.txt")

    val grid = TrackingQuantumGrid()
    file.readLines()
        .forEach { grid.moveHead(it) }

    println(grid.tailObservedAt().size)
}

class TrackingQuantumGrid {
    private var headCoord = Coord(0,0)
    private var tailCoord = Coord(0, 0)
    private val tailObservedAt = HashSet<Coord>()

    init {
        tailObservedAt.add(tailCoord)
    }

    fun moveHead(instruction: String) {
        val (direction, steps) = instruction.split(" ")

        for (i in 1 .. steps.toInt()) {
            moveHeadSingleStep(direction)
            moveTailTowardsHead()
        }
    }

    private fun moveHeadSingleStep(direction: String) {
        headCoord = when (direction) {
            "R" -> Coord(headCoord.x + 1, headCoord.y)
            "L" -> Coord(headCoord.x - 1, headCoord.y)
            "U" -> Coord(headCoord.x, headCoord.y + 1)
            "D" -> Coord(headCoord.x, headCoord.y - 1)
            else -> error("Unknown direction $direction")
        }
    }

    private fun moveTailTowardsHead() {
        if (tailCoord.isAdjacentTo(headCoord)) return

        val distanceX = headCoord.x - tailCoord.x
        val distanceY = headCoord.y - tailCoord.y

        tailCoord = Coord(tailCoord.x + distanceX.sign, tailCoord.y + distanceY.sign)
        tailObservedAt.add(tailCoord)
    }

    fun tailObservedAt(): Set<Coord> = tailObservedAt


    override fun toString(): String {
        val sb = StringBuilder()

        val size = max(max(headCoord.x, headCoord.y), max(tailCoord.x, tailCoord.y)) + 1

        for (x in 0 .. size) {
            for (y in 0 .. size) {
                val coord = Coord(x, y)
                sb.append(when(coord) {
                    headCoord -> "H"
                    tailCoord -> "T"
                    else -> "."
                })
            }
            sb.append("\n")
        }

        return sb.toString()
    }
}

data class Coord(val x: Int, val y: Int) {
    fun isAdjacentTo(other: Coord) =
        abs(this.x - other.x) <= 1 && abs(this.y - other.y) <= 1
}