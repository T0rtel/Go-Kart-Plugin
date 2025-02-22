package tortel.gokartsecondtry.Listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import tortel.gokartsecondtry.Utils.Race.RaceUtils
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils

class PlayerMoveEvent : Listener {

    @EventHandler
    fun onPunch(event : PlayerMoveEvent){
        val plr = event.player
       // println("${event.player} ${event.eventName} ${event.player.isInsideVehicle} ${event.player.isSneaking}")
        if (RaceUtils.RaceStarted){
            if (!plr.isInsideVehicle && RaceUtils.PlayersInRace.contains(plr)){
                VehicleUtils.PlayerItemDisplays[plr]!![0].addPassenger(plr)
                println("player got off vehicle")
            }

        }
    }

}