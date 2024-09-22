package tortel.gokartsecondtry.Utils


import net.minecraft.world.entity.animal.horse.Horse
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftHorse
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector


object VehicleUtils {

    val PlayersAccelerating = mutableListOf<Player>()
    val PlayersVelocities = mutableMapOf<Player, Double>()
    val PlayerRotations = mutableMapOf<Player, List<Double>>()

    val Acceleration = 0.01 //per tick
    val deceleration = 0.03
    val MaxSpeed = 1.0 // 1 * 20 = 20 blocks/second
    val MaxRotationSpeed = 10f
    val bouncePower = -2.5


    //TODO: ADD NEW ROTATION VARIABLE, CHANGE FROM vehicle.velocity TO vehicle.setdeltaspeed or some shi
    fun MoveForward(plr: Player, frontAndBack: Float, sides: Float){
        if (!PlayersAccelerating.contains(plr)){
            PlayersAccelerating.add(plr)
        }

        val Vehicle = getplrVehicle(plr)!! //TODO: IMPROVE
        val x = Vehicle.location.direction.x
        val z = Vehicle.location.direction.z

        val bounce = BounceBackIfWallAhead(plr)
        IncreaseVel(plr)
        Vehicle.velocity =  Vector(x,0.0,z).multiply(Vector(PlayersVelocities[plr]!!,0.0,PlayersVelocities[plr]!!))
        //Vehicle.teleport(Vehicle.location.add(Vector(x,0.0,z).multiply(Vector(PlayersVelocities[plr]!!,-5.0,PlayersVelocities[plr]!!))))
    }
//e
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
                getplrVehicle(plr)!!.velocity =  Vector(getplrVehicle(plr)!!.location.direction.x,0.0,getplrVehicle(plr)!!.location.direction.z).multiply(Vector(PlayersVelocities[plr]!!,-0.5,PlayersVelocities[plr]!!))
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
            val x = vehicle.location.direction.x
            val z = vehicle.location.direction.y
            if (blockAboveBlockAhead.isCollidable && blockAboveBlockAhead.isSolid){

                vehicle.velocity =  Vector(x,0.0,z).multiply(Vector(bouncePower,-0.5,bouncePower))
                PlayersAccelerating.remove(plr)
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

    fun convertBukkitToNMS(entity: Entity): net.minecraft.world.entity.Entity {
        return (entity as CraftEntity).handle
    }

    fun convertNMSToBukkit(nmsEntity: net.minecraft.world.entity.Entity): org.bukkit.entity.Entity {
        return Bukkit.getEntity(nmsEntity.uuid)!!
    }

}