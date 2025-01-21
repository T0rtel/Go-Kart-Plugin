package tortel.gokartsecondtry

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.hakan.core.HCore
import com.mongodb.Mongo
import com.mongodb.client.MongoDatabase
import lombok.Getter
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.player.PlayerJoinEvent
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
import tortel.gokartsecondtry.Utils.RaceUtils
import tortel.gokartsecondtry.Utils.VehicleUtils
import tortel.gokartsecondtry.data.RaceTracksConfig
import tortel.gokartsecondtry.data.database.MongoDb
import java.io.File

class Main : JavaPlugin() {

    val pluginmanager = Bukkit.getPluginManager()
    private var protocolManager: ProtocolManager? = null

    companion object {
        var dataFolderDir: File = File("")
            private set
        var instance: JavaPlugin? = null
            private set
        var mongoDb: MongoDatabase? = null
            private set
    }//test

    override fun onEnable() {
        instance = this
        //HCore.initialize(this)
        protocolManager = ProtocolLibrary.getProtocolManager()
        dataFolderDir = dataFolder
        try {
            mongoDb = MongoDb.SetupDB("playerData","accounts") // mongodb+srv://admin:HtKyprc87BMYKRCo4rTrG3eMECjqdS@clobnet.ucuwlbq.mongodb.net/
        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("PROFILES : ${MongoDb.getCollection("accounts").find().first()}")

        setupConfigsOnEnable()
        registerEvents()
        registerCommands(this)
        VehicleUtils.startRaceTicking()
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
        protocolManager!!.addPacketListener(PlayerVehicleInput(this))
        //protocolManager!!.addPacketListener(UnHeldKeyEvent(this))
        pluginmanager.registerEvents(PlayerJoinLeaveEvent(), this)
        pluginmanager.registerEvents(PlayerSpaceHeld(), this)
        pluginmanager.registerEvents(PlayerQuitVehicle(), this)
        pluginmanager.registerEvents(PlayerMoveEvent(), this)
        pluginmanager.registerEvents(PlayerPunchEntityEvent(), this)

    }

    fun registerCommands(plugin: JavaPlugin){
        val lamp: Lamp<BukkitCommandActor> = BukkitLamp.builder(plugin)
            .build()

        lamp.register(RaceCommand())
        lamp.register(GarageCommand())
       // lamp.register(RideCommand())
        lamp.register(RacePosCommands())
        //getCommand("ride")?.setExecutor(RideCommand())
        //getCommand("race")?.setExecutor(RaceCommand())
    }

    private fun setupConfigsOnEnable() {
        config.set("plrcount", 0)
        saveConfig()

        RaceTracksConfig.load()


        logger.info("Configs Setup!")
    }

    private fun saveConfigsOnDisable() {
        RaceTracksConfig.save()
        this.saveConfig()

        logger.info("Configs Saved!")
    }

    fun onDisablelogic(){
        //remove all horses
        
        VehicleUtils.PlayerHorses.forEach {
            val Entity = it.value
            val player = it.key

            Entity.remove()
            VehicleUtils.PlayerHorses.remove(player)
        }

        //remove all horses
        VehicleUtils.PlayerItemDisplays.forEach {
            val Entity = it.value
            val player = it.key

            Entity.remove()
            VehicleUtils.PlayerItemDisplays.remove(player)
        }
        

    }

    private fun setupConfigTickSystem(){
        object : BukkitRunnable() {
            override fun run() {
                // CachedConfig.reload() // so if anyone verifies his discord it could be detected
                //RaceTracksConfig.reload()
                if (!RaceUtils.RaceStarted) return
                Bukkit.getWorld("kartmap")!!.entities.forEach { entity ->
                    if (entity.type == EntityType.ITEM_DISPLAY){
                        entity.teleport(
                            VehicleUtils.PlayerHorses[Bukkit.getPlayer("TTortel")]!!.location.add(0.0,0.0,0.0))
                        println("teleporting ${entity.type}")
                    }
                }
            }
        }.runTaskTimer(this, 1, 1) // originally 20
    }

    /*

    fun setupTickSystem(){
        object : BukkitRunnable() {
            override fun run() {
               if (!RaceUtils.RaceStarted) return
                for (plr in PlayersInRace) {
                    if (VehicleUtils.getplrVehicle(plr) == null || VehicleUtils.PlayerHorses[plr] == null || VehicleUtils.PlayerItemDisplays[plr] == null)
                        continue

                    val Horse = VehicleUtils.PlayerHorses[plr] ?: continue
                    val ItemDisplay = VehicleUtils.PlayerItemDisplays[plr] ?: continue
                    val plrVRotation = VehicleUtils.PlayerRotations[plr] ?: continue

                    if (!Horse.isValid || !ItemDisplay.isValid){
                        println("WOOPSIES")
                        continue
                    }

                    // VELOCITY
                    Horse.velocity = Vector(Horse.location.direction.x, 0.5, Horse.location.direction.z).multiply(
                        Vector(VehicleUtils.PlayersVelocities[plr]!!, -5.0, VehicleUtils.PlayersVelocities[plr]!!)
                    )

                    // ROTATION
                    Horse.setRotation(plrVRotation, 0.0f)

                    // ARMOR STAND
                    ItemDisplay.teleport(Horse)

                    //println("Teleported ItemDisplay to Horse at: ${Horse.location}")

                    // DECELERATION
                    if (!VehicleUtils.PlayersAccelerating.contains(plr) && VehicleUtils.PlayersVelocities[plr]!! > 0.0) {
                        //println("Decelerate plr")
                        VehicleUtils.DecreaseVel(plr)
                    }

                    VehicleUtils.PlayersAccelerating.remove(plr)
                }

            }
        }.runTaskTimer(this, 1, 1)

    }
     */

}
