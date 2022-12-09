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
    private val ropeSectionCoords = ArrayList<Coord>()
    private val tailObservedAt = HashSet<Coord>()

    init {
        for (i in 0..9) {
            ropeSectionCoords.add(Coord(0, 0))
        }
        tailObservedAt.add(tailCoord())
    }

    fun moveHead(instruction: String) {
        val (direction, steps) = instruction.split(" ")

        for (i in 1 .. steps.toInt()) {
            moveHeadSingleStep(direction)
            moveTailsTowardsAncestor()
        }
    }

    private fun moveHeadSingleStep(direction: String) {
        val head = headCoord()
        val newHead = when (direction) {
            "R" -> Coord(head.x + 1, head.y)
            "L" -> Coord(head.x - 1, head.y)
            "U" -> Coord(head.x, head.y + 1)
            "D" -> Coord(head.x, head.y - 1)
            else -> error("Unknown direction $direction")
        }
        ropeSectionCoords[0] = newHead
    }

    private fun moveTailsTowardsAncestor() {
        for (i in 1..9) moveTailsTowardsAncestor(i)
        tailObservedAt.add(tailCoord())
    }

    private fun moveTailsTowardsAncestor(index: Int) {
        val ancestor = ropeSectionCoords[index - 1]
        val node = ropeSectionCoords[index]
        if (node.isAdjacentTo(ancestor)) return

        val distanceX = ancestor.x - node.x
        val distanceY = ancestor.y - node.y

        val newCoord = Coord(node.x + distanceX.sign, node.y + distanceY.sign)
        ropeSectionCoords[index] = newCoord
    }

    private fun headCoord() = ropeSectionCoords.first()
    private fun tailCoord() = ropeSectionCoords.last()

    fun tailObservedAt(): Set<Coord> = tailObservedAt
}

data class Coord(val x: Int, val y: Int) {
    fun isAdjacentTo(other: Coord) =
        abs(this.x - other.x) <= 1 && abs(this.y - other.y) <= 1
}