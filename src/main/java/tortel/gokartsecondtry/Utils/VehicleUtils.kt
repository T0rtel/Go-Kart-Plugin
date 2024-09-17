package tortel.gokartsecondtry.Utils


import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector


object VehicleUtils {

    val PlayersAccelerating = mutableListOf<Player>()
    val PlayersVelocities = mutableMapOf<Player, Double>()

    val Acceleration = 0.001 //per tick
    val deceleration = 0.001
    val MaxSpeed = 0.05
    val MaxRotationSpeed = 15f


    fun MoveForward(plr : Player){
        if (!PlayersAccelerating.contains(plr)){
            PlayersAccelerating.add(plr)
        }

        val ArmorStand = getplrVehicle(plr)!! //TODO: IMPROVE
        val x = ArmorStand.location.direction.x
        val z = ArmorStand.location.direction.z
        val bounce = BounceBackIfWallAhead(plr)
        if (bounce){
            ArmorStand.velocity = Vector(x,-0.5,-5.0)
            PlayersAccelerating.remove(plr)
            return
        }
        IncreaseVel(plr)
        ArmorStand.velocity = Vector(x,-0.5,z + PlayersVelocities[plr]!!)

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

    fun DecreaseVel(plr: Player) {
        PlayersVelocities[plr]?.let { velocity ->
            if (velocity > 0) {
                PlayersVelocities[plr] = (velocity - deceleration).coerceAtLeast(0.0)
            }
        }
    }

    fun BounceBackIfWallAhead(plr : Player) : Boolean{
        val vehicle = getplrVehicle(plr) ?: return false
        if (vehicle.world.rayTraceBlocks(vehicle.location, vehicle.location.direction, 1.0) == null) return false
        val blockAhead = vehicle.world.rayTraceBlocks(vehicle.location, vehicle.location.direction, 1.0)!!.hitBlock ?: return false//.add(0.0,0.0,1.0).block.type
        val blockAboveBlockAhead = blockAhead.location.add(0.0,1.0,0.0).block.type

        //println("${blockAhead.type}, $blockAboveBlockAhead")

        if (blockAhead.isCollidable && blockAhead.isSolid){

            if (blockAboveBlockAhead.isCollidable && blockAboveBlockAhead.isSolid){
                println("HIT WALL")
                //vehicle.velocity = Vector(0.0, 0.0, -5.0)
                //PlayersVelocities[plr] = 0.0
                return true
            }else{
                println("STAIRS(MAYBE)")
                return false
            }

        }

        return false
    }
    fun getplrVehicle(plr : Player): Entity? {
        return if (plr.isInsideVehicle){
            plr.vehicle
        }else{
            null
        }

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