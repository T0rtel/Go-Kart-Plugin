package tortel.gokartsecondtry.Listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.KeyListener.KeyPressEvent
import tortel.gokartsecondtry.Utils.KeyListener.KeyReleaseEvent
import tortel.gokartsecondtry.Utils.VehicleUtils
import tortel.gokartsecondtry.Utils.VehicleUtils.ToggleAccelerate
import tortel.gokartsecondtry.Utils.VehicleUtils.toggleBrakes
import tortel.gokartsecondtry.Utils.VehicleUtils.toggleDrifting
import tortel.gokartsecondtry.Utils.VehicleUtils.toggleSteerLeft
import tortel.gokartsecondtry.Utils.VehicleUtils.toggleSteerRight

/*
class PlayerVehicleInput() : Listener{
    @EventHandler
    fun onMove(event : Player){
        Main.instance?.logger?.info("moved.")
    }
}

 */


class PlayerVehicleInput(plugin : Plugin) : PacketAdapter(
    Main.instance!!,
    ListenerPriority.NORMAL,  // Synchronous priority
    PacketType.Play.Client.STEER_VEHICLE) {


    override fun onPacketReceiving(event: PacketEvent){
        //detect if player moves //STEER_VEHICLE
        if (event.packetType == PacketType.Play.Client.STEER_VEHICLE){
            val plr = event.player

            if (!VehicleUtils.PlayersVelocities.contains(plr)){
                VehicleUtils.PlayersVelocities.put(plr, 0.0)
            }
            if (!VehicleUtils.PlayerRotations.contains(plr)){
                VehicleUtils.PlayerRotations.put(plr, 0.0f)
            }

            val forwardMovement = event.packet.float.read(1) // W/S
            val sidewardMovement = event.packet.float.read(0) // A/D

            val newS = forwardMovement < 0 // S key pressed when forwardMovement is negative
            val newW = forwardMovement > 0
            val newA = sidewardMovement > 0
            val newD = sidewardMovement < 0

            val keyState = VehicleUtils.playerKeyStates.getOrPut(plr) { VehicleUtils.KeyState() }

            //println(forwardMovement)
            // If S key state changes, fire the appropriate event
            if (keyState.wPressed != newW) {
                keyState.wPressed = newW

                if (newW) {

                    ToggleAccelerate(plr, true)
                } else {

                    ToggleAccelerate(plr, false)
                }
            }



            if (keyState.sPressed != newS) {
                keyState.sPressed = newS

                if (newS) {
                    if (sidewardMovement != 0.0f){
                        toggleDrifting(plr, sidewardMovement, true)
                    }else{
                        toggleBrakes(plr, true)
                    }

                } else {

                    toggleDrifting(plr, sidewardMovement, false)
                    toggleBrakes(plr, false)
                }
            }

            if (keyState.aPressed != newA) {
                keyState.aPressed = newA

                if (newA) {


                    toggleSteerLeft(plr, true)
                } else {

                    toggleSteerLeft(plr, false)
                }
            }

            if (keyState.dPressed != newD) {
                keyState.dPressed = newD

                if (newD) {

                    toggleSteerRight(plr, true)
                } else {

                    toggleSteerRight(plr, false)
                }
            }
            //if (frontAndBack == 0.0f) return
            //VehicleUtils.ApplyVelocity(plr, frontAndBack.toDouble(), sides.toDouble())
            if (forwardMovement == 0.98f){
               // println("W")
                //VehicleUtils.MoveForward(plr)
            }
            if (forwardMovement == -0.98f){
               // println("S")
                //VehicleUtils.drift(plr, sidewardMovement)

            }
            if (sidewardMovement == 0.98f){
               // println("A")
                //VehicleUtils.SteerLeft(plr)
            }
            if (sidewardMovement == -0.98f){
                //VehicleUtils.SteerRight(plr)
            }

        }
    }

}