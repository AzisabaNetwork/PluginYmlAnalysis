package net.azisaba.pluginymlanalysis

import net.azisaba.pluginymlanalysis.util.FileUtil
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class PluginMain : JavaPlugin() {
    override fun onEnable() {
        getCommand("pluginymlanalysis")?.setExecutor(PluginYmlAnalysisCommand())
    }

    inner class PluginYmlAnalysisCommand : TabExecutor {
        override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
            if (args.isEmpty()) {
                sender.sendMessage("Usage: /pluginymlanalysis <plugin>")
                return true
            }
            Bukkit.getScheduler().runTaskAsynchronously(this@PluginMain, Runnable {
                val files = FileUtil.collectJarFiles(File("./plugins").toPath())
                val descriptions = PluginDescriptionFile.readFiles(files)
                val target = descriptions.find { it.name.equals(args[0], ignoreCase = true) }
                if (target != null) {
                    sender.sendMessage(target.toString(descriptions).replace(" {2}".toRegex(RegexOption.MULTILINE), " §f §f"))
                }
            })
            return true
        }

        override fun onTabComplete(
            sender: CommandSender,
            p1: Command,
            p2: String,
            args: Array<out String>
        ): List<String>? {
            if (args.size == 1) {
                return Bukkit.getPluginManager().plugins.map { it.name }.filter { it.lowercase().startsWith(args[0].lowercase()) }
            }
            return emptyList()
        }
    }
}
