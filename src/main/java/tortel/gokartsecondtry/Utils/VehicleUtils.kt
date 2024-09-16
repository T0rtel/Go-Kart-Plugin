package tortel.gokartsecondtry.Utils

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector


object VehicleUtils {

    val PlayersAccelerating = mutableListOf<Player>()
    val PlayersDecelerating = listOf<Player>()
    val PlayersVelocities = mutableMapOf<Player, Double>()

    val Acceleration = 0.001 //per tick
    val deceleration = 0.005
    val MaxSpeed = 0.05
    val MaxRotationSpeed = 15f


    fun MoveForward(plr : Player){
        if (!PlayersAccelerating.contains(plr)){
            PlayersAccelerating.add(plr)
        }

        val ArmorStand = getplrVehicle(plr)!! //TODO: IMPROVE
        val x = ArmorStand.location.direction.x
        val z = ArmorStand.location.direction.z
        IncreaseVel(plr)
        ArmorStand.velocity = Vector(x,-0.5,z + PlayersVelocities[plr]!!)
        PlayersAccelerating.plus(plr)
    }

    fun SteerRight(plr : Player){
        val ArmorStand = getplrVehicle(plr)!!

        if (PlayersAccelerating.contains(plr)){

            val RotationSpeed = MaxRotationSpeed

            ArmorStand.setRotation(ArmorStand.yaw + RotationSpeed, 0.0f)
        }

    }

    fun SteerLeft(plr : Player){
        val ArmorStand = getplrVehicle(plr)!!

        if (PlayersAccelerating.contains(plr)){

            val RotationSpeed = MaxRotationSpeed

            ArmorStand.setRotation(ArmorStand.yaw - RotationSpeed, 0.0f)
        }
    }

    fun IncreaseVel(plr : Player){
        PlayersVelocities[plr]?.let { velocity ->
            if (velocity < MaxSpeed) {
                PlayersVelocities[plr] = (velocity + Acceleration).coerceAtMost(MaxSpeed)
                println("$velocity")
            }
        }

    }

    fun DecreaseVel(player: Player) {
        PlayersVelocities[player]?.let { velocity ->
            if (velocity > 0) {
                PlayersVelocities[player] = (velocity - deceleration).coerceAtLeast(0.0)
            }
        }
    }

    fun getplrVehicle(plr : Player): Entity? {
        return plr.vehicle
    }

    fun GetVehicleWishDirection(frontandback : Double, sides : Double) : Vector? {
        if (frontandback <= 0.0) return null
        val Dir = Vector(sides, 0.0, frontandback)
       // Main.instance?.logger?.info("$Dir")
        return Dir
    }

    fun ApplyVelocity(plr : Player, frontAndBack : Double, sides :Double){
        if (frontAndBack == 0.0) return
        //val wishdir = GetVehicleWishDirection(frontAndBack, sides)!!
        // val vel = wishdir.add(Vector(0.0,0.0,0.01))
        val ArmorStand = getplrVehicle(plr)

        if (ArmorStand != null) {
            ArmorStand.location.direction = Vector(sides,0.0,frontAndBack)
            println(ArmorStand.location.direction)
            println("${frontAndBack} AND $sides")
        }

        return
    }

}