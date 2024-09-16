package tortel.gokartsecondtry.Listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.plugin.Plugin
import tortel.gokartsecondtry.Utils.VehicleUtils
/*
class PlayerVehicleInput() : Listener{
    @EventHandler
    fun onMove(event : Player){
        Main.instance?.logger?.info("moved.")
    }
}

 */


class PlayerVehicleInput(plugin : Plugin) : PacketAdapter(params().plugin(plugin).types(PacketType.Play.Client.STEER_VEHICLE)) {

    override fun onPacketReceiving(event: PacketEvent){
        //detect if player moves //STEER_VEHICLE
        if (event.packetType == PacketType.Play.Client.STEER_VEHICLE){
            val plr = event.player

            if (!VehicleUtils.PlayersVelocities.contains(plr)){
                VehicleUtils.PlayersVelocities.put(plr, 0.0)
            }

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