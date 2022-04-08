package net.azisaba.pluginymlanalysis.util

import net.azisaba.pluginymlanalysis.PluginDescriptionFile

object PluginDescriptionUtil {
    fun findDependents(
        pluginDescriptions: List<PluginDescriptionFile>,
        target: PluginDescriptionFile,
        hard: Boolean,
    ): List<PluginDescriptionFile> {
        val dependents = mutableListOf<PluginDescriptionFile>()

        for (pluginDescription in pluginDescriptions) {
            val list = if (hard) pluginDescription.depend else pluginDescription.softdepend
            if (list?.contains(target.name) == true) {
                dependents.add(pluginDescription)
            }
        }

        return dependents
    }
}
