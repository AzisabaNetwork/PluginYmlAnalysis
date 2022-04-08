@file:JvmName("Main")
package net.azisaba.pluginymlanalysis

import net.azisaba.pluginymlanalysis.util.FileUtil
import net.azisaba.pluginymlanalysis.util.PluginDescriptionUtil
import java.io.File

fun main() {
    val files = FileUtil.collectJarFiles(File(".").toPath())
    val descriptions = PluginDescriptionFile.readFiles(files)
    ArrayList(descriptions).forEach {
        println("Plugin '${it.name}':")
        println("  Version: ${it.version}")
        println("  Main class: ${it.main}")
        if (it.description != null) println("  Description: ${it.description.trim()}")
        if (it.authors != null) println("  Authors: ${it.authors.joinToString(", ")}")
        if (it.website != null) println("  Website: ${it.website}")
        if (it.prefix != null) println("  Prefix: ${it.prefix}")
        if (it.database != null) println("  Database: ${it.database}")
        if (it.load != null) println("  Load: ${it.load}")
        if (it.depend != null) {
            println("  Dependencies:")
            it.depend.forEach { s ->
                println("    - $s")
            }
        }
        if (it.softdepend != null) {
            println("  Soft dependencies:")
            it.softdepend.forEach { s ->
                println("    - $s")
            }
        }
        if (it.loadbefore != null) println("  Load before: ${it.loadbefore}")
        if (it.apiVersion != null) println("  API version: ${it.apiVersion}")
        if (it.libraries != null) {
            println("  Libraries:")
            it.libraries.forEach { s ->
                println("    - $s")
            }
        }
        if (it.defaultPermission != null) println("  Default permission: ${it.defaultPermission}")
        val hardDependents = PluginDescriptionUtil.findDependents(descriptions, it, true)
        if (hardDependents.isNotEmpty()) {
            println("  Dependents (hard):")
            hardDependents.forEach { d ->
                println("    - ${d.name}")
            }
        }
        val softDependents = PluginDescriptionUtil.findDependents(descriptions, it, false)
        if (softDependents.isNotEmpty()) {
            println("  Dependents (soft):")
            softDependents.forEach { d ->
                println("    - ${d.name}")
            }
        }
        println()
    }
}
