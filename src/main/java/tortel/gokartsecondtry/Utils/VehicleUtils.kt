package tortel.gokartsecondtry.Utils


import io.papermc.paper.entity.TeleportFlag
import jdk.incubator.vector.VectorOperators
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import org.checkerframework.checker.units.qual.Speed
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.RaceUtils.RaceStarted
import tortel.gokartsecondtry.Utils.RaceUtils.PlayersInRace
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
    val PlayerItemDisplays = mutableMapOf<Player, Entity>()
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
    fun ToggleAccelerate(plr: Player, accelerate : Boolean){
        if (accelerate){
            if (!PlayersAccelerating.contains(plr)){
                PlayersAccelerating.add(plr)

            }
        }else{
            if (PlayersAccelerating.contains(plr)){
                PlayersAccelerating.remove(plr)

            }
        }
        /*
        if (!PlayersAccelerating.contains(plr)){
            PlayersAccelerating.add(plr)
        }

        //val Vehicle = getplrVehicle(plr)!! //TODO: IMPROVE

        //val bounce = BounceBackIfWallAhead(plr)
        IncreaseVel(plr)
        //Vehicle.teleport(PlayerHorses[plr]!!)


        //Vehicle.velocity =  Vector(Vehicle.location.direction.x,1.0,Vehicle.location.direction.z).multiply(Vector(PlayersVelocities[plr]!!,-5.0,PlayersVelocities[plr]!!))
         */
    }

    fun toggleSteerLeft(plr : Player, steer : Boolean){
        if (steer){
            if (!PlayersSteeringLeft.contains(plr)){
                PlayersSteeringLeft.add(plr)
            }
        }else{
            if (PlayersSteeringLeft.contains(plr)){
                PlayersSteeringLeft.remove(plr)
            }
        }
    }

    fun toggleSteerRight(plr : Player, steer : Boolean){
        if (steer){
            if (!PlayersSteeringRight.contains(plr)){
                PlayersSteeringRight.add(plr)
            }
        }else{
            if (PlayersSteeringRight.contains(plr)){
                PlayersSteeringRight.remove(plr)
            }
        }
    }

    fun toggleBrakes(plr : Player, brake : Boolean){
        if (brake){
            if (!PlayersBraking.contains(plr)){
                PlayersBraking.add(plr)
            }
        }else{
            if (PlayersBraking.contains(plr)){
                PlayersBraking.remove(plr)
            }
        }
    }
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
//TODO: GOING ORIGINALLY RIGHT THEN CLICK ON LEFT STOPS DRIFTING
    fun toggleDrifting(plr : Player, sides : Float, drift : Boolean){
        val lastRot = PlayerRotations[plr]!!

        if (drift && sides != 0.0f && PlayersVelocities[plr]!! > MinSpeedToStartDrifting){
            if (!PlayersDrifting.contains(plr)){
                PlayersDrifting.add(plr)
                if (sides == -0.98f){
                    DriftingDir[plr] = "right"

                    PlayerRotations[plr] = lastRot + DriftingStartOffset

                   // println("started originally right")
                }
                if (sides == 0.98f){
                    DriftingDir[plr] = "left"

                    PlayerRotations[plr] = lastRot - DriftingStartOffset

                   // println("started originally left")
                }
            }
        }else{
            if (PlayersDrifting.contains(plr)){
                PlayersDrifting.remove(plr)
                DriftingDir.remove(plr)
                //println("stop drifting")
            }
        }
    }
    //TODO: FIX LETTING GO IF ANY OF DIRECTION KEYS(S/D) IT DOESNT STOP DRIFTINMG
    //TODO: FIX RANDOM SPEED BUG, EVERY PLAYER HAS A RANDOM SPEED SOMETIMES FOR SOME REASON
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

        Particle(PlayerHorses[plr]!!)
        val lastRot = PlayerRotations[plr]!!

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
    /*
    //TODO: add on drift offset and a bit of a jump (when we start give the player the bump,
        // dont give him a bump till he stops drifting then remove his tag or smth)

        if (PlayersDrifting.contains(plr) || sides == 0f) return

        PlayersDrifting.add(plr)

        var lastRot = PlayerRotations[plr]!!

        if (!PlayersTickDrifting.contains(plr)){
            PlayersTickDrifting[plr] = 0
        }

        //drifting first tick(fired when he starts drifting)
        if (PlayersTickDrifting[plr] == 0){
            PlayersTickDrifting[plr] = 1
            //do first tick stuff

            if (sides == -0.98f){
                DriftingDir[plr] = "left"
                PlayersTickDrifting[plr] = 1

                PlayerRotations[plr] = lastRot + DriftingStartOffset
                lastRot += DriftingStartOffset

                println("started originally left")

                //PlayerRotations[plr] = PlayerRotations[plr]!! + MaxDriftingRotationSpeed
            }
            if (sides == 0.98f){
                DriftingDir[plr] = "right"
                PlayersTickDrifting[plr] = 1

                PlayerRotations[plr] = lastRot - DriftingStartOffset
                lastRot -= DriftingStartOffset

                println("started originally right")

                //PlayerRotations[plr] = PlayerRotations[plr]!! - MaxDriftingRotationSpeed
            }
        }

        if (DriftingDir[plr] == "left"){ // originally going left
            if (sides == -0.98f){ // is still going left, angle stays the same, increased

                PlayerRotations[plr] = lastRot + MaxDriftingRotationSpeed
            }
            if (sides == 0.98f){ // BUT if its going right (originally left) angle decreases

                PlayerRotations[plr] = lastRot + MinDriftingRotationSpeed
            }
        }

        if (DriftingDir[plr] == "right"){ // originally going right
            if (sides == 0.98f){ // is still going right, angle stays the same, increased

                PlayerRotations[plr] = lastRot - MaxDriftingRotationSpeed
            }
            if (sides == -0.98f){ // UT if its going left (originally left) angle decreases

                PlayerRotations[plr] = lastRot - MinDriftingRotationSpeed
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
     */


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

    fun spawnItemDisplay(worldname: String, plr : Player){
        val ItemDisplay = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay

        ItemDisplay.isInvulnerable = true
        ItemDisplay.setNoPhysics(true)
        ItemDisplay.setGravity(false)
        ItemDisplay.teleportDuration = 1
        ItemDisplay.customName(Component.text(PlayerHorses[plr]!!.uniqueId.toString()))
        ItemDisplay.addScoreboardTag("needstotp")
        ItemDisplay.addPassenger(plr)
        /*
        ItemDisplay.transformation = Transformation(
            Vector3f(0.0f, 0.5f, 0.0f),
            AxisAngle4f(0.0f,0.0f,0.0f,1.0f),
            Vector3f(-2f, 2f, -2f), // was originally 2f,2f,2f
            AxisAngle4f(0.0f, 0.0f, 0.0f, 1.0f)
        )

         */

        val Item = CONSTANTS.getMainkart()


        ItemDisplay.setItemStack(Item)

        //ItemDisplay.teleportAsync(plr.location.add(0.0,1.0,0.0))

        PlayerItemDisplays[plr] = ItemDisplay

        //PlayerHorses[plr]!!.addPassenger(ItemDisplay)
    }

    //TODO: SMOKE PARTICLE WHEN DRIFTING
    fun Particle(Vehicle : Entity){
        val blockBelow = Vehicle.location.subtract(0.0, 1.0, 0.0).block
        val particleLoc = Vehicle.location.add(-Vehicle.location.direction.x,0.0,-Vehicle.location.direction.z)

        val particleData = blockBelow.blockData
        Vehicle.world.spawnParticle(org.bukkit.Particle.BLOCK, particleLoc, 25, 0.35, 0.1, 0.35, particleData)

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
            PlayerItemDisplays[plr]!!.remove()

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
        val keys = VehicleUtils.playerKeyStates.get(plr)
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
    fun startRaceTicking(){
        object : BukkitRunnable() {
            override fun run() {
                if (!RaceStarted) return
                for (plr in PlayersInRace) {
                    val Horse = PlayerHorses[plr] ?: continue

                    val ItemDisplay = PlayerItemDisplays[plr] ?: continue

                    val plryawRotation = PlayerRotations[plr] ?: continue

                    // VELOCITY
                    if (PlayersAccelerating.contains(plr)){
                        IncreaseVel(plr)
                        Particle(Horse)
                        println("${plr.name} is accelerating")
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
                    ItemDisplay.teleport(Horse.location.add(CONSTANTS.KartOffset), TeleportFlag.EntityState.RETAIN_PASSENGERS)



                    //FORCES
                    if (PlayersDrifting.contains(plr) ){ // && Velocity > (Velocity * 20/100)
                        applyDriftingOffset(plr, Velocity, Horse, SpeedVector)
                        /*

                        val keys = VehicleUtils.playerKeyStates.get(plr)
                        if (!keys!!.dPressed && !keys.aPressed) {
                            Horse.velocity = Vector(Horse.location.direction.x, 0.5, Horse.location.direction.z).multiply(
                                //Vector(Velocity, GravityValue, Velocity)
                                SpeedVector
                            )
                            continue
                        }
                        val radians = Math.toRadians(Horse.location.yaw.toDouble())

                        val leftdriftingx = -sin(radians) * driftingforwardSpeed + cos(radians) * driftingdiagonalOffset
                        val leftdriftingz = cos(radians) * driftingforwardSpeed + sin(radians) * driftingdiagonalOffset

                        val rightdriftingx = -sin(radians) * driftingforwardSpeed  - cos(radians)* driftingdiagonalOffset
                        val rightdriftingz = cos(radians) * driftingforwardSpeed - sin(radians)* driftingdiagonalOffset

                        if (DriftingDir[plr] == "left") {

                            Horse.velocity = Vector(rightdriftingx, 0.5, rightdriftingz).multiply(
                                Vector(Velocity, GravityValue, Velocity)
                            )


                        }else if (DriftingDir[plr] == "right") {

                            Horse.velocity = Vector(leftdriftingx, 0.5, leftdriftingz).multiply(
                                Vector(Velocity, GravityValue, Velocity)
                            )

                        }
                         */

                    }
                    if (!PlayersDrifting.contains(plr)){ // PlayersAccelerating.contains(plr) &&
                        applyAccVelocity(Velocity, Horse, SpeedVector)
                        /*
                        Horse.velocity = Vector(Horse.location.direction.x, 0.5, Horse.location.direction.z).multiply(
                            //Vector(Velocity, GravityValue, Velocity)
                            SpeedVector
                        )
                         */



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