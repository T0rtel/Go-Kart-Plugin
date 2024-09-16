package tortel.gokartsecondtry.Listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerArmorStandManipulateEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.VehicleUtils
/*
class PlayerVehicleMove() : Listener{
    @EventHandler
    fun onMove(event : Player){
        Main.instance?.logger?.info("moved.")
    }
}

 */


class PlayerVehicleMove(plugin : Plugin) : PacketAdapter(params().plugin(plugin).types(PacketType.Play.Client.STEER_VEHICLE)) {

    override fun onPacketReceiving(event: PacketEvent){
        //detect if player moves //STEER_VEHICLE
        if (event.packetType == PacketType.Play.Client.STEER_VEHICLE){
            val plr = event.player

            val packet = event.packet
            val frontAndBack = event.packet.float.read(1)
            val sides = event.packet.float.read(0)

            //if (frontAndBack == 0.0f) return
            //VehicleUtils.ApplyVelocity(plr, frontAndBack.toDouble(), sides.toDouble())
            if (frontAndBack == 0.98f){
               // println("W")
                VehicleUtils.MoveForward(plr)
            }
            if (frontAndBack == -0.98f){
               // println("S")

            }
            if (sides == 0.98f){
               // println("A")
                VehicleUtils.SteerLeft(plr)
            }
            if (sides == -0.98f){
                VehicleUtils.SteerRight(plr)
            }

        }

    }

}