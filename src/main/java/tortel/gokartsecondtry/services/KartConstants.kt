package tortel.gokartsecondtry.services

object KartConstants {

    // Driving
    const val ACCELERATION = 0.05
    const val DECELERATION = 0.01
    const val BREAKING = 0.1
    const val MAX_SPEED = 0.8
    const val MAX_ROTATION_SPEED = 3f
    const val BOUNCE_POWER = -2.5
    const val GRAVITY = -2.5

    // Drifting
    const val DRIFTING_FORWARD_SPEED = 1
    const val DRIFTING_DIAGONAL_OFFSET = 0.3
    const val DRIFTING_START_OFFSET = 15f
    const val MAX_DRIFTING_ROTATION_SPEED = 6f
    const val MIN_DRIFTING_ROTATION_SPEED = 4f
    const val MAX_DRIFTING_SPEED = 0.5
    const val MIN_DRIFTING_START_SPEED = 0.03 // Speed required to start a drift
}