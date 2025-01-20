package tortel.gokartsecondtry.Utils.Garage

import com.comphenix.protocol.PacketType.Play
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Interaction
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.CONSTANTS
import tortel.gokartsecondtry.Utils.RaceUtils
import tortel.gokartsecondtry.Utils.VehicleUtils
import kotlin.collections.HashMap

object GarageVehicleUtils {
    val PlayersInGarage = mutableListOf<Player>()
    val PlayerKarts = HashMap<Player,Entity>()
    val PlayerHitboxes = HashMap<Player,Entity>()
    fun playerEnterGarage(plr : Player, loc : Location){ // spawn kart when player goes to garage
        SpawnKartEntitiesForPlr(plr, loc)
        hidePlayer(plr)
    }

    fun loadKartInfo(plr: Player, kart : ItemDisplay){ // load kart info and data
        //TODO: LOAD FROM MONGODB
    }

    fun playerLeaveGarage(plr : Player){ // save kart info and data
        //TODO: SAVE TO MONGODB
        if (!PlayersInGarage.contains(plr) || !PlayerKarts.contains(plr) || !PlayerHitboxes.contains(plr)) return
        PlayerKarts[plr]!!.remove()
        PlayerHitboxes[plr]!!.remove()

        unHidePlayer(plr)
    }

    fun SpawnKartEntitiesForPlr(plr : Player, loc : Location){
        val ItemDisplay = loc.world.spawnEntity(Location(loc.world,loc.x, loc.y + CONSTANTS.KartOffset.y, loc.z, loc.yaw, 0f), EntityType.ITEM_DISPLAY) as ItemDisplay
        ItemDisplay.isInvulnerable = true
        ItemDisplay.addScoreboardTag("DisplayKart_${plr.name}")
        ItemDisplay.setItemStack(CONSTANTS.getMainkart())
        ItemDisplay.isGlowing = true

        val HitBox = loc.world.spawnEntity(Location(loc.world,loc.x, loc.y, loc.z, loc.yaw, 0f), EntityType.INTERACTION) as Interaction
        HitBox.interactionHeight = 1f
        HitBox.interactionWidth = 2f
        HitBox.addScoreboardTag("KartInteraction_1_${plr.uniqueId}")

        PlayerKarts[plr] = ItemDisplay
        PlayerHitboxes[plr] = HitBox

        Bukkit.getOnlinePlayers().forEach { onlineplr ->
            if (onlineplr.uniqueId != plr.uniqueId) {
                onlineplr.hideEntity(Main.instance!!, ItemDisplay)
                onlineplr.hideEntity(Main.instance!!, HitBox)
                onlineplr.hideEntity(Main.instance!!, plr)
                println("hid entity to plr ${onlineplr.name}")
            }

        }

        /*

        val spawnPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY)
        spawnPacket.uuiDs.write(0, UUID.randomUUID())
        spawnPacket.id
        spawnPacket.doubles
            .write(0, loc.x)
            .write(1, loc.y)
            .write(2, loc.z) // Position
        spawnPacket.bytes
            .write(0, 0) // Pitch
            .write(1, 0) // Yaw
        spawnPacket.integers.write(1, 21) // Item Display entity type

        ProtocolLibrary.getProtocolManager().sendServerPacket(plr, spawnPacket)

         */
    }


    fun CustomizeMainpart(){ // open gui that asks player for new color

    }

    fun OnCustomizeMainpart(){ // when player clicks something in the gui do something

    }
    fun hidePlayer(plr : Player){
        Bukkit.getOnlinePlayers().forEach { onlineplr ->
            if (onlineplr.uniqueId != plr.uniqueId) {
                onlineplr.hideEntity(Main.instance!!, plr)
            }

        }
    }
    fun unHidePlayer(plr : Player){
        Bukkit.getOnlinePlayers().forEach { onlineplr ->
            if (onlineplr.uniqueId != plr.uniqueId) {
                onlineplr.showEntity(Main.instance!!, plr)
            }

        }
    }

    fun handlePlayerJoinServer(plr : Player){
        Bukkit.getOnlinePlayers().forEach { onlineplr ->
            if (PlayersInGarage.contains(onlineplr)) {
                object : BukkitRunnable() {
                    override fun run() {

                        plr.hideEntity(Main.instance!!, onlineplr)
                        plr.hideEntity(Main.instance!!, PlayerKarts[onlineplr]!!)
                        plr.hideEntity(Main.instance!!, PlayerKarts[onlineplr]!!)

                    }
                }.runTaskLater(Main.instance!!, 20) // originally 20

            }
        }
    }

    fun handlePlayerLeaveServer(plr : Player){
        if (PlayersInGarage.contains(plr)) {
            playerLeaveGarage(plr)
        }
    }
}