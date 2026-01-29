package tortel.gokartsecondtry.Listeners

import com.destroystokyo.paper.ParticleBuilder
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.data.Checkpoint
import tortel.gokartsecondtry.data.Memory.server
import tortel.gokartsecondtry.Utils.Race.RaceUtils
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.getVehicleManager
import tortel.gokartsecondtry.data.Memory
import tortel.gokartsecondtry.data.RaceData
import java.util.*

class PlayerMoveEvent : Listener {

    @EventHandler
    fun onKartMove(event : EntityMoveEvent) {
        if (event.entity.type == EntityType.HORSE) {
            if (event.entity.scoreboardTags.contains("kart")) {
                val world: String = event.to.world.name

                for (location in Memory.server?.raceData?.get(world)?.itemboxes!!) {
                    if (event.to.distance(location) <= 0.4) {
                        var plr: Player? = null
                        for (a in event.entity.scoreboardTags) {
                            if (a != "kart") {
                                plr = Bukkit.getPlayer(UUID.fromString(a))
                            }
                        }
                        if (Memory.getPlayerMemory(plr!!)?.currentItem == "") {
                            if (Memory.server?.races?.get(Memory.getPlayerMemory(plr)?.raceID)?.boxes?.keys?.contains(
                                    location
                                )!!
                            ) {
                                Memory.server?.races?.get(Memory.getPlayerMemory(plr)?.raceID)?.boxes?.get(location)
                                    ?.remove()
                                ParticleBuilder(Particle.TOTEM_OF_UNDYING).location(location).count(10).extra(0.3)
                                    .spawn()

                                RaceUtils().boxOpen(plr)
                                object : BukkitRunnable() {
                                    override fun run() {
                                        val paa = event.to.world.spawn(location.clone().add(0.0,1.0,0.0), ItemDisplay::class.java)
                                        val a = ItemStack(Material.LEATHER_HORSE_ARMOR)
                                        a.itemMeta.setCustomModelData(1223)
                                        paa.setItemStack(a)
                                        Memory.server?.races?.get(Memory.getPlayerMemory(plr!!)?.raceID)?.boxes?.put(
                                            location,
                                            paa
                                        )
                                    }
                                }.runTaskLater(Main.instance!!, 600)


                            }
                    }
                        break
                    }
                }
                val a: MutableList<Checkpoint> = mutableListOf()
                a.addAll(server?.raceData?.get(world)?.checkpoint!!)
                a.add(server?.raceData?.get(world)?.finishLine!!)

                val aer = event.to.clone().add(0.0,-1.0, 0.0).block.type
                if (aer == Material.MAGENTA_GLAZED_TERRACOTTA) {
                    var plr : Player? = null
                    for (a in event.entity.scoreboardTags) {
                        if (a != "kart") {
                            plr = Bukkit.getPlayer(UUID.fromString(a))
                        }
                    }
                    VehicleUtils.boost(plr!!)
                } else if (aer == Material.PINK_GLAZED_TERRACOTTA) {
                    var plr : Player? = null
                    for (a in event.entity.scoreboardTags) {
                        if (a != "kart") {
                            plr = Bukkit.getPlayer(UUID.fromString(a))
                        }
                    }
                    VehicleUtils.jump(plr!!)
                }
                for (ch in a) {
                    val from = event.from
                    val to = event.to
                    val distanceCheck: Double = ch.length?.toDouble()!!

                    if (ch.direction == false) {
                        //checking player crossed checkpoint line
                        if (ch.vector?.z == 1.0 && (from.z < ch.z!! && to.z >= ch.z!!) || ch.vector?.z == -1.0 && (from.z > ch.z!! && to.z <= ch.z!!)) {
                            if (kotlin.math.abs(to.y - ch.y!!) <= 1 && kotlin.math.abs(to.x - ch.x!!) <= distanceCheck) {
                                crossed(ch, world, event)

                            }
                        }
                    } else {
                        //checking player crossed checkpoint line
                        if (ch.vector?.x == 1.0 && (from.x < ch.x!! && to.x >= ch.x!!) || ch.vector?.x == -1.0 && (from.x > ch.x!! && to.x <= ch.x!!)) {
                            if (kotlin.math.abs(to.y - ch.y!!) <= 1 && kotlin.math.abs(to.z - ch.z!!) <= distanceCheck) {
                crossed(ch,world,event)
                            }

                        }
                    }
                }
            }
        }
    }


