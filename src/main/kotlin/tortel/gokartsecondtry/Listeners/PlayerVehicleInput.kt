package tortel.gokartsecondtry.Listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInputEvent
import org.bukkit.plugin.Plugin
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils
import tortel.gokartsecondtry.Utils.Vehicle.OnKeyToggle.ToggleAccelerate
import tortel.gokartsecondtry.Utils.Vehicle.OnKeyToggle.toggleBrakes
import tortel.gokartsecondtry.Utils.Vehicle.OnKeyToggle.toggleDrifting
import tortel.gokartsecondtry.Utils.Vehicle.OnKeyToggle.toggleSteerLeft
import tortel.gokartsecondtry.Utils.Vehicle.OnKeyToggle.toggleSteerRight
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.getVehicleManager


class PlayerVehicleInput(plugin : Plugin) : Listener {
    @EventHandler
    fun onInput(event : PlayerInputEvent){
        val plr = event.player
        val VehicleManager = getVehicleManager()!!
        if (plr.isInsideVehicle && VehicleManager.isEntityInVehicle(plr)) {
            val Vehicle = VehicleManager.getVehicle(plr)
            val input = event.input

            val newW = input.isForward
            val newS = input.isBackward
            val sidewardMovement = if (input.isRight) -0.98f else if (input.isLeft) 0.98f else 0.0f
            val newA = sidewardMovement > 0
            val newD = sidewardMovement < 0
            val jumpMovement = input.isJump
            val keyState = Vehicle.playerKeyState

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
//                    if (sidewardMovement != 0.0f){
//                        toggleDrifting(plr, sidewardMovement, true)
//                    }else{
//                        toggleBrakes(plr, true)
//                    }
                    toggleBrakes(plr, true)

                } else {

                    //toggleDrifting(plr, sidewardMovement, false)
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

            if (keyState.jumpPressed != jumpMovement) {
                keyState.jumpPressed = jumpMovement

                if (jumpMovement) {
                    toggleDrifting(plr, sidewardMovement, true)

                }else{
                    toggleDrifting(plr, sidewardMovement, false)
                }
            }
        }
    }
}




//class PlayerVehicleInput(plugin : Plugin) : PacketAdapter(
//    Main.instance,
//    ListenerPriority.NORMAL,  // Synchronous priority
//    PacketType.Play.Client.STEER_VEHICLE) {
//
//
//    override fun onPacketReceiving(event: PacketEvent){
//        //detect if player moves //STEER_VEHICLE
//        if (event.packetType == PacketType.Play.Client.STEER_VEHICLE){
//            val plr = event.player
//            val VehicleManager = getVehicleManager()!!
//            val Vehicle = VehicleManager.getVehicle(plr)
//
//            val forwardMovement = event.packet.float.read(1) // W/S
//            val sidewardMovement = event.packet.float.read(0) // A/D
//
//            val newS = forwardMovement < 0 // S key pressed when forwardMovement is negative
//            val newW = forwardMovement > 0
//            val newA = sidewardMovement > 0
//            val newD = sidewardMovement < 0
//
//            val keyState = VehicleUtils.playerKeyStates.getOrPut(plr) { VehicleUtils.KeyState() }
//
//            //println(forwardMovement)
//            // If S key state changes, fire the appropriate event
//            if (keyState.wPressed != newW) {
//                keyState.wPressed = newW
//
//                if (newW) {
//
//                    ToggleAccelerate(plr, true)
//                } else {
//
//                    ToggleAccelerate(plr, false)
//                }
//            }
//
//
//
//            if (keyState.sPressed != newS) {
//                keyState.sPressed = newS
//
//                if (newS) {
//                    if (sidewardMovement != 0.0f){
//                        toggleDrifting(plr, sidewardMovement, true)
//                    }else{
//                        toggleBrakes(plr, true)
//                    }
//
//                } else {
//
//                    toggleDrifting(plr, sidewardMovement, false)
//                    toggleBrakes(plr, false)
//                }
//            }
//
//            if (keyState.aPressed != newA) {
//                keyState.aPressed = newA
//                println("NEW A : $newA")
//                if (newA) {
//
//                    toggleSteerLeft(plr, true)
//                } else {
//
//                    toggleSteerLeft(plr, false)
//                }
//            }
//
//            if (keyState.dPressed != newD) {
//                keyState.dPressed = newD
//
//                if (newD) {
//
//                    toggleSteerRight(plr, true)
//                } else {
//
//                    toggleSteerRight(plr, false)
//                }
//            }
//            //if (frontAndBack == 0.0f) return
//            //VehicleUtils.ApplyVelocity(plr, frontAndBack.toDouble(), sides.toDouble())
//            if (forwardMovement == 0.98f){
//               // println("W")
//                //VehicleUtils.MoveForward(plr)
//            }
//            if (forwardMovement == -0.98f){
//               // println("S")
//                //VehicleUtils.drift(plr, sidewardMovement)
//
//            }
//            if (sidewardMovement == 0.98f){
//               // println("A")
//                //VehicleUtils.SteerLeft(plr)
//            }
//            if (sidewardMovement == -0.98f){
//                //VehicleUtils.SteerRight(plr)
//            }
//
//        }
//    }
//
//}