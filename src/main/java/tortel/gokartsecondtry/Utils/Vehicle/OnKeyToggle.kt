package tortel.gokartsecondtry.Utils.Vehicle

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
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
                        Bukkit.broadcastMessage("right")
                        //Crook\ Speed from drift
                        Main.instance?.let {
                            object : BukkitRunnable () {
                                var cycles = 2
                                var ticksPerCycle = 20
                                var speedMax: Double = 0.6
                                var speedAdded: Double = 0.0
                                var SpeedTicks = 20

                                var speedAddPerCycle: Double = speedMax/cycles
                                var currentCycles = 0
                                var i = 0
                                override fun run() {
                                if (i % ticksPerCycle == 0 &&  i < ticksPerCycle * cycles) {
                                    speedAdded += speedAddPerCycle
                                    currentCycles += 1
                                    plr.playSound(plr, Sound.BLOCK_TRIAL_SPAWNER_DETECT_PLAYER, 1f, 0.8f + (0.4f * currentCycles))

                                }
                                    if (!Vehicle.isDrifting) {
                                        Vehicle.velocity += speedAdded
                                        object : BukkitRunnable () {
                                            var i = 0
                                            override fun run() {
                                                if (i > SpeedTicks) {
                                                    this.cancel()
                                                    Vehicle.velocity -= speedAdded
                                                }
                                                i +=1
                                            }
                                        }.runTaskTimer(it,0,1)

                                        this.cancel()
                                    }
                                    i +=1
                                }
                            }.runTaskTimer(it, 0, 1)
                        }
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