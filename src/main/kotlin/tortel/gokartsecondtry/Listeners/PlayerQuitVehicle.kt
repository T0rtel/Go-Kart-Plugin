package tortel.gokartsecondtry.Listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent


class PlayerQuitVehicle : Listener {
    @EventHandler
    fun onquit(event : PlayerToggleSneakEvent){
        if (event.player.vehicle != null){
            event.isCancelled = true
        }

    }

}