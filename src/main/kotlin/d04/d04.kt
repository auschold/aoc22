package d04

import java.io.File

fun main() {
    val file = File("src/main/resources/d04/input.txt")

    val numberOfRedundantAssignments = file.readLines()
        .map { parseAssignedSections(it) }
        .count { isFullyRedundantSectionAssignment(it) }

    println(numberOfRedundantAssignments)

    val numberOfOverlappingAssignments = file.readLines()
        .map { parseAssignedSections(it) }
        .count { isOverlappingSectionAssignment(it) }

    println(numberOfOverlappingAssignments)
}

private fun isFullyRedundantSectionAssignment(assignment: Pair<IntRange, IntRange>) =
    assignment.first.contains(assignment.second) || assignment.second.contains(assignment.first)

private fun isOverlappingSectionAssignment(assignment: Pair<IntRange, IntRange>) =
    assignment.first.overlaps(assignment.second)

private fun parseAssignedSections(input: String): Pair<IntRange, IntRange> {
    val (sectionsFirstElf, sectionsSecondElf) = input.split(",")
    return Pair(parseSectionRange(sectionsFirstElf), parseSectionRange(sectionsSecondElf))
}

private fun parseSectionRange(input: String): IntRange {
    val (start, end) = input.split("-")
    return IntRange(start.toInt(), end.toInt())
}

private fun IntRange.contains(other: IntRange) =
    this.contains(other.first) && this.contains(other.last)

private fun IntRange.overlaps(other: IntRange) =
    this.contains(other.first) || this.contains(other.last) ||
            other.contains(this.first) || other.contains(this.last)
