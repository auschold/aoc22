package d06

import java.io.File
import java.util.Stack
import java.util.TreeMap

fun main() {
    val PACKET_MARKER_SIZE = 4
    val MESSAGE_MARKER_SIZE = 14

    val file = File("src/main/resources/d06/input.txt")
    val message = file.readText()

    val positionOfStartOfPackage = findMarkerPosition(message, PACKET_MARKER_SIZE)
    val positionOfStartOfMessage = findMarkerPosition(message, MESSAGE_MARKER_SIZE)

    println(positionOfStartOfPackage)
    println(positionOfStartOfMessage)
}

private fun findMarkerPosition(input: String, markerSize: Int) =
    input.windowed(size = markerSize, step = 1, partialWindows = false)
        .indexOfFirst { isStartOfMarker(it, markerSize) }
        .plus(markerSize)

private fun isStartOfMarker(symbols: String, markerSize: Int): Boolean = symbols.toSet().size == markerSize