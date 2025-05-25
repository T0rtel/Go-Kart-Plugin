package tortel.gokartsecondtry

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager

import com.mongodb.client.MongoDatabase
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import revxrsal.commands.Lamp
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import tortel.gokartsecondtry.Commands.GarageCommand
import tortel.gokartsecondtry.Commands.RaceCommand
import tortel.gokartsecondtry.Commands.TrackSetup.RacePosCommands
import tortel.gokartsecondtry.Listeners.*
import tortel.gokartsecondtry.Utils.Race.RaceUtils
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils
import tortel.gokartsecondtry.Vehicle.VehicleManager
import tortel.gokartsecondtry.data.RaceTracksConfig
import java.io.File

class Main : JavaPlugin() {

    val pluginmanager = Bukkit.getPluginManager()

    companion object {
        fun dataFolder(): File {
            return this.dataFolder()
        }
        var dataFolderDir: File = File("")
            private set
        var instance: JavaPlugin? = null
            private set
        var mongoDb: MongoDatabase? = null
            private set
        var vehicleManager: VehicleManager? = null
            private set
    }//test

    override fun onEnable() {

        instance = this
        //protocolManager = ProtocolLibrary.getProtocolManager()
        dataFolderDir = dataFolder
        vehicleManager = VehicleManager()

        setupConfigsOnEnable()
        registerEvents()
        registerCommands(this)
        VehicleUtils.startVehicleTicking()
        //setupConfigTickSystem()
        //setupTickSystem()

        logger.info("GoKart Plugin Enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        //onDisablelogic()
        saveConfigsOnDisable()
        RaceUtils.stopRace("whateva")
    }
    fun registerEvents(){
       // protocolManager!!.addPacketListener(PlayerVehicleInput(this))
        //protocolManager!!.addPacketListener(UnHeldKeyEvent(this))
        pluginmanager.registerEvents(PlayerInventoryInput(), this)
        pluginmanager.registerEvents(PlayerJoinLeaveEvent(), this)
        pluginmanager.registerEvents(PlayerQuitVehicle(), this)
        pluginmanager.registerEvents(PlayerMoveEvent(), this)
        pluginmanager.registerEvents(PlayerPunchEntityEvent(), this)

    }

    fun registerCommands(plugin: JavaPlugin){
        val lamp: Lamp<BukkitCommandActor> = BukkitLamp.builder(plugin)
            .build()//e

        lamp.register(RaceCommand())
        lamp.register(GarageCommand())
        lamp.register(RacePosCommands())
    }

    private fun setupConfigsOnEnable() {
        config.set("plrcount", 0)
            /*  if (config.contains("")) {

        }*/
        saveConfig()

        RaceTracksConfig.load()


        logger.info("Configs Setup!")
    }

    private fun saveConfigsOnDisable() {
        RaceTracksConfig.save()
        this.saveConfig()

        logger.info("Configs Saved!")
    }

}
