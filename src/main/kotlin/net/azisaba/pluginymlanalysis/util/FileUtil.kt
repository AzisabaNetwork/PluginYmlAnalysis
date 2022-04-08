package net.azisaba.pluginymlanalysis.util

import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipFile

object FileUtil {
    fun collectJarFiles(path: Path): List<Path> {
        if (!Files.exists(path)) return emptyList()
        val list = mutableListOf<Path>()

        if (Files.isDirectory(path)) {
            Files.list(path).forEach {
                if (Files.isDirectory(it)) {
                    list.addAll(collectJarFiles(it))
                } else {
                    if (it.toString().endsWith(".jar")) {
                        list.add(it)
                    }
                }
            }
        } else {
            if (path.toString().endsWith(".jar")) {
                list.add(path)
            }
        }

        return list
    }

    // get contents of plugin.yml in jar file and return as string
    fun getContents(name: String, path: Path): String? {
        if (!Files.exists(path) || Files.isDirectory(path)) {
            return null
        }

        ZipFile(path.toFile()).use { jar ->
            val entry = jar.getEntry(name) ?: return null

            jar.getInputStream(entry).use { input -> input.bufferedReader().use { reader -> return reader.readText() } }
        }
    }
}
