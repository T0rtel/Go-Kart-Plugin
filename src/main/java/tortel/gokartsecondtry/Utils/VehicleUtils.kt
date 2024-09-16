package tortel.gokartsecondtry.Utils

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main


object VehicleUtils {



    fun MoveForward(plr : Player){
        val ArmorStand = getplrVehicle(plr)!! //TODO: IMPROVE
        val x = ArmorStand.location.direction.x
        val z = ArmorStand.location.direction.z
        ArmorStand.velocity = Vector(x,-1.0,z + 0.1)
    }

    fun SteerRight(plr : Player){
        val ArmorStand = getplrVehicle(plr)!!


        ArmorStand.setRotation(ArmorStand.yaw + 15.0f, 0.0f)

    }

    fun SteerLeft(plr : Player){
        val ArmorStand = getplrVehicle(plr)!!


        ArmorStand.setRotation(ArmorStand.yaw - 15.0f, 0.0f)

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