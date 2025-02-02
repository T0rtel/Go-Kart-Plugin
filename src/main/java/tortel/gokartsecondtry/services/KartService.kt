package tortel.gokartsecondtry.services

import org.bukkit.entity.Player
import tortel.gokartsecondtry.Utils.VehicleUtils.KeyState
import tortel.gokartsecondtry.services.karts.DefaultKart

class KartService(
    private val player: Player,
) {

    var PlayerAccelerating = false
    var PlayerBraking = false
    var PlayerSteeringLeft = false
    var PlayerSteeringRight = false
    var PlayersDrifting = false

    var PlayersVelocity = 0.0
    var PlayerRotation = 0.0F
    var PlayerDriftingDirrection = null

    var PlayerKartObject = DefaultKart()




    /*
        Runtime movement functions (Brake/Steer/Drift)
    */
    fun brake() {
        if (PlayersDrifting || PlayersVelocity > 0.0 ) return

        PlayersVelocity -= KartConstants.BREAKING
    }

    fun steerRight() {
        if (PlayersDrifting || PlayersVelocity > 0.0) return

        PlayerRotation = player.location.yaw + KartConstants.MAX_ROTATION_SPEED
    }

    fun steerLeft() {
        if (PlayersDrifting || PlayersVelocity > 0.0) return

        PlayerRotation = player.location.yaw - KartConstants.MAX_ROTATION_SPEED
    }

    fun drift(KeyStates: KeyState){
        val right = KeyStates.dPressed
        val left = KeyStates.aPressed

        if (!right && !left) return

        //Particle(PlayerHorses[plr]!!)

        val lastRot = PlayerRotation
        if (PlayerSteeringRight){
            if (right){
                PlayerRotation = lastRot + KartConstants.MAX_DRIFTING_ROTATION_SPEED
            }
            if (left){
                PlayerRotation = lastRot + KartConstants.MIN_DRIFTING_ROTATION_SPEED
            }
        }

        if (PlayerSteeringLeft){
            if (left){
                PlayerRotation = lastRot - KartConstants.MAX_DRIFTING_ROTATION_SPEED
            }
            if (right){
                PlayerRotation = lastRot - KartConstants.MIN_DRIFTING_ROTATION_SPEED
            }
        }
    }




    /*
        Toggle movement functions
    */




    /*
        General functions
    */
    fun constructor() {
        return
    }
}