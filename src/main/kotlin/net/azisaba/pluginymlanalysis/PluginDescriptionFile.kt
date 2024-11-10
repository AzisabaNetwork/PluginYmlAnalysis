package net.azisaba.pluginymlanalysis

import net.azisaba.pluginymlanalysis.util.FileUtil
import net.azisaba.pluginymlanalysis.util.PluginDescriptionUtil
import net.azisaba.pluginymlanalysis.yaml.YamlConfiguration
import net.azisaba.pluginymlanalysis.yaml.YamlObject
import java.nio.file.Path

data class PluginDescriptionFile(
    val name: String,
    val main: String,
    val version: String,
    val description: String?,
    val authors: List<String>?,
    val website: String?,
    val prefix: String?,
    val database: Boolean?,
    val load: String?,
    val depend: List<String>?,
    val softdepend: List<String>?,
    val loadbefore: List<String>?,
    val apiVersion: String?,
    val libraries: List<String>?,
    val defaultPermission: String?,
) {
    companion object {
        fun read(obj: YamlObject) =
            PluginDescriptionFile(
                obj.getString("name"),
                obj.getString("main"),
                obj.getString("version"),
                obj.getString("description"),
                obj.getArray("authors")?.mapToString(),
                obj.getString("website"),
                obj.getString("prefix"),
                if (obj.rawData["database"] != null) obj.getBoolean("database", false) else null,
                obj.getString("load"),
                obj.getArray("depend")?.mapToString(),
                obj.getArray("softdepend")?.mapToString(),
                obj.getArray("loadbefore")?.mapToString(),
                obj.getString("apiVersion"),
                obj.getArray("libraries")?.mapToString(),
                obj.getString("defaultPermission"),
            )

        fun readFiles(files: List<Path>) = files.map { path ->
            val fileContents = FileUtil.getContents("plugin.yml", path) ?: return@map null
            return@map try {
                val obj = YamlConfiguration(YamlConfiguration.DEFAULT, fileContents).asObject()
                read(obj)
            } catch (e: Exception) {
                println("Failed to load $path:")
                e.printStackTrace()
                println("Raw contents for $path:")
                println(fileContents)
                null
            }
        }.filterNotNull()
    }

    override fun toString(): String {
        var sb = mutableListOf<String>()
        sb += "Plugin '$name':"
        "  Version: $version"
        "  Main class: $main"
        if (description != null) sb += "  Description: ${description.trim()}"
        if (authors != null) sb += "  Authors: ${authors.joinToString(", ")}"
        if (website != null) sb += "  Website: $website"
        if (prefix != null) sb += "  Prefix: $prefix"
        if (database != null) sb += "  Database: $database"
        if (load != null) sb += "  Load: $load"
        if (depend != null) {
            sb += "  Dependencies:"
            depend.forEach { s ->
                sb += "    - $s"
            }
        }
        if (softdepend != null) {
            sb += "  Soft dependencies:"
            softdepend.forEach { s ->
                sb += "    - $s"
            }
        }
        if (loadbefore != null) sb += "  Load before: $loadbefore"
        if (apiVersion != null) sb += "  API version: $apiVersion"
        if (libraries != null) {
            sb += "  Libraries:"
            libraries.forEach { s ->
                sb += "    - $s"
            }
        }
        if (defaultPermission != null) sb += "  Default permission: $defaultPermission"
        return sb.joinToString("\n")
    }

    fun toString(descriptions: List<PluginDescriptionFile>): String {
        var sb = toString() + "\n"
        val hardDependents = PluginDescriptionUtil.findDependents(descriptions, this, true)
        if (hardDependents.isNotEmpty()) {
            sb += "  Dependents (hard):\n"
            hardDependents.forEach { d ->
                sb += "    - ${d.name}\n"
            }
        }
        val softDependents = PluginDescriptionUtil.findDependents(descriptions, this, false)
        if (softDependents.isNotEmpty()) {
            sb += "  Dependents (soft):\n"
            softDependents.forEach { d ->
                sb += "    - ${d.name}\n"
            }
        }
        return sb.trim('\n')
    }
}
