package tortel.gokartsecondtry

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import org.bukkit.Bukkit
import org.bukkit.entity.Mob
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Commands.RideCommand
import tortel.gokartsecondtry.Listeners.PlayerJoinEvent
import tortel.gokartsecondtry.Listeners.PlayerSpaceHeld
import tortel.gokartsecondtry.Listeners.PlayerVehicleInput
import tortel.gokartsecondtry.Listeners.UnHeldKeyEvent
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
        setupTickSystem()
        logger.info("GoKart Plugin Enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    fun registerEvents(){
        protocolManager!!.addPacketListener(PlayerVehicleInput(this))
        protocolManager!!.addPacketListener(UnHeldKeyEvent(this))
        pluginmanager.registerEvents(PlayerJoinEvent(), this)
        pluginmanager.registerEvents(PlayerSpaceHeld(), this)

    }

    fun registerCommands(){
        getCommand("ride")?.setExecutor(RideCommand())
    }

    fun setupTickSystem(){
        object : BukkitRunnable() {
            override fun run() {

                Bukkit.getOnlinePlayers().forEach {
                    val plr = it
                    if (VehicleUtils.getplrVehicle(plr) == null) return

                    //VehicleUtils.BounceBackIfWallAhead(plr)
                    //VehicleUtils.getMobPlayerIsRiding(plr)!!.let { horse -> Bukkit.getMobGoals().removeAllGoals(horse) }

                    //plr deceleration
                    if (!VehicleUtils.PlayersAccelerating.contains(plr) && VehicleUtils.PlayersVelocities.get(plr)!! > 0.0) {
                        println("Decelerate plr")
                        //deceleration
                        VehicleUtils.DecreaseVel(plr)

                    }

                    VehicleUtils.PlayersAccelerating.remove(plr)
                }
            }
        }.runTaskTimer(this, 1, 1)

    }
}
