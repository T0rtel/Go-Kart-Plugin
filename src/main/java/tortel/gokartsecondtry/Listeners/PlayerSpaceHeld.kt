package tortel.gokartsecondtry.Listeners

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener


class PlayerSpaceHeld() : Listener {
    @EventHandler
    fun onjump(event : PlayerJumpEvent){
       // println("${event.player} JUMPED!")
    }
}
/*
class PlayerSpaceHeld(plugin : Plugin) : PacketAdapter(params().plugin(plugin).types(PacketType.Play.Server.K)) {

    override fun onPacketReceiving(event: PacketEvent){
        //detect if player moves //STEER_VEHICLE
        if (event.packetType == PacketType.Play.Client.STEER_VEHICLE){


        }

    }

}

 */