package tortel.gokartsecondtry.Listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import tortel.gokartsecondtry.Utils.VehicleUtils

class PlayerJoinEvent : Listener {
    @EventHandler
    fun onjoin(event : PlayerJoinEvent){
        VehicleUtils.PlayersVelocities.put(event.player, 0.0)
        VehicleUtils.PlayerRotations.put(event.player, 0.0f)
    }
}