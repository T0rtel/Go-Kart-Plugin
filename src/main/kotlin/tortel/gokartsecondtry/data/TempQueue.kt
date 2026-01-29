package tortel.gokartsecondtry.data

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.Race.RaceUtils
import java.util.*
import kotlin.math.roundToInt

class TempQueue (initialPlayer: Player, trackName:String, loca: Location) {
    val loc = loca
    val players : MutableList<Player> = mutableListOf(initialPlayer)
    val track = trackName
    var forceStart : Boolean = false
    val timerInSeconds : Double = 30.0
    fun TempQueue() : TempQueue {
        //Send Started Queue Message
        for (plr in Bukkit.getOnlinePlayers()) {
            //checking if plr is not in race and not in this queue
            if (!Memory.getPlayerMemory(plr)?.inRace!! && !players.contains(plr)) {
                //text
                var component: TextComponent = Component.text("§7A §6§lqueue has started §7for §6§l$track§7!")
                //clickable text
                component = component.append(Component.text(" §a§l[Click To Join]").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/race queue $track")))
                plr.sendMessage(component)
            }
        }
        //start timer
        object : BukkitRunnable() {
            var ticks : Int = 0
            var timer : Int = (timerInSeconds * 20).roundToInt()
            override fun run() {
                for (plr in Memory.server?.queues?.get(track)?.players!!) {
                    plr.sendActionBar("§d§l${Memory.server?.queues?.get(track)?.players!!.size}/${Memory.server?.raceData?.get(track)?.racePosistions?.size} | Starting in ${(timer-ticks)/20}s")
                }
                if (timerInSeconds > 25) {
                    //halfway message
                    if ((timer/2) == ticks) {
                        for (plr in Bukkit.getOnlinePlayers()) {
                            if (!Memory.getPlayerMemory(plr)?.inRace!! && !players.contains(plr)) {
                                var component: TextComponent = Component.text("§7Race on track §6§l$track §7is starting in §6§l" + (ticks/20) +"s")
                                component = component.append(Component.text(" §a§l[Click To Join]").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/race queue $track")))
                                plr.sendMessage(component)
                            }
                        }
                    }
                }else if (ticks == (timer - (10*20))) {
                    //10 second warning
                    if ((timer/2) == ticks) {
                        for (plr in Bukkit.getOnlinePlayers()) {
                            if (!Memory.getPlayerMemory(plr)?.inRace!! && !players.contains(plr)) {
                                var component: TextComponent = Component.text("§7Race on track §6§l$track §7is starting in §6§l" + (ticks/20) +"s")
                                component = component.append(Component.text(" §a§l[Click To Join]").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/race queue $track")))
                                plr.sendMessage(component)
                            }
                        }
                    }
                } else if (ticks >= (timer - (5*20))) {
                    if (ticks == (timer -5*20)) {
                        //5 second warning
                        for (plr in Bukkit.getOnlinePlayers()) {
                            if (!Memory.getPlayerMemory(plr)?.inRace!!) {
                                var component: TextComponent = Component.text("§7Race on track §6§l$track §7is starting in §6§l" + (ticks/20) +"s")
                                component = component.append(Component.text(" §a§l[Click To Join]").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/race queue $track")))
                                plr.sendMessage(component)
                            }
                        }
                    }else {
                        if (ticks % 20 == 0) {
                            // 4,3,2,1 second warnings
                            for (plr in Bukkit.getOnlinePlayers()) {
                                if (!Memory.getPlayerMemory(plr)?.inRace!!) {
                                    var component: TextComponent = Component.text("§7Race on track §6§l$track §7is starting in §6§l" + (ticks/20) +"s")
                                    component = component.append(Component.text(" §a§l[Click To Join]").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/race queue $track")))
                                    plr.sendMessage(component)
                                }
                            }
                        }
                    }
                }
                if (timer <= ticks || forceStart || Memory.server?.queues?.get(track)?.players!!.size >= Memory.server?.raceData?.get(track)?.racePosistions?.size!!) {
                    //timer ended

                    //come up with a unique id for the race
                    val id = UUID.randomUUID().toString()
                    //initialize race data
                    Memory.server?.races?.put(id, TempRaceData(RaceUtils(), loc, track))
                    //start race methods
                    Memory.server?.races?.get(id)?.raceUtil?.setupAndStartRace(track, players, id)
                    //remove this inst from serverData
                    Memory.server?.queues?.remove(track)
                    this.cancel()
                }
               ticks++
            }
        }.runTaskTimer(Main.instance!!,0,1)
        return this
    }
}