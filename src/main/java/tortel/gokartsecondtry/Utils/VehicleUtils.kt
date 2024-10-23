package tortel.gokartsecondtry.Utils


import com.comphenix.protocol.PacketType.Play
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.AxisAngle4f
import org.joml.Vector3f
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.RaceUtils.RaceStarted
import tortel.gokartsecondtry.Utils.RaceUtils.playersInRace
import kotlin.math.cos
import kotlin.math.sin


object VehicleUtils {

    val PlayersAccelerating = mutableListOf<Player>()
    val PlayersVelocities = mutableMapOf<Player, Double>()
    val PlayerRotations = mutableMapOf<Player, Float>()
    val PlayerHorses = mutableMapOf<Player, Entity>()
    val PlayerArmorStands = mutableMapOf<Player, Entity>()
    val PlayersDrifting = mutableListOf<Player>()
    val DriftingDir = mutableMapOf<Player, String>()
    val PlayersTickDrifting = mutableMapOf<Player, Int>()

    //driving
    val Acceleration = 0.05 //per tick
    val deceleration = 0.01
    val MaxSpeed = 0.8 // 0.8 * 20 = 16 blocks/second
    val MaxRotationSpeed = 2f
    val bouncePower = -2.5

    //drifting
    val DriftingStartOffset = 15f // when we start drifting, rotate this much to start drifting
    val MaxDriftingRotationSpeed = 5f
    val MinDriftingRotationSpeed = 3f
    val MaxDriftingSpeed = 0.5

    //TODO: ADD NEW ROTATION VARIABLE, CHANGE FROM vehicle.velocity TO vehicle.setdeltaspeed or some shi
    fun MoveForward(plr: Player){
        if (!PlayersAccelerating.contains(plr)){
            PlayersAccelerating.add(plr)
        }

        //val Vehicle = getplrVehicle(plr)!! //TODO: IMPROVE

        //val bounce = BounceBackIfWallAhead(plr)
        IncreaseVel(plr)
        //Vehicle.teleport(PlayerHorses[plr]!!)

        //Vehicle.velocity =  Vector(Vehicle.location.direction.x,1.0,Vehicle.location.direction.z).multiply(Vector(PlayersVelocities[plr]!!,-5.0,PlayersVelocities[plr]!!))
    }

