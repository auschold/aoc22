package d08

import java.io.File

fun main() {
    val file = File("src/main/resources/d08/input.txt")

    val topology = TreeTopology.from(file.readText())
    println(topology)

    var visibleTrees = 0
    for (row in 0..topology.numberOfRows()) {
        for (col in 0 .. topology.numberOfCols()) {
            if (topology.isTreeVisibleFromOutside(Coord(row, col))) visibleTrees++
        }
    }
    println(visibleTrees)
}

private class TreeTopology(trees : List<List<Int>>) {
    private val trees: List<List<Int>> = trees

    fun isTreeVisibleFromOutside(coord: Coord) =
        ViewDirection.values()
            .map { isTreeVisibleFrom(coord, it) }
            .any { it }

    fun isTreeVisibleFrom(coord: Coord, viewDirection: ViewDirection): Boolean {
        val targetHeight = height(coord)
        val anyTreeInLineOfSightAtLeastSameHeight = coordsInLineOfSight(coord, viewDirection)
            .map { height(it) }
            .any { it >= targetHeight }
        return !anyTreeInLineOfSightAtLeastSameHeight
    }

    fun coordsInLineOfSight(targetCoord: Coord, viewDirection: ViewDirection) =
        generateSequence(viewDirection.borderCoordinateFunction.invoke(targetCoord, this)) { last ->
            viewDirection.stepFunction.invoke(last, this)
        }.takeWhile { it != targetCoord }

    fun numberOfRows() = trees.size - 1
    fun numberOfCols() = trees.first().size - 1

    fun height(coord: Coord) = trees[coord.row][coord.col]

    override fun toString(): String =
        trees.map { row ->
            row.joinToString(",")
        }.joinToString("\n")

    companion object {
        fun from(input: String): TreeTopology {
            val data = input.split("\n")
                .map { line ->
                    line.chunked(1)
                        .map { it.toInt() }
                }.toList()

            return TreeTopology(data)
        }
    }
}

private enum class ViewDirection(
    val borderCoordinateFunction: (coord: Coord, topology: TreeTopology) -> Coord,
    val stepFunction: (coord: Coord, topology: TreeTopology) -> Coord?
) {
    NORTH(
        borderCoordinateFunction = { coord, _ -> Coord(0, coord.col) },
        stepFunction = { coord, topology -> if (coord.row + 1 <= topology.numberOfRows()) Coord(coord.row + 1, coord.col) else null }
    ),
    SOUTH(
        borderCoordinateFunction = { coord, topology -> Coord(topology.numberOfRows(), coord.col) },
        stepFunction = { coord, _ -> if (coord.row - 1 >= 0) Coord(coord.row - 1, coord.col) else null }
    ),
    WEST(
        borderCoordinateFunction = { coord, _ -> Coord(coord.row, 0) },
        stepFunction = { coord, topology -> if (coord.col + 1 <= topology.numberOfCols()) Coord(coord.row, coord.col + 1) else null }
    ),
    EAST(
        borderCoordinateFunction = { coord, topology -> Coord(coord.row, topology.numberOfCols()) },
        stepFunction = { coord, _ -> if (coord.col - 1 >= 0) Coord(coord.row, coord.col - 1) else null }
    ),
}

private data class Coord(val row: Int, val col: Int) {
    override fun toString(): String = "[$row,$col]"
}
