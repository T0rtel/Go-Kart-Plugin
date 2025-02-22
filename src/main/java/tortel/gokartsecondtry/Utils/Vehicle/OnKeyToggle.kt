package tortel.gokartsecondtry.Utils.Vehicle

import org.bukkit.entity.Player
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.DriftingDir
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.DriftingStartOffset
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.MinSpeedToStartDrifting
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.PlayerRotations
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.PlayersAccelerating
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.PlayersBraking
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.PlayersDrifting
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.PlayersSteeringLeft
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.PlayersSteeringRight
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.PlayersVelocities

object OnKeyToggle {

    fun ToggleAccelerate(plr: Player, accelerate : Boolean){
        if (accelerate){
            if (!PlayersAccelerating.contains(plr)){
                PlayersAccelerating.add(plr)

            }
        }else{
            if (PlayersAccelerating.contains(plr)){
                PlayersAccelerating.remove(plr)

            }
        }
        /*
        if (!PlayersAccelerating.contains(plr)){
            PlayersAccelerating.add(plr)
        }

        //val Vehicle = getplrVehicle(plr)!! //TODO: IMPROVE

        //val bounce = BounceBackIfWallAhead(plr)
        IncreaseVel(plr)
        //Vehicle.teleport(PlayerHorses[plr]!!)


        //Vehicle.velocity =  Vector(Vehicle.location.direction.x,1.0,Vehicle.location.direction.z).multiply(Vector(PlayersVelocities[plr]!!,-5.0,PlayersVelocities[plr]!!))
         */
    }

    fun toggleSteerLeft(plr : Player, steer : Boolean){
        if (steer){
            if (!PlayersSteeringLeft.contains(plr)){
                PlayersSteeringLeft.add(plr)
            }
        }else{
            if (PlayersSteeringLeft.contains(plr)){
                PlayersSteeringLeft.remove(plr)
            }
        }
    }

    fun toggleSteerRight(plr : Player, steer : Boolean){
        if (steer){
            if (!PlayersSteeringRight.contains(plr)){
                PlayersSteeringRight.add(plr)
            }
        }else{
            if (PlayersSteeringRight.contains(plr)){
                PlayersSteeringRight.remove(plr)
            }
        }
    }

    fun toggleBrakes(plr : Player, brake : Boolean){
        if (brake){
            if (!PlayersBraking.contains(plr)){
                PlayersBraking.add(plr)
            }
        }else{
            if (PlayersBraking.contains(plr)){
                PlayersBraking.remove(plr)
            }
        }
    }

    //TODO: GOING ORIGINALLY RIGHT THEN CLICK ON LEFT STOPS DRIFTING
    fun toggleDrifting(plr : Player, sides : Float, drift : Boolean){
        val lastRot = PlayerRotations[plr]!!

        if (drift && sides != 0.0f && PlayersVelocities[plr]!! > MinSpeedToStartDrifting){
            if (!PlayersDrifting.contains(plr)){
                PlayersDrifting.add(plr)
                if (sides == -0.98f){
                    DriftingDir[plr] = "right"

                    PlayerRotations[plr] = lastRot + DriftingStartOffset

                    // println("started originally right")
                }
                if (sides == 0.98f){
                    DriftingDir[plr] = "left"

                    PlayerRotations[plr] = lastRot - DriftingStartOffset

                    // println("started originally left")
                }
            }
        }else{
            if (PlayersDrifting.contains(plr)){
                PlayersDrifting.remove(plr)
                DriftingDir.remove(plr)
                //println("stop drifting")
            }
        }
    }
}