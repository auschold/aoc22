package d07

import java.io.File
import java.util.Stack
import java.util.TreeMap
import kotlin.math.max

fun main() {
    val file = File("src/main/resources/d07/input.txt")

    val rootFolder = parseTerminalOutput(file.readLines())
    val folderSmaller100000 = folderSmallerThan(rootFolder, 100000L)
    println(folderSmaller100000.sumOf { totalSizeOf(it) })
}

private fun folderSmallerThan(root: FolderEntry, maxSize: Long): List<FolderEntry> {
    val result = ArrayList<FolderEntry>()

    root.accept { folder ->
        val folderSize = totalSizeOf(folder)
        if (folderSize < maxSize) {
            result.add(folder)
        }
    }

    return result
}

private fun totalSizeOf(root: FolderEntry): Long {
    var result = 0L
    root.accept { folder ->
        result += folder.files().sumOf { it.size }
    }
    return result
}

private fun parseTerminalOutput(lines: List<String>): FolderEntry {
    val state = State()

    lines.forEach {
        if (it.startsWith("$")) {
            parseCommand(it, state)
        } else {
            parseData(it, state)
        }
    }


    return state.rootFolder
}

private fun parseCommand(line: String, state: State) {
    val commandAndArgs = line.removePrefix("$ ").split(" ")

    when {
        commandAndArgs.first() == "ls" -> return
        commandAndArgs.first() == "cd" -> state.currentFolder = resolvePath(commandAndArgs.last(), state.currentFolder)
    }
}

private fun parseData(line: String, state: State) {
    val data = line.split(" ")
    when {
        data.first() == "dir" -> state.currentFolder.addFolder(data.last())
        else -> state.currentFolder.addFile(FileEntry(data.last(), data.first().toLong()))
    }
}

private fun resolvePath(path: String, currentFolder: FolderEntry): FolderEntry {
    return when (path) {
        "/" -> currentFolder.let {
            var current: FolderEntry = it
            while (current.parentFolder != null) current = current.parentFolder!!
            return current
        }
        ".." -> currentFolder.parentFolder!!
        else -> currentFolder.nestedFolder(path)!!
    }
}

private class State {
    val rootFolder: FolderEntry = FolderEntry(null, null)
    var currentFolder = rootFolder
}

private class FileEntry(val name: String, val size: Long)

private class FolderEntry(val name: String?, val parentFolder: FolderEntry?) {
    private val nestedFolder = ArrayList<FolderEntry>()
    private val files = ArrayList<FileEntry>()

    fun nestedFolder(name: String): FolderEntry? = nestedFolder.firstOrNull { it.name == name }

    fun addFolder(name: String): FolderEntry {
        nestedFolder(name)?.let { return it }

        val newFolder = FolderEntry(name, this)
        nestedFolder.add(newFolder)
        return newFolder
    }

    fun addFile(file: FileEntry) {
        files.add(file)
    }

    fun files(): List<FileEntry> = files
    fun nestedFolder(): List<FolderEntry> = nestedFolder

    fun accept( visitor: (folder: FolderEntry) -> Unit ) {
        visitor.invoke(this)
        nestedFolder.forEach { it.accept(visitor) }
    }
}