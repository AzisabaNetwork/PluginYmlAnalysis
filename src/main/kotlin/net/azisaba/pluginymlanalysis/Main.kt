@file:JvmName("Main")
package net.azisaba.pluginymlanalysis

import net.azisaba.pluginymlanalysis.util.FileUtil
import java.io.File

fun main() {
    val files = FileUtil.collectJarFiles(File(".").toPath())
    val descriptions = PluginDescriptionFile.readFiles(files)
    ArrayList(descriptions).forEach {
        println(it.toString(descriptions))
        println()
    }
}
