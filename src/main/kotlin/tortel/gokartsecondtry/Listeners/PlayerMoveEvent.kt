package tortel.gokartsecondtry.Listeners

import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import tortel.gokartsecondtry.Utils.Garage.GarageVehicleUtils
import tortel.gokartsecondtry.data.Checkpoint
import tortel.gokartsecondtry.data.Memory.server
import tortel.gokartsecondtry.Utils.Race.RaceUtils
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.getVehicleManager
import tortel.gokartsecondtry.data.Memory
import java.util.*

class PlayerMoveEvent : Listener {

    @EventHandler
    fun onKartMove(event : EntityMoveEvent) {
        if (event.entity.type == EntityType.HORSE) {
            if (event.entity.scoreboardTags.contains("kart")) {
                val world: String = event.to.world.name
                // Bukkit.broadcastMessage(world)
                val a: MutableList<Checkpoint> = server?.checkpoint?.get(world)!!
                Bukkit.broadcastMessage("all checlpoints : ${a.size}")
                a.add(server?.finishLine?.get(world)!!)

                for (ch in a) {
                    val from = event.from
                    val to = event.to
                    val distanceCheck: Double = ch.length?.toDouble()!!

                    if (ch.direction == false) {
                        //checking player crossed checkpoint line
                        if (ch.vector?.z == 1.0 && ( from.z < ch.z!! && to.z >= ch.z!!) || ch.vector?.z == -1.0 && (from.z > ch.z!! && to.z <= ch.z!!)) {
                            if (kotlin.math.abs(to.y - ch.y!!) <= 1 && kotlin.math.abs(to.x - ch.x!!) <= distanceCheck) {
                                var plr : Player? = null
                                for (ab in event.entity.scoreboardTags) {
                                    if (ab != "kart") {
                                        plr = Bukkit.getPlayer(UUID.fromString(ab))
                                    }
                                }
                                if (ch.number == 0) {
                                    var num = 0
                                    for (cha in server?.checkpoint?.get(world)!!) {
                                        num +=1
                                    }
                                    if (Memory.getPlayerMemory(plr!!)?.racePos == num) {
                                        Memory.getPlayerMemory(plr)?.racePos = 0
                                        Memory.getPlayerMemory(plr)?.let {
                                            it.laps +=1
                                        }
                                    }

                                } else if ((ch.number - 1) == Memory.getPlayerMemory(plr!!)?.racePos) {
                                    Memory.getPlayerMemory(plr)?.let {
                                        it.racePos +=1
                                    }
                                }
                            }
                        }
                    } else {
                        //checking player crossed checkpoint line
                        if (ch.vector?.x == 1.0 && (from.x < ch.x!! && to.x >= ch.x!!) || ch.vector?.x == -1.0 &&(from.x > ch.x!! && to.x <= ch.x!!)) {
                            if (kotlin.math.abs(to.y - ch.y!!) <= 1 && kotlin.math.abs(to.z - ch.z!!) <= distanceCheck) {
                                var plr : Player? = null
                                for (a in event.entity.scoreboardTags) {
                                    if (a != "kart") {
                                        plr = Bukkit.getPlayer(UUID.fromString(a))
                                    }
                                }
                                if (ch.number == 0) {
                                    var num = 0
                                    for (cha in server?.checkpoint?.get(world)!!) {
                                        num +=1
                                    }
                                    if (Memory.getPlayerMemory(plr!!)?.racePos == num) {
                                        Memory.getPlayerMemory(plr)?.racePos = 0
                                        Memory.getPlayerMemory(plr)?.let {
                                            it.laps +=1
                                        }
                                    }

                                } else if ((ch.number - 1) == Memory.getPlayerMemory(plr!!)?.racePos) {
                                    Memory.getPlayerMemory(plr)?.let {
                                        it.racePos +=1
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    fun onPunch(event : PlayerMoveEvent){
        val plr = event.player
       // println("${event.player} ${event.eventName} ${event.player.isInsideVehicle} ${event.player.isSneaking}")
        if (RaceUtils.RaceStarted){
            if (!plr.isInsideVehicle && RaceUtils.PlayersInRace.contains(plr)){
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