package tortel.gokartsecondtry

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
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
        onDisablelogic()
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

                Bukkit.getOnlinePlayers().forEach {
                    val plr = it
                    if (VehicleUtils.getplrVehicle(plr) == null || VehicleUtils.PlayerHorses[plr] == null || VehicleUtils.PlayerArmorStands[plr] == null) return

                    //VehicleUtils.BounceBackIfWallAhead(plr)
                    //VehicleUtils.getMobPlayerIsRiding(plr)!!.let { horse -> Bukkit.getMobGoals().removeAllGoals(horse) }
                    val Horse = VehicleUtils.PlayerHorses[plr]!!
                    val ArmorStand = VehicleUtils.PlayerArmorStands[plr]!!
                    val plrVRotation = VehicleUtils.PlayerRotations[plr]!!
                    //VELOCITY
                    Horse.velocity =  Vector(Horse.location.direction.x,0.5,Horse.location.direction.z).multiply( //TODO: HOVER CONSTANT for gliders
                        Vector(VehicleUtils.PlayersVelocities[plr]!!,-5.0, VehicleUtils.PlayersVelocities[plr]!!)
                    )

                    //ROTATION
                    Horse.setRotation(plrVRotation.first, 0.0f)

                    //ARMOR STAND
                    ArmorStand.teleport(Horse)
                    /*
                    val Atest = plr.world.spawnEntity(plr.location, EntityType.ARMOR_STAND) as ArmorStand
                    Atest.isInvulnerable = true
                    Atest.isInvisible = false
                    Atest.isCustomNameVisible = false
                    Atest.setGravity(false)
                    Atest.isMarker = true
                    Atest.isSilent = true
                    Atest.isSmall = true

                     */

                    //Atest.teleport(Horse)



                    //DECELERATION
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
