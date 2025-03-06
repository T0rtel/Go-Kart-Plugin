package tortel.gokartsecondtry.Utils.Vehicle

import org.bukkit.entity.Player
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.DriftingStartOffset
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.MinSpeedToStartDrifting
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.getVehicleManager

object OnKeyToggle {

    fun ToggleAccelerate(plr: Player, accelerate : Boolean){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)


        Vehicle.isAccelerating = accelerate
    }

    fun toggleSteerLeft(plr : Player, steer : Boolean){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)

        if (steer){
            Vehicle.steering -= 1.0
        }else{
            Vehicle.steering += 1.0
        }
    }

    fun toggleSteerRight(plr : Player, steer : Boolean){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)


        if (steer){
            Vehicle.steering += 1.0
        }else{
            Vehicle.steering -= 1.0
        }
    }

    fun toggleBrakes(plr : Player, brake : Boolean){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)


        Vehicle.isBraking = brake
    }

    //TODO: GOING ORIGINALLY RIGHT THEN CLICK ON LEFT STOPS DRIFTING
    fun toggleDrifting(plr : Player, sides : Float, drift : Boolean){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)



        Vehicle.rotation.let { lastRot ->
            if (drift && sides != 0.0f && Vehicle.velocity > MinSpeedToStartDrifting){
                if (!Vehicle.isDrifting){
                    Vehicle.isDrifting = true
                    if (sides == -0.98f){
                        Vehicle.DriftingDir = "right"

                        Vehicle.rotation = lastRot + DriftingStartOffset

                        // println("started originally right")
                    }
                    if (sides == 0.98f){
                        Vehicle.DriftingDir = "left"

                        Vehicle.rotation = lastRot - DriftingStartOffset

                        // println("started originally left")
                    }
                }
            }else{
                if (Vehicle.isDrifting){
                    Vehicle.isDrifting = false
                    Vehicle.DriftingDir = ""
                    //println("stop drifting")
                }
            }
        }


    }
}