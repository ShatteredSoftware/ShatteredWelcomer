package com.github.shatteredsoftware.welcomer

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.nio.file.Files
import kotlin.math.roundToLong
import kotlin.properties.Delegates

class ShatteredWelcomer : JavaPlugin() {
    private lateinit var config: YamlConfiguration
    private lateinit var messages: String
    private var delay by Delegates.notNull<Double>()

    private val configFile = File(dataFolder, "config.yml")

    override fun onLoad() {
        if (!configFile.exists()) {
            dataFolder.mkdirs()
            val input = getResource("config.yml")!!
            Files.copy(input, configFile.toPath())
        }

        config = YamlConfiguration.loadConfiguration(configFile)
        messages = config.getString("message") ?: ""
        delay = config.getDouble("delay")
    }

    override fun onEnable() {
        val miniMessage = MiniMessage.miniMessage()
        val audiences = BukkitAudiences.create(this)
        val papi = server.pluginManager.isPluginEnabled("PlaceholderAPI")
        if (messages.isEmpty()) {
            logger.info("Message is empty. Disabling.")
            this.isEnabled = false
            return
        }
        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onJoin(playerJoinEvent: PlayerJoinEvent) {
                val player = playerJoinEvent.player
                val audience = audiences.player(player)
                val message = if (papi) {
                    PlaceholderAPI.setPlaceholders(player, messages)
                } else messages
                val r = object : BukkitRunnable() {
                    override fun run() {
                        audience.sendMessage(miniMessage.deserialize(message))
                    }
                }
                r.runTaskLater(this@ShatteredWelcomer, (delay * 20).roundToLong())
            }
        }, this)
    }
}