package tortel.gokartsecondtry.Listeners


import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

import tortel.gokartsecondtry.Utils.Garage.GarageVehicleUtils
import tortel.gokartsecondtry.Utils.Race.RaceUtils


class PlayerJoinLeaveEvent : Listener {
    @EventHandler
    fun onjoin(event : PlayerJoinEvent){
        RaceUtils.onPlayerEnterGame(event.player)
        GarageVehicleUtils.handlePlayerJoinServer(event.player)

    }

    @EventHandler
    fun onleave(event : PlayerQuitEvent){
        RaceUtils.onPlayerLeaveGame(event.player)
        GarageVehicleUtils.handlePlayerLeaveServer(event.player)
    }
}