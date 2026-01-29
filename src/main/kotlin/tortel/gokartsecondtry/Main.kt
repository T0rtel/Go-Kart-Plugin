package tortel.gokartsecondtry

import com.mongodb.client.MongoDatabase
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.Lamp
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import tortel.gokartsecondtry.Commands.GarageCommand
import tortel.gokartsecondtry.Commands.RaceCommand
import tortel.gokartsecondtry.Commands.TrackSetup.BuilderCommands
import tortel.gokartsecondtry.Commands.TrackSetup.RacePosCommands
import tortel.gokartsecondtry.Listeners.*
import tortel.gokartsecondtry.Vehicle.VehicleManager
import tortel.gokartsecondtry.data.Checkpoint
import tortel.gokartsecondtry.data.RaceData
import tortel.gokartsecondtry.data.RaceTracksConfig
import java.io.File

class Main : JavaPlugin() {



    private val pluginmanager = Bukkit.getPluginManager()

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
            ConfigurationSerialization.registerClass(RaceData::class.java, "raceData")
        ConfigurationSerialization.registerClass(Checkpoint::class.java, "checkpoint")
        instance = this
        //protocolManager = ProtocolLibrary.getProtocolManager()
        dataFolderDir = dataFolder
        vehicleManager = VehicleManager()

        setupConfigsOnEnable()
        registerEvents()
        registerCommands(this)

        //setupConfigTickSystem()
        //setupTickSystem()

        logger.info("GoKart Plugin Enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        //onDisablelogic()
        saveConfigsOnDisable()
     //todo: stop race
    }
    fun registerEvents(){
       // protocolManager!!.addPacketListener(PlayerVehicleInput(this))
        //protocolManager!!.addPacketListener(UnHeldKeyEvent(this))
        pluginmanager.registerEvents(PlayerVehicleInput(this), this)
        pluginmanager.registerEvents(PlayerInventoryInput(), this)
        pluginmanager.registerEvents(PlayerJoinLeaveEvent(), this)
        pluginmanager.registerEvents(PlayerQuitVehicle(), this)
        pluginmanager.registerEvents(PlayerMoveEvent(), this)
        pluginmanager.registerEvents(PlayerPunchEntityEvent(), this)

    }

    fun registerCommands(plugin: JavaPlugin){
        val lamp: Lamp<BukkitCommandActor> = BukkitLamp.builder(plugin).suggestionProviders {providers ->
        providers.addProvider(World::class.java) { _ ->
            Bukkit.getWorlds().map { it.name }
        }
    }.build()//e
        lamp.register(RaceCommand())
       lamp.register(BuilderCommands())
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
class MyTabCompleter : TabCompleter {
    private val additionalSuggestions = mutableListOf("option1", "option2", "option3")

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String>? {
        return when (args.size) {
            1 -> listOf("forcestart", "stop", "queue") // Initial suggestions
            else -> additionalSuggestions // Suggestions from the mutable list
        }
    }
}