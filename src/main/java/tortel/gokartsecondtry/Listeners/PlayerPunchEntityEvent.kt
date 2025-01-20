package tortel.gokartsecondtry.Listeners

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

class PlayerPunchEntityEvent : org.bukkit.event.Listener {
    @EventHandler
    fun onInteract(event : PlayerInteractEntityEvent){
        val plr = event.player
        val entity = event.rightClicked
        if (entity.type == EntityType.INTERACTION){
            println(entity.scoreboardTags)
        }

    }
}