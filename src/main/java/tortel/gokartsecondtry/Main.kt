package tortel.gokartsecondtry

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import org.bukkit.Bukkit
import org.bukkit.entity.ArmorStand
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Commands.RaceCommand
import tortel.gokartsecondtry.Commands.RideCommand
import tortel.gokartsecondtry.Listeners.*
import tortel.gokartsecondtry.Utils.RaceUtils
import tortel.gokartsecondtry.Utils.RaceUtils.playersInRace
import tortel.gokartsecondtry.Utils.VehicleUtils
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
        VehicleUtils.startRaceTicking()
        //setupTickSystem()
        logger.info("GoKart Plugin Enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        onDisablelogic()
        RaceUtils.stopRace("whateva")
    }

    fun registerEvents(){
        protocolManager!!.addPacketListener(PlayerVehicleInput(this))
        //protocolManager!!.addPacketListener(UnHeldKeyEvent(this))
        pluginmanager.registerEvents(PlayerJoinEvent(), this)
        pluginmanager.registerEvents(PlayerSpaceHeld(), this)
        pluginmanager.registerEvents(PlayerQuitVehicle(), this)

    }

    fun registerCommands(){
        getCommand("ride")?.setExecutor(RideCommand())
        getCommand("race")?.setExecutor(RaceCommand())
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
        VehicleUtils.PlayerArmorStands.forEach {
            val Entity = it.value
            val player = it.key

            Entity.remove()
            VehicleUtils.PlayerArmorStands.remove(player)
        }

    }

    fun setupTickSystem(){
        object : BukkitRunnable() {
            override fun run() {
               if (!RaceUtils.RaceStarted) return
                for (plr in playersInRace) {
                    if (VehicleUtils.getplrVehicle(plr) == null || VehicleUtils.PlayerHorses[plr] == null || VehicleUtils.PlayerArmorStands[plr] == null)
                        continue

                    val Horse = VehicleUtils.PlayerHorses[plr] ?: continue
                    val ArmorStand = VehicleUtils.PlayerArmorStands[plr] ?: continue
                    val plrVRotation = VehicleUtils.PlayerRotations[plr] ?: continue

                    if (!Horse.isValid || !ArmorStand.isValid){
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
                    ArmorStand.teleport(Horse)

                    //println("Teleported ArmorStand to Horse at: ${Horse.location}")

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
}
