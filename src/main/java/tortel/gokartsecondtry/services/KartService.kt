package tortel.gokartsecondtry.services

import io.papermc.paper.entity.TeleportFlag
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.CONSTANTS
import tortel.gokartsecondtry.services.karts.DefaultKart
import kotlin.math.cos
import kotlin.math.sin

class KartService(
    private val player: Player,
) {

    data class KeyState(
        var wPressed : Boolean = false,
        var aPressed : Boolean = false,
        var sPressed : Boolean = false,
        var dPressed : Boolean = false
    )

    var PlayerAccelerating = false
    var PlayerBraking = false
    var PlayerSteeringLeft = false
    var PlayerSteeringRight = false
    var PlayerDrifting = false

    var PlayerVelocity = 0.0
    var PlayerRotation = 0.0F
    var PlayerDriftingDirrection = ""

    var PlayerKartObject : DefaultKart = DefaultKart()

    var kartTickingRunnable : BukkitRunnable? = null

    init {

        PlayerKartObject.spawnHorseEntity(player)
        PlayerKartObject.spawnDisplayEntity(player)
    }




    /*
        Runtime movement functions (Brake/Steer/Drift)
    */
    fun brake() {
        if (PlayerDrifting || PlayerVelocity > 0.0 ) return

        PlayerVelocity -= KartConstants.BREAKING
    }

    fun steerRight() {
        if (PlayerDrifting || PlayerVelocity > 0.0) return

        PlayerRotation = player.location.yaw + KartConstants.MAX_ROTATION_SPEED
    }

    fun steerLeft() {
        if (PlayerDrifting || PlayerVelocity > 0.0) return

        PlayerRotation = player.location.yaw - KartConstants.MAX_ROTATION_SPEED
    }

    fun drift() {
        val right = KeyState().dPressed
        val left = KeyState().aPressed

        if (!right && !left) return

        val lastRot = PlayerRotation
        if (PlayerSteeringRight) {
            if (right) {
                PlayerRotation = lastRot + KartConstants.MAX_DRIFTING_ROTATION_SPEED
            }
            if (left) {
                PlayerRotation = lastRot + KartConstants.MIN_DRIFTING_ROTATION_SPEED
            }
        }

        if (PlayerSteeringLeft) {
            if (left) {
                PlayerRotation = lastRot - KartConstants.MAX_DRIFTING_ROTATION_SPEED
            }
            if (right) {
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
    fun increaseVelocity() {

    }

    fun decreaseVelocity() {

    }

    fun applyVelocity() {
        val horse = PlayerKartObject.KartHorseEntity
        horse.velocity = Vector(horse.location.direction.x, 0.5, horse.location.direction.z).multiply(
            Vector(PlayerVelocity, KartConstants.GRAVITY, PlayerVelocity)
        )
    }

    fun applyRotation() {
        val horse = PlayerKartObject.KartHorseEntity
        val display = PlayerKartObject.KartDisplayEntity

        horse.setRotation(PlayerRotation, 0.0f)
        display.teleport(horse.location.add(CONSTANTS.KartOffset), TeleportFlag.EntityState.RETAIN_PASSENGERS)
    }

    fun applyDriftingOffset() {
        val keys = KeyState()
        val horse = PlayerKartObject.KartHorseEntity
        if (!keys.dPressed && !keys.aPressed) {
            decreaseVelocity()
            applyVelocity()
            return
        }
        val radians = Math.toRadians(horse.location.yaw.toDouble())

        val leftdriftingx = -sin(radians) * KartConstants.DRIFTING_FORWARD_SPEED + cos(radians) * KartConstants.DRIFTING_DIAGONAL_OFFSET
        val leftdriftingz = cos(radians) * KartConstants.DRIFTING_FORWARD_SPEED + sin(radians) * KartConstants.DRIFTING_DIAGONAL_OFFSET

        val rightdriftingx = -sin(radians) * KartConstants.DRIFTING_FORWARD_SPEED  - cos(radians) * KartConstants.DRIFTING_DIAGONAL_OFFSET
        val rightdriftingz = cos(radians) * KartConstants.DRIFTING_FORWARD_SPEED - sin(radians) * KartConstants.DRIFTING_DIAGONAL_OFFSET

        if (PlayerDriftingDirrection == "left") {

            horse.velocity = Vector(rightdriftingx, 0.5, rightdriftingz).multiply(
                PlayerVelocity
            )


        } else if (PlayerDriftingDirrection == "right") {

            horse.velocity = Vector(leftdriftingx, 0.5, leftdriftingz).multiply(
                PlayerVelocity
            )

        }
    }




    /*
        Runtime kart updates (ticks)
     */
    fun startKartTicking() {
        var kartTickingRunnable = object : BukkitRunnable() {
            override fun run() {

                // VELOCITY
                if (PlayerAccelerating) {
                    increaseVelocity()
                }
                if (PlayerBraking) {
                    brake()
                }
                if (PlayerDrifting) {
                    drift()
                }

                if (PlayerSteeringLeft) {
                    steerLeft()
                }
                if (PlayerSteeringRight) {
                    steerRight()
                }

                applyRotation()

                //FORCES
                if (PlayerDrifting) {
                    applyDriftingOffset()
                }

                if (!PlayerDrifting) {
                    applyVelocity()
                }

                // DECELERATION
                if (!PlayerAccelerating && PlayerVelocity > 0.0 && !PlayerDrifting) {
                    decreaseVelocity()
                }

            }
        }.runTaskTimer(Main.instance!!, 1, 1)
    }

    fun stopKartTicking() {
        kartTickingRunnable!!.cancel()
    }
}