    fun SteerRight(plr : Player){

        if (PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)){//PlayersAccelerating.contains(plr)
            val lastRot = PlayerRotations[plr]!!
            val RotationSpeed = MaxRotationSpeed
            val newRot = lastRot + RotationSpeed

           // ArmorStand.setRotation(newRot.toFloat(), 0.0f)
            PlayerRotations[plr] = newRot
        }

    }

    fun SteerLeft(plr : Player){

        if (PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)){//PlayersAccelerating.contains(plr)
            val lastRot = PlayerRotations[plr]!!
            val RotationSpeed = MaxRotationSpeed
            val newRot = lastRot - RotationSpeed

            //ArmorStand.setRotation(newRot.toFloat(), 0.0f)
            PlayerRotations[plr] = newRot
        }
    }

    fun drift(plr : Player, sides: Float){
        //TODO: add on drift offset and a bit of a jump (when we start give the player the bump,
        // dont give him a bump till he stops drifting then remove his tag or smth)
        if (PlayersDrifting.contains(plr) || sides == 0f) return
        PlayersDrifting.add(plr)
        var isfirstTick = false
        var lastRot = PlayerRotations[plr]!!

        if (!PlayersTickDrifting.contains(plr)){
            PlayersTickDrifting[plr] = 0
        }

        //drifting first tick(fired when he starts drifting)
        if (PlayersTickDrifting[plr] == 0){
            PlayersTickDrifting[plr] = 1
            //do first tick stuff
            isfirstTick = true
            println("just started drifting")

            if (sides == -0.98f){
                DriftingDir[plr] = "left"

                PlayerRotations[plr] = lastRot + DriftingStartOffset
                lastRot += DriftingStartOffset

                //PlayerRotations[plr] = PlayerRotations[plr]!! + MaxDriftingRotationSpeed
            }
            if (sides == 0.98f){
                DriftingDir[plr] = "right"

                PlayerRotations[plr] = lastRot - DriftingStartOffset
                lastRot -= DriftingStartOffset

                //PlayerRotations[plr] = PlayerRotations[plr]!! - MaxDriftingRotationSpeed
            }
        }

        if (DriftingDir[plr] == "left"){ // originally going left
            if (sides == -0.98f){ // is still going left, angle stays the same, increased

                PlayerRotations[plr] = lastRot + MaxDriftingRotationSpeed
            }
            if (sides == 0.98f){ // BUT if its going right (originally left) angle decreases

                PlayerRotations[plr] = lastRot - MinDriftingRotationSpeed
            }
        }

        if (DriftingDir[plr] == "right"){ // originally going right
            if (sides == 0.98f){ // is still going right, angle stays the same, increased

                PlayerRotations[plr] = lastRot + MaxDriftingRotationSpeed
            }
            if (sides == -0.98f){ // UT if its going left (originally left) angle decreases

                PlayerRotations[plr] = lastRot + MinDriftingRotationSpeed
            }
        }
        /*
        if (sides == -0.98f){
            DriftingDir[plr] = "left"

            PlayerRotations[plr] = lastRot + MaxDriftingRotationSpeed
        }
        if (sides == 0.98f){
            DriftingDir[plr] = "right"

            PlayerRotations[plr] = lastRot - MaxDriftingRotationSpeed
        }

         */
    }


    fun IncreaseVel(plr : Player){
        PlayersVelocities[plr]?.let { velocity ->
            if (velocity < MaxSpeed) {
                PlayersVelocities[plr] = (velocity + Acceleration).coerceAtMost(MaxSpeed)
                //println("$velocity")
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
        val Horse = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.HORSE) as Horse


        Horse.isInvulnerable = true
        Horse.isInvisible = true
        Horse.isCustomNameVisible = false
        Horse.setNoPhysics(false)
        Horse.setGravity(true)

        Horse.setBaby()
        Horse.isTamed = true
        Horse.owner = plr
        Horse.isSilent = true


        Horse.customName(Component.text(plr.name))
        //Horse.let { horse -> Bukkit.getMobGoals().removeAllGoals(horse)}
        Bukkit.getMobGoals().removeAllGoals(Horse)
        Horse.setRotation(plr.location.yaw,0.0f)

        //Horse.addPassenger(plr)
        PlayerHorses[plr] = Horse
    }



    fun spawnArmorStand(worldname: String, plr : Player){
        val ArmorStand = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay
        ArmorStand.isInvulnerable = true

        ArmorStand.setNoPhysics(true)
        ArmorStand.setGravity(false)
        ArmorStand.teleportDuration = 1

        ArmorStand.transformation = Transformation(
            Vector3f(0.0f, 0.5f, 0.0f),
            AxisAngle4f(0.0f,0.0f,0.0f,1.0f),
            Vector3f(-2f, 2f, -2f), // was originally 2f,2f,2f
            AxisAngle4f(0.0f, 0.0f, 0.0f, 1.0f)
        )


        //ArmorStand.isOp = true
        //ArmorStand.isMarker = true
        //ArmorStand.isSmall = true
       // ArmorStand.isInvisible = true

        //ArmorStand.setAI(false)

        val Item = ItemStack(Material.YELLOW_DYE)
        val meta = Item.itemMeta
        meta.setCustomModelData(1)
        Item.setItemMeta(meta)

        //Bukkit.getMobGoals().removeAllGoals(ArmorStand)

        ArmorStand.setItemStack(Item)
       // ArmorStand.addPassenger(plr)
        //ArmorStand.setItem(EquipmentSlot.HEAD, Item)

        //ArmorStand.addPassenger(plr)

        ArmorStand.addPassenger(plr)
        PlayerArmorStands[plr] = ArmorStand

        PlayerHorses[plr]!!.addPassenger(ArmorStand)
    }


    fun despawnHorse(plr : Player){
        if (PlayerHorses.contains(plr)){
            PlayerHorses[plr]!!.remove()

            PlayerHorses.remove(plr)
        }
    }

    fun despawnArmorStand(plr : Player){
        if (PlayerArmorStands.contains(plr)){
            PlayerArmorStands[plr]!!.remove()

            PlayerArmorStands.remove(plr)
        }
    }

    fun resetAllValues(){
        for (plr in playersInRace){
            PlayersVelocities[plr] = 0.0
            PlayerRotations[plr] = 0.0f
        }
    }

    fun startRaceTicking(){
        object : BukkitRunnable() {
            override fun run() {
                if (!RaceStarted) return
                for (plr in playersInRace) {

                    val Horse = PlayerHorses[plr] ?: continue

                    val ArmorStand = PlayerArmorStands[plr] ?: continue

                    val plryawRotation = PlayerRotations[plr] ?: continue

                    val Velocity = PlayersVelocities[plr] ?: continue

                    println("$PlayersDrifting $DriftingDir")

                    val forwardSpeed = 1 // Increased forward speed
                    val diagonalOffset = 0.3 // Decreased rightward offset

                    val radians = Math.toRadians(Horse.location.yaw.toDouble())

                    val leftdriftingx = -sin(radians) * forwardSpeed + cos(radians) * diagonalOffset
                    val leftdriftingz = cos(radians) * forwardSpeed + sin(radians) * diagonalOffset

                    val rightdriftingx = -sin(radians) * forwardSpeed  - cos(radians)* diagonalOffset
                    val rightdriftingz = cos(radians) * forwardSpeed - sin(radians)* diagonalOffset



                    // VELOCITY
                    if (PlayersDrifting.contains(plr) && Velocity > (Velocity * 20/100)){
                        if (DriftingDir[plr] == "left") {
                            Horse.velocity = Vector(leftdriftingx, 0.5, leftdriftingz).multiply(
                                Vector(Velocity, -5.0, Velocity)
                            )
                        }else if (DriftingDir[plr] == "right") {
                            Horse.velocity = Vector(rightdriftingx, 0.5, rightdriftingz).multiply(
                                Vector(Velocity, -5.0, Velocity)
                            )
                        }

                    }else{
                        Horse.velocity = Vector(Horse.location.direction.x, 0.5, Horse.location.direction.z).multiply(
                            Vector(Velocity, -5.0, Velocity)
                        )

                    }

                    // ROTATION
                    Horse.setRotation(plryawRotation, 0.0f)
                    ArmorStand.setRotation(plryawRotation, 0.0f)

                    // DECELERATION
                    if (!PlayersAccelerating.contains(plr) && PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)) {
                        //println("Decelerate plr")
                        DecreaseVel(plr)

                    }

                    if (!PlayersDrifting.contains(plr)){
                        PlayersTickDrifting[plr] = 0
                    }

                    PlayersAccelerating.remove(plr)
                    PlayersDrifting.remove(plr)
                    DriftingDir.remove(plr)

                    if (!RaceStarted) {
                        this.cancel()
                        return
                    }
                }

            }
        }.runTaskTimer(Main.instance!!, 1, 1)
    }
}