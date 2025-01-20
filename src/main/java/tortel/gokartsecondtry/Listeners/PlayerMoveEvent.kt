package tortel.gokartsecondtry.Listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import tortel.gokartsecondtry.Utils.RaceUtils
import tortel.gokartsecondtry.Utils.VehicleUtils

class PlayerMoveEvent : Listener {

    @EventHandler
    fun onPunch(event : PlayerMoveEvent){
        val plr = event.player
       // println("${event.player} ${event.eventName} ${event.player.isInsideVehicle} ${event.player.isSneaking}")
        if (RaceUtils.RaceStarted){
            if (!plr.isInsideVehicle && RaceUtils.PlayersInRace.contains(plr)){
                VehicleUtils.PlayerItemDisplays[plr]!!.addPassenger(plr)
                println("player got off vehicle")
            }

        }
    }

}