    private fun crossed(ch : Checkpoint, world : String, event : EntityMoveEvent) {
        val to = event.to
        var plr : Player? = null
        for (a in event.entity.scoreboardTags) {
            if (a != "kart") {
                plr = Bukkit.getPlayer(UUID.fromString(a))
            }
        }
        if (ch.number == 0) {
            Bukkit.broadcastMessage("${server?.raceData?.get(world)?.checkpoint?.size}")
            if (Memory.getPlayerMemory(plr!!)?.racePos == (server?.raceData?.get(world)?.checkpoint?.size!! -1)) {
                Bukkit.broadcastMessage("passed")
                Memory.getPlayerMemory(plr)?.racePos = 0
                Memory.getPlayerMemory(plr)?.let {
                    it.laps +=1
                }
                plr.sendMessage("§7Lap completed in §f${Memory.getPlayerMemory(plr)?.lapTime?.div(20)}s")
                Memory.getPlayerMemory(plr)?.lapTime = 0.0
                if (Memory.getPlayerMemory(plr)?.laps!! >= server?.raceData?.get(world)?.laps!!) {
                    Bukkit.broadcastMessage("§7${plr.name()} §fHas completed the race!")
                    getVehicleManager()?.removeVehicle(plr)
                    plr.isInvisible = true
                    plr.allowFlight = true
                    //todo: cleanup and add ability to leave race once completed and spectate
                    Memory.getPlayerMemory(plr)?.let {
                        it.laps = 0
                    }
                    Memory.getPlayerMemory(plr)?.inRace = false
                    server?.races?.get(Memory.getPlayerMemory(plr)?.raceID)?.finished?.add(plr)
                    val place = server?.races?.get(Memory.getPlayerMemory(plr)?.raceID)?.finished?.indexOf(plr)
                    place?.plus(1)
                    val string = when (place) {
                        1 -> "§e§l§o${place}st"
                        2 -> "§7§l§o${place}nd"
                        3 -> "§x§c§d§7§f§3§2§l§o${place}rd"
                        else -> "§8${place}th"
                    }
                    plr.sendTitle("§f\uD83C\uDFC1 §x§F§F§F§F§F§F§l§oR§x§E§F§E§F§E§F§l§oa§x§D§F§D§F§D§F§l§oc§x§C§F§C§F§C§F§l§oe §x§A§F§A§F§A§F§l§oF§x§A§0§A§0§A§0§l§oi§x§9§0§9§0§9§0§l§on§x§8§0§8§0§8§0§l§oi§x§7§0§7§0§7§0§l§os§x§6§0§6§0§6§0§l§oh§x§5§0§5§0§5§0§l§oe§x§4§0§4§0§4§0§l§od §f\uD83C\uDFC1","§7Finished $string §8| §f${Memory.getPlayerMemory(plr)?.time?.div(
                        20
                    )}s",10,100,10)
                    plr.playSound(plr,Sound.UI_TOAST_CHALLENGE_COMPLETE,1f,1f)
                    plr.playSound(plr,Sound.ENTITY_VILLAGER_CELEBRATE,1f,1f)
                    server?.races?.get(Memory.getPlayerMemory(plr)?.raceID!!)?.raceUtil?.stopRaceIfEmpty(Memory.getPlayerMemory(plr)?.raceID!!)
                    Memory.getPlayerMemory(plr)?.currentItem = ""
                    for (item in plr.inventory.contents) {
                        if (item?.hasItemMeta()!!) {
                            if (item.itemMeta?.persistentDataContainer?.get(
                                    NamespacedKey(
                                        Main.instance!!,
                                        "box"
                                    ), PersistentDataType.STRING
                                ) != null
                            ) {
                                plr.inventory.remove(item)
                            }
                        }
                    }
                } else {
                    plr.playSound(plr,Sound.BLOCK_BELL_USE,1f,1.7f)
                }
            }

        } else if ((ch.number - 1) == Memory.getPlayerMemory(plr!!)?.racePos) {
            Memory.getPlayerMemory(plr)?.let {
                it.racePos +=1
            }
        }
        Bukkit.broadcastMessage("${plr.name} crossed! checkpoint ${ch.number} at ${to.x}, ${to.y}, ${to.z} in world ${event.to.world.name} ${Memory.getPlayerMemory(plr)?.racePos}")
    }


    @EventHandler
    fun onPunch(event : PlayerMoveEvent){
        val plr = event.player
       // println("${event.player} ${event.eventName} ${event.player.isInsideVehicle} ${event.player.isSneaking}")
        if (Memory.getPlayerMemory(plr)?.inRace!!){
            if (!plr.isInsideVehicle && server?.races?.get(Memory.getPlayerMemory(plr)?.raceID)?.PlayersInRace?.contains(plr)!!){
                val VehicleManager = getVehicleManager()!!
                val Vehicle = VehicleManager.getVehicle(plr)

                 Vehicle.VehicleItemDisplays[0].addPassenger(plr)
                println("player got off vehicle")
            }
            if (plr.isInsideVehicle) {
                plr.sendMessage("yesa")
            }

        }
    }

}