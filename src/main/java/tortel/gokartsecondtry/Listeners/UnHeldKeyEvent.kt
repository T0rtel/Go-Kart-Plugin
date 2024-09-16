package tortel.gokartsecondtry.Listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.plugin.Plugin

class UnHeldKeyEvent(plugin : Plugin) : PacketAdapter(params().plugin(plugin).types(PacketType.Play.Client.VEHICLE_MOVE)) {

    override fun onPacketReceiving(event: PacketEvent){
        //detect if player moves //STEER_VEHICLE
        if (event.packetType == PacketType.Play.Client.VEHICLE_MOVE){
            println("Vehicle move")
        }

    }

}