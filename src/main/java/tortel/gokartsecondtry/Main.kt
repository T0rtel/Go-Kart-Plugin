package tortel.gokartsecondtry

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import tortel.gokartsecondtry.Commands.RideCommand
import tortel.gokartsecondtry.Listeners.PlayerVehicleMove
import java.io.File

class Main : JavaPlugin() {
    val pluginmanager = Bukkit.getPluginManager()
    private var protocolManager: ProtocolManager? = null
    companion object {
        var dataFolderDir: File = File("")
            private set
        var instance : Plugin? = null
            private set
    }//test

    override fun onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager()
        dataFolderDir = dataFolder
        instance = this

        registerEvents()
        registerCommands()
        logger.info("GoKart Plugin Enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    fun registerEvents(){
       protocolManager!!.addPacketListener(PlayerVehicleMove(this))
        //pluginmanager.registerEvents(PlayerVehicleMove(), this)
    }

    fun registerCommands(){
        getCommand("ride")?.setExecutor(RideCommand())
    }
}
