package tortel.gokartsecondtry.Utils



import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main


object VehicleUtils {

    val PlayersAccelerating = mutableListOf<Player>()
    val PlayersVelocities = mutableMapOf<Player, Double>()
    val PlayerRotations = mutableMapOf<Player, List<Float>>()
    val PlayerHorses = mutableMapOf<Player, Entity>()
    val PlayerArmorStands = mutableMapOf<Player, ArmorStand>()

    val Acceleration = 0.05 //per tick
    val deceleration = 0.1
    val MaxSpeed = 1.0 // 1 * 20 = 20 blocks/second
    val MaxRotationSpeed = 7f
    val bouncePower = -2.5


    //TODO: ADD NEW ROTATION VARIABLE, CHANGE FROM vehicle.velocity TO vehicle.setdeltaspeed or some shi
    fun MoveForward(plr: Player, frontAndBack: Float, sides: Float){
        if (!PlayersAccelerating.contains(plr)){
            PlayersAccelerating.add(plr)
        }

        val Vehicle = getplrVehicle(plr)!! //TODO: IMPROVE

        //val bounce = BounceBackIfWallAhead(plr)
        IncreaseVel(plr)
        Vehicle.teleport(PlayerHorses[plr]!!)

        //Vehicle.velocity =  Vector(Vehicle.location.direction.x,1.0,Vehicle.location.direction.z).multiply(Vector(PlayersVelocities[plr]!!,-5.0,PlayersVelocities[plr]!!))
    }

    fun SteerRight(plr : Player){
        val ArmorStand = getplrVehicle(plr)!!

        if (PlayersVelocities[plr]!! > 0.0){//PlayersAccelerating.contains(plr)
            val lastRot = PlayerRotations[plr]!!
            val RotationSpeed = MaxRotationSpeed
            val newRot = lastRot.first + RotationSpeed

           // ArmorStand.setRotation(newRot.toFloat(), 0.0f)
            PlayerRotations[plr] = listOf(newRot,0.0f)
        }

    }

    fun SteerLeft(plr : Player){
        val ArmorStand = getplrVehicle(plr)!!
        /*
        if (PlayersAccelerating.contains(plr)){

            val RotationSpeed = MaxRotationSpeed

            ArmorStand.setRotation(ArmorStand.yaw - RotationSpeed, 0.0f)
        }

         */

        if (PlayersVelocities[plr]!! > 0.0){//PlayersAccelerating.contains(plr)
            val lastRot = PlayerRotations[plr]!!
            val RotationSpeed = MaxRotationSpeed
            val newRot = lastRot.first - RotationSpeed

            //ArmorStand.setRotation(newRot.toFloat(), 0.0f)
            PlayerRotations[plr] = listOf(newRot, 0.0f)
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
                //getplrVehicle(plr)!!.velocity =  Vector(getplrVehicle(plr)!!.location.direction.x,0.0,getplrVehicle(plr)!!.location.direction.z).multiply(Vector(PlayersVelocities[plr]!!,-0.5,PlayersVelocities[plr]!!))
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
               // println("STAIRS(MAYBE)")

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

    fun getMobPlayerIsRiding(plr: Player): Mob? {
        // Get the player's vehicle (the entity they are riding)
       // val nmsPlayer = (plr as CraftPlayer).handle
        val vehicle = plr.vehicle

        for (entity in plr.world.entities) {
            if (entity is Mob && entity.name == plr.name){
                //Bukkit.broadcastMessage("$entity")
                return entity
            }
        }

        return if (vehicle != null){
            vehicle as Mob
        }else{
            null
        }

    }

    fun spawnHorse(worldname : String, plr : Player){
        val Horse = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.HORSE) as org.bukkit.entity.Horse


        Horse.isInvulnerable = true
        Horse.isInvisible = false
        Horse.isCustomNameVisible = false
        Horse.setNoPhysics(false)
        Horse.setGravity(true)
        //Vehicle.setAI(false)
        //Horse.setBaby()
        Horse.isTamed = true
        Horse.owner = plr
        Horse.isSilent = true

        //make the player sit on armor stand
        //Vehicle.addPassenger(sender)

        Horse.customName(Component.text(plr.name))
        getMobPlayerIsRiding(plr)!!.let { horse -> Bukkit.getMobGoals().removeAllGoals(horse)}
        Horse.setRotation(0.0f,0.0f)

        PlayerHorses[plr] = Horse
    }

    fun spawnArmorStand(worldname : String,plr : Player){
        val ArmorStand = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.HORSE) as ArmorStand

        PlayerArmorStands[plr] = ArmorStand
    }
}