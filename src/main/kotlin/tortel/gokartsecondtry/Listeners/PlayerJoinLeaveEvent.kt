package tortel.gokartsecondtry.Listeners


import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import tortel.gokartsecondtry.Main

import tortel.gokartsecondtry.Utils.Garage.GarageVehicleUtils
import tortel.gokartsecondtry.Utils.Race.RaceUtils
import tortel.gokartsecondtry.Vehicle.VehicleManager
import tortel.gokartsecondtry.data.Memory


class PlayerJoinLeaveEvent : Listener {
    @EventHandler
    fun onjoin(event : PlayerJoinEvent){

        GarageVehicleUtils.handlePlayerJoinServer(event.player)
    }

    @EventHandler
    fun onleave(event : PlayerQuitEvent){
        val plr = event.player
     if (!Memory.getPlayerMemory(plr)?.raceID?.isEmpty()!!) {
         Memory.server?.races?.get(Memory.getPlayerMemory(plr)?.raceID)?.raceUtil?.onPlayerLeaveGame(plr)
     }
        GarageVehicleUtils.handlePlayerLeaveServer(plr)
        plr.isInvisible = false
        plr.allowFlight = false
    }
}