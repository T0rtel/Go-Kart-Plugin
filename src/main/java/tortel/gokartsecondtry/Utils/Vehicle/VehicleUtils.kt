package tortel.gokartsecondtry.Utils.Vehicle


import io.papermc.paper.entity.TeleportFlag
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.CONSTANTS
import tortel.gokartsecondtry.Utils.Race.RaceUtils.RaceStarted
import tortel.gokartsecondtry.Utils.Race.RaceUtils.PlayersInRace
import kotlin.math.cos
import kotlin.math.sin


object VehicleUtils {
    //TODO: DATA LEAK ALERT WAWAWAWAWA
    data class KeyState(
        var wPressed : Boolean = false,
        var aPressed : Boolean = false,
        var sPressed: Boolean = false,
        var dPressed : Boolean = false
    )

    val PlayersAccelerating = mutableListOf<Player>()
    val PlayersBraking = mutableListOf<Player>()
    val PlayersSteeringLeft = mutableListOf<Player>()
    val PlayersSteeringRight = mutableListOf<Player>()

    val PlayersVelocities = mutableMapOf<Player, Double>()
    val PlayerRotations = mutableMapOf<Player, Float>() // Player, Float
    val PlayerHorses = mutableMapOf<Player, Entity>()
    val PlayerItemDisplays = mutableMapOf<Player, List<Entity>>()
    val PlayersDrifting = mutableListOf<Player>()
    val DriftingDir = mutableMapOf<Player, String>()

    val playerKeyStates = mutableMapOf<Player, KeyState>()

    //driving
    val Acceleration = 0.05 //per tick
    val deceleration = 0.01
    val braking = 0.1
    val MaxSpeed = 0.8 // 0.8 * 20 = 16 blocks/second
    val MaxRotationSpeed = 3f // 3f
    val bouncePower = -2.5
    val GravityValue = -2.5

    //drifting
    val driftingforwardSpeed = 1 // Increased forward speed
    val driftingdiagonalOffset = 0.3 // Decreased rightward offset

    val DriftingStartOffset = 15f // when we start drifting, rotate this much to start drifting
    val MaxDriftingRotationSpeed = 6f
    val MinDriftingRotationSpeed = 4f
    val MaxDriftingSpeed = 0.5
    const val MinSpeedToStartDrifting = 0.03 // half the maxspeed

    //TODO: ADD NEW ROTATION VARIABLE, CHANGE FROM vehicle.velocity TO vehicle.setdeltaspeed or some shi

    fun Brake(plr : Player){
        if (PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)){//PlayersAccelerating.contains(plr)
            println("${plr.name} is using brakes!!")
            PlayersVelocities[plr]= PlayersVelocities[plr]!! - braking
        }
    }

    fun SteerRight(plr : Player){
        PlayerRotations[plr]?.let { plrYawRotation ->
            if (PlayersDrifting.contains(plr) || PlayersVelocities[plr]!! <= 0.0) return

            PlayerRotations[plr] =  plrYawRotation + MaxRotationSpeed
        }
        /*
        if (PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)){//PlayersAccelerating.contains(plr)
            val lastRot = PlayerRotations[plr]!!
            PlayerRotations[plr] =  lastRot + MaxRotationSpeed
            println("Delta angle : ${PlayerRotations[plr]!! - lastRot}")
        }
         */


    }

    fun SteerLeft(plr : Player){
        PlayerRotations[plr]?.let { plrYawRotation ->
            if (PlayersDrifting.contains(plr) || PlayersVelocities[plr]!! <= 0.0) return

            PlayerRotations[plr] =  plrYawRotation - MaxRotationSpeed
        }
        /*

        if (PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)){//PlayersAccelerating.contains(plr)
            val lastRot = PlayerRotations[plr]!!

            PlayerRotations[plr] = lastRot - MaxRotationSpeed
        }
         */

    }

    //TODO: FIX LETTING GO IF ANY OF DIRECTION KEYS(S/D) IT DOESNT STOP DRIFTINMG
    fun drift(plr : Player, KeyStates: KeyState){
        val originalDriftingDir = DriftingDir[plr]
        val right = KeyStates.dPressed
        val left = KeyStates.aPressed
        if (!right && !left) {
            //PlayersDrifting.remove(plr)
            //DriftingDir.remove(plr)
            return
        }
        if (DriftingDir[plr] != "right" && DriftingDir[plr] != "left") return

        DriftParticle(PlayerHorses[plr]!!)
        val lastRot = PlayerRotations[plr]!!
        //TODO: changing driftdir while drifting lags abit
        //originally right
        if (originalDriftingDir == "right"){
            if (right){

               // Bukkit.broadcastMessage("not Wide angle drift")
                PlayerRotations[plr] = lastRot + MaxDriftingRotationSpeed
            }
            if (left){

               // Bukkit.broadcastMessage("Wide angle drift")
                PlayerRotations[plr] = lastRot + MinDriftingRotationSpeed
            }
        }
        //originally left
        if (originalDriftingDir == "left"){
            if (left){

              //  Bukkit.broadcastMessage("not Wide angle drift")
                PlayerRotations[plr] = lastRot - MaxDriftingRotationSpeed
            }
            if (right){

               // Bukkit.broadcastMessage("Wide angle drift")
                PlayerRotations[plr] = lastRot - MinDriftingRotationSpeed
            }
        }

    }

    fun IncreaseVel(plr : Player){
        PlayersVelocities[plr]?.let { plrvelocity ->
            if (plrvelocity < MaxSpeed) {
                PlayersVelocities[plr] = (plrvelocity + Acceleration).coerceAtMost(MaxSpeed)
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

                vehicle.velocity =  Vector(x,0.0,z).multiply(Vector(bouncePower,-0.5, bouncePower))
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
    //TODO: FIX SHIFTING BEFORE GAME START LETS YOU OFF IT
    //TODO: FIX SHIFT + LEFT/RIGHT CLICK THE KART LETS YOU OFF IT
    fun spawnHorse(worldname : String, plr : Player){
        val Horse = Bukkit.getWorld(worldname)!!
            .spawnEntity(
                Location(plr.world,plr.location.x, plr.location.y, plr.location.z, plr.location.yaw, plr.location.pitch),
                EntityType.HORSE) as Horse
        Bukkit.getMobGoals().removeAllGoals(Horse)
        Horse.isInvulnerable = true
        Horse.isInvisible = true // TODO: MAKE IT INVIS
        Horse.isCustomNameVisible = false
        Horse.setNoPhysics(false)
        Horse.setGravity(true)
        Horse.isCollidable = false
        Horse.isPersistent = true
        Horse.removeWhenFarAway = false

        Horse.setBaby()
        Horse.isTamed = true
        Horse.owner = plr
        Horse.isSilent = true


        Horse.customName(Component.text(plr.name))
        //Horse.let { horse -> Bukkit.getMobGoals().removeAllGoals(horse)}
        Bukkit.getMobGoals().removeAllGoals(Horse)
        //Horse.setRotation(0f,0.0f)
        //Horse.addPassenger(plr)
        PlayerHorses[plr] = Horse
    }

    //TODO: KART COLORS
    fun getKartColor(){

    }

    fun spawnItemDisplay(worldname: String, plr : Player){
        //10051 metal stuff
        //10052 wheel
        //10055 main body
        val MainItemDisplay = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay

        MainItemDisplay.isInvulnerable = true
        MainItemDisplay.setNoPhysics(true)
        MainItemDisplay.setGravity(false)
        MainItemDisplay.teleportDuration = 1
        MainItemDisplay.customName(Component.text(PlayerHorses[plr]!!.uniqueId.toString()))
        MainItemDisplay.addScoreboardTag("needstotp")
        MainItemDisplay.addPassenger(plr)

        MainItemDisplay.setItemStack(CONSTANTS.getMainkart())

        val WheelItemDisplay = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay

        WheelItemDisplay.isInvulnerable = true
        WheelItemDisplay.setNoPhysics(true)
        WheelItemDisplay.setGravity(false)
        WheelItemDisplay.teleportDuration = 1
        WheelItemDisplay.customName(Component.text(PlayerHorses[plr]!!.uniqueId.toString()))
        WheelItemDisplay.addScoreboardTag("needstotp")
        WheelItemDisplay.addPassenger(plr)

        WheelItemDisplay.setItemStack(CONSTANTS.getKartWheel())

        val MetalsItemDisplay = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay

        MetalsItemDisplay.isInvulnerable = true
        MetalsItemDisplay.setNoPhysics(true)
        MetalsItemDisplay.setGravity(false)
        MetalsItemDisplay.teleportDuration = 1
        MetalsItemDisplay.customName(Component.text(PlayerHorses[plr]!!.uniqueId.toString()))
        MetalsItemDisplay.addScoreboardTag("needstotp")
        MetalsItemDisplay.addPassenger(plr)

        MetalsItemDisplay.setItemStack(CONSTANTS.getKartMetal())

        val displays = mutableListOf<Entity>(MainItemDisplay,WheelItemDisplay,MetalsItemDisplay)
        PlayerItemDisplays[plr] = displays

        MainItemDisplay.addPassenger(WheelItemDisplay)
        MainItemDisplay.addPassenger(MetalsItemDisplay)
    }


    fun Particle(Vehicle : Entity){
        val blockBelow = Vehicle.location.subtract(0.0, 1.0, 0.0).block
        val particleLoc = Vehicle.location.add(-Vehicle.location.direction.x,0.0,-Vehicle.location.direction.z)

        val particleData = blockBelow.blockData
        Vehicle.world.spawnParticle(org.bukkit.Particle.BLOCK, particleLoc, 25, 0.35, 0.1, 0.35, particleData)

        //plr.world.spawnParticle(org.bukkit.Particle.DUST_PLUME, Horse.location, 50, 0.0, 0.1 ,0.0)


    }
    //TODO: SMOKE PARTICLE WHEN DRIFTING
    fun DriftParticle(Vehicle : Entity){
        val blockBelow = Vehicle.location.subtract(0.0, 1.0, 0.0).block
        val particleLoc = Vehicle.location.add(-Vehicle.location.direction.x,0.0,-Vehicle.location.direction.z)

        val particleData = blockBelow.blockData
        Vehicle.world.spawnParticle(org.bukkit.Particle.BLOCK, particleLoc, 25, 0.35, 0.1, 0.35, particleData)
        Vehicle.world.spawnParticle(org.bukkit.Particle.DUST_PLUME, particleLoc, 1, 0.0, 0.0, 0.0)
        //plr.world.spawnParticle(org.bukkit.Particle.DUST_PLUME, Horse.location, 50, 0.0, 0.1 ,0.0)

    }

    fun despawnHorse(plr : Player){
        if (PlayerHorses.contains(plr)){
            PlayerHorses[plr]!!.remove()

            PlayerHorses.remove(plr)
        }
    }

    fun despawnItemDisplay(plr : Player){
        if (PlayerItemDisplays.contains(plr)){
            PlayerItemDisplays[plr]!!.forEach {
                it.remove()
            }

            PlayerItemDisplays.remove(plr)
        }
    }

    fun resetAllValues(){
       // println("BEFORE: $PlayersVelocities $PlayerRotations $PlayersSteeringLeft $PlayersSteeringRight $PlayersDrifting $PlayerHorses $PlayersBraking $PlayersAccelerating $PlayersInRace end of before")
        if (PlayersInRace.size == 0) return
        for (plr in PlayersInRace.toList()){
            println("removing values for ${plr.name} who is in PlayersInRace")
            PlayersVelocities[plr] = 0.0
            PlayerRotations[plr] = 0.0f
            //PlayersInRace.remove(plr)
        }
        PlayersInRace.clear()
       // println(" AFTER: $PlayersVelocities $PlayerRotations $PlayersSteeringLeft $PlayersSteeringRight $PlayersDrifting $PlayerHorses $PlayersBraking $PlayersAccelerating $PlayersInRace end of after")
    }

    fun resetValues(plr : Player){
       // println("BEFORE: $PlayersVelocities $PlayerRotations $PlayersSteeringLeft $PlayersDrifting $PlayerHorses $PlayersBraking $PlayersAccelerating $PlayersInRace \n\n")
        PlayersVelocities[plr] = 0.0
        PlayerRotations[plr] = 0.0f
        PlayersInRace.remove(plr)
       // println(" AFTER: $PlayersVelocities $PlayerRotations $PlayersSteeringLeft $PlayersDrifting $PlayerHorses $PlayersBraking $PlayersAccelerating $PlayersInRace")
    }

    fun applyAccVelocity(Velocity : Double , Horse : Entity, SpeedVector : Vector){

        Horse.velocity = Vector(Horse.location.direction.x, 0.5, Horse.location.direction.z).multiply(
            //Vector(Velocity, GravityValue, Velocity)
            SpeedVector
        )
    }
    fun applyDriftingOffset(plr : Player, Velocity: Double, Horse: Entity, SpeedVector : Vector){
        val keys = playerKeyStates.get(plr)
        if (!keys!!.dPressed && !keys.aPressed) {// player let go of both a and d key
            DecreaseVel(plr)
            applyAccVelocity(Velocity, Horse, SpeedVector)
            return
        }
        val radians = Math.toRadians(Horse.location.yaw.toDouble())

        val leftdriftingx = -sin(radians) * driftingforwardSpeed + cos(radians) * driftingdiagonalOffset
        val leftdriftingz = cos(radians) * driftingforwardSpeed + sin(radians) * driftingdiagonalOffset

        val rightdriftingx = -sin(radians) * driftingforwardSpeed  - cos(radians)* driftingdiagonalOffset
        val rightdriftingz = cos(radians) * driftingforwardSpeed - sin(radians)* driftingdiagonalOffset

        if (DriftingDir[plr] == "left") {

            Horse.velocity = Vector(rightdriftingx, 0.5, rightdriftingz).multiply(
                SpeedVector
            )


        }else if (DriftingDir[plr] == "right") {

            Horse.velocity = Vector(leftdriftingx, 0.5, leftdriftingz).multiply(
                SpeedVector
            )

        }
    }
    //TODO: FIX TURNING/DRIFTING DIFFER FROM PERSON TO PERSON (FASTER/SLOWER)
    fun startVehicleTicking(){
        object : BukkitRunnable() {
            override fun run() {
                if (!RaceStarted) return
                for (plr in PlayersInRace) {
                    val Horse = PlayerHorses[plr] ?: continue

                    val ItemDisplays = PlayerItemDisplays[plr] ?: continue

                    val plryawRotation = PlayerRotations[plr] ?: continue

                    // VELOCITY
                    if (PlayersAccelerating.contains(plr)){
                        IncreaseVel(plr)
                        Particle(Horse)
                    }
                    if (PlayersBraking.contains(plr)){
                        Brake(plr)
                    }
                    if (PlayersDrifting.contains(plr)){
                        //TODO: ADD DRIFTING
                        drift(plr, playerKeyStates.get(plr)!!)
                    }

                    if (PlayersSteeringLeft.contains(plr)){
                        SteerLeft(plr)
                    }
                    if (PlayersSteeringRight.contains(plr)){
                        SteerRight(plr)
                    }



                    val Velocity = PlayersVelocities[plr] ?: continue

                    if (Velocity < 0.0) { PlayersVelocities[plr] = 0.0 }


                    val SpeedVector = Vector(Velocity, GravityValue, Velocity)

                    // ROTATION

                    Horse.setRotation(plryawRotation, 0.0f)
                    ItemDisplays[1].setRotation(plryawRotation, 0.0f)
                    ItemDisplays[2].setRotation(plryawRotation, 0.0f)

                    ItemDisplays[0].teleport(Horse.location.add(CONSTANTS.KartOffset), TeleportFlag.EntityState.RETAIN_PASSENGERS)



                    //FORCES
                    if (PlayersDrifting.contains(plr) ){ // && Velocity > (Velocity * 20/100)
                        applyDriftingOffset(plr, Velocity, Horse, SpeedVector)
                    }
                    if (!PlayersDrifting.contains(plr)){ // PlayersAccelerating.contains(plr) &&
                        applyAccVelocity(Velocity, Horse, SpeedVector)
                    }//w

                    // DECELERATION
                    if (!PlayersAccelerating.contains(plr) && PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)) {
                        //println("Decelerate plr")
                        DecreaseVel(plr)

                    }


                    //PlayersAccelerating.remove(plr)
                    //PlayersDrifting.remove(plr)

                    /*
                    if (!RaceStarted) {
                        this.cancel()
                        return
                    }

                     */
                }

            }
        }.runTaskTimer(Main.instance!!, 1, 1)
    }
}