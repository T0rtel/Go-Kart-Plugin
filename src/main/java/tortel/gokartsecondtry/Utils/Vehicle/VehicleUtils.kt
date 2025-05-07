package tortel.gokartsecondtry.Utils.Vehicle


import com.destroystokyo.paper.ParticleBuilder
import io.papermc.paper.entity.TeleportFlag
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.CONSTANTS
import tortel.gokartsecondtry.Utils.Race.RaceUtils.PlayersInRace
import tortel.gokartsecondtry.Utils.Race.RaceUtils.RaceStarted
import tortel.gokartsecondtry.Vehicle.VehicleManager
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

    fun getVehicleManager(): VehicleManager? {
        return Main.vehicleManager
    }

    fun Brake(plr : Player){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)

        if (Vehicle.velocity > 0.0 && !Vehicle.isDrifting){//Vehicle.isAccelerating
            println("${plr.name} is using brakes!!")
            Vehicle.velocity -= braking
        }
        /*

        if (PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)){//Vehicle.isAccelerating
            println("${plr.name} is using brakes!!")
            PlayersVelocities[plr]= PlayersVelocities[plr]!! - braking
        }
         */

    }
    //TODO: FIX STOPPING AND STARTING RACE AGAIN MULTIPLIES THE STEERING :(
    fun SteerRight(plr : Player){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)


        Vehicle.rotation.let { plrYawRotation ->
            if (Vehicle.isDrifting || Vehicle.velocity <= 0.0) return

            Vehicle.rotation =  plrYawRotation + MaxRotationSpeed
        }
        /*
        PlayerRotations[plr]?.let { plrYawRotation ->
            if (PlayersDrifting.contains(plr) || PlayersVelocities[plr]!! <= 0.0) return

            PlayerRotations[plr] =  plrYawRotation + MaxRotationSpeed
        }
         */

        /*
        if (PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)){//Vehicle.isAccelerating
            val lastRot = PlayerRotations[plr]!!
            PlayerRotations[plr] =  lastRot + MaxRotationSpeed
            println("Delta angle : ${PlayerRotations[plr]!! - lastRot}")
        }
         */


    }

    fun SteerLeft(plr : Player){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)

        Vehicle.rotation.let { plrYawRotation ->
            if (Vehicle.isDrifting || Vehicle.velocity <= 0.0) return

            Vehicle.rotation =  plrYawRotation - MaxRotationSpeed
        }
        /*
        PlayerRotations[plr]?.let { plrYawRotation ->
            if (PlayersDrifting.contains(plr) || PlayersVelocities[plr]!! <= 0.0) return

            PlayerRotations[plr] =  plrYawRotation - MaxRotationSpeed
        }
         */

        /*

        if (PlayersVelocities[plr]!! > 0.0 && !PlayersDrifting.contains(plr)){//Vehicle.isAccelerating
            val lastRot = PlayerRotations[plr]!!

            PlayerRotations[plr] = lastRot - MaxRotationSpeed
        }
         */

    }

    fun drift(plr : Player, KeyStates: KeyState){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)

        val originalDriftingDir = Vehicle.DriftingDir
        val right = KeyStates.dPressed
        val left = KeyStates.aPressed
        if (!right && !left) {
            //PlayersDrifting.remove(plr)
            //DriftingDir.remove(plr)
            return
        }
        if (Vehicle.DriftingDir != "right" && Vehicle.DriftingDir != "left") return

        DriftParticle(Vehicle.VehicleHorse)
        //TODO: changing driftdir while drifting lags abit
        Vehicle.rotation.let { lastRot ->
            //originally right
            if (originalDriftingDir == "right"){
                if (right){

                    // Bukkit.broadcastMessage("not Wide angle drift")
                    Vehicle.rotation = lastRot + MaxDriftingRotationSpeed
                }
                if (left){

                    // Bukkit.broadcastMessage("Wide angle drift")
                    Vehicle.rotation = lastRot + MinDriftingRotationSpeed
                }
            }
            //originally left
            if (originalDriftingDir == "left"){
                if (left){

                    //  Bukkit.broadcastMessage("not Wide angle drift")
                    Vehicle.rotation = lastRot - MaxDriftingRotationSpeed
                }
                if (right){

                    // Bukkit.broadcastMessage("Wide angle drift")
                    Vehicle.rotation = lastRot - MinDriftingRotationSpeed
                }
            }

        }

    }

    fun IncreaseVel(plr : Player){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)

        Vehicle.velocity.let { plrvelocity ->
            if (plrvelocity < MaxSpeed) {
                Vehicle.velocity = (plrvelocity + Acceleration).coerceAtMost(MaxSpeed)
            }
        }

    }

    fun DecreaseVel(plr: Player) {
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)

        Vehicle.velocity.let { velocity ->
            if (velocity > 0) {
                Vehicle.velocity = (velocity - deceleration).coerceAtLeast(0.0)
            }
        }
    }

    //TODO: FIX SHIFTING BEFORE GAME START LETS YOU OFF IT
    //TODO: FIX SHIFT + LEFT/RIGHT CLICK THE KART LETS YOU OFF IT
    fun spawnHorse(worldname : String, plr : Player): Horse {
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

        Bukkit.getMobGoals().removeAllGoals(Horse)

        return Horse
    }

    //TODO: KART COLORS
    fun getKartColor(){

    }

    fun spawnItemDisplay(worldname: String, plr : Player): List<ItemDisplay> {
        val VehicleManager = getVehicleManager()
        val Vehicle = VehicleManager!!.getVehicle(plr)

        //10051 metal stuff
        //10052 wheel
        //10055 main body
        val MainItemDisplay = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay

        MainItemDisplay.isInvulnerable = true
        MainItemDisplay.setNoPhysics(true)
        MainItemDisplay.setGravity(false)
        MainItemDisplay.teleportDuration = 1
        MainItemDisplay.customName(Component.text(Vehicle.owner.uniqueId.toString()))
        MainItemDisplay.addScoreboardTag("needstotp")
        MainItemDisplay.addPassenger(plr)

        MainItemDisplay.setItemStack(CONSTANTS.getMainkart())

        val WheelItemDisplay = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay

        WheelItemDisplay.isInvulnerable = true
        WheelItemDisplay.setNoPhysics(true)
        WheelItemDisplay.setGravity(false)
        WheelItemDisplay.teleportDuration = 1
        WheelItemDisplay.customName(Component.text(Vehicle.owner.uniqueId.toString()))
        WheelItemDisplay.addScoreboardTag("needstotp")
        WheelItemDisplay.addPassenger(plr)

        WheelItemDisplay.setItemStack(CONSTANTS.getKartWheel())

        val MetalsItemDisplay = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay

        MetalsItemDisplay.isInvulnerable = true
        MetalsItemDisplay.setNoPhysics(true)
        MetalsItemDisplay.setGravity(false)
        MetalsItemDisplay.teleportDuration = 1
        MetalsItemDisplay.customName(Component.text(Vehicle.owner.uniqueId.toString()))
        MetalsItemDisplay.addScoreboardTag("needstotp")
        MetalsItemDisplay.addPassenger(plr)

        MetalsItemDisplay.setItemStack(CONSTANTS.getKartMetal())

        val displays = mutableListOf<Entity>(MainItemDisplay,WheelItemDisplay,MetalsItemDisplay)

        MainItemDisplay.addPassenger(WheelItemDisplay)
        MainItemDisplay.addPassenger(MetalsItemDisplay)

        return listOf(MainItemDisplay, WheelItemDisplay, MetalsItemDisplay)
    }


    fun Particle(Vehicle : Entity){
        val blockBelow = Vehicle.location.subtract(0.0, 1.0, 0.0).block
        val particleLoc = Vehicle.location.add(-Vehicle.location.direction.x,0.0,-Vehicle.location.direction.z)

        val particleData = blockBelow.blockData
        Vehicle.world.spawnParticle(org.bukkit.Particle.BLOCK, particleLoc, 25, 0.35, 0.1, 0.35, particleData)

        //plr.world.spawnParticle(org.bukkit.Particle.DUST_PLUME, Horse.location, 50, 0.0, 0.1 ,0.0)


    }

    fun DriftParticle(Vehicle : Entity){
        val blockBelow = Vehicle.location.subtract(0.0, 1.0, 0.0).block
        val particleLoc = Vehicle.location.add(-Vehicle.location.direction.x,0.0,-Vehicle.location.direction.z)
        val left: Vector = particleLoc.direction.clone().rotateAroundY(Math.toRadians(90.0)).multiply(0.6)
        val right: Vector = particleLoc.direction.clone().rotateAroundY(Math.toRadians(-90.0)).multiply(0.6)
        val particleData = blockBelow.blockData

        //changed to ParticleBuilder for performance and my sanity, and changed dust plume to two black dust particles ->
        ParticleBuilder(org.bukkit.Particle.BLOCK).location(particleLoc).count(25).offset(0.35,0.1,0.35).data(particleData).spawn()
        ParticleBuilder(org.bukkit.Particle.DUST).offset(0.0,0.1,0.0).color(org.bukkit.Color.BLACK).location(
            Location(
                particleLoc.world,
                particleLoc.x +(left.x),
                particleLoc.y,
                particleLoc.z +(left.x)
            )).count(2).spawn()
        ParticleBuilder(org.bukkit.Particle.DUST).offset(0.0,0.1,0.0).color(org.bukkit.Color.BLACK).location(
            Location(
                particleLoc.world,
                particleLoc.x +(right.x),
                particleLoc.y,
                particleLoc.z +(right.x)
            )).count(2).spawn()

        //Vehicle.world.spawnParticle(org.bukkit.Particle.BLOCK, particleLoc, 25, 0.35, 0.1, 0.35, particleData)
        //Vehicle.world.spawnParticle(org.bukkit.Particle.DUST_PLUME, particleLoc, 1, 0.0, 0.0, 0.0)
        //plr.world.spawnParticle(org.bukkit.Particle.DUST_PLUME, Horse.location, 50, 0.0, 0.1 ,0.0)

    }

    fun applyAccVelocity(Velocity : Double , Horse : Entity, SpeedVector : Vector){

        Horse.velocity = Vector(Horse.location.direction.x, 0.5, Horse.location.direction.z).multiply(
            //Vector(Velocity, GravityValue, Velocity)
            SpeedVector
        )
    }
    fun applyDriftingOffset(plr : Player, Velocity: Double, Horse: Entity, SpeedVector : Vector){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)


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

        if (Vehicle.DriftingDir == "left") {

            Horse.velocity = Vector(rightdriftingx, 0.5, rightdriftingz).multiply(
                SpeedVector
            )


        }else if (Vehicle.DriftingDir == "right") {

            Horse.velocity = Vector(leftdriftingx, 0.5, leftdriftingz).multiply(
                SpeedVector
            )

        }
    }
    //TODO: FIX TURNING/DRIFTING DIFFER FROM PERSON TO PERSON (FASTER/SLOWER)
    fun startVehicleTicking(){
        val VehicleManager = getVehicleManager()!!

        object : BukkitRunnable() {
            override fun run() {
                if (!RaceStarted) return
                for (plr in PlayersInRace) {
                    val Vehicle = VehicleManager.getVehicle(plr)

                    // VELOCITY
                    if (Vehicle.isAccelerating){
                        IncreaseVel(plr)
                        Particle(Vehicle.VehicleHorse)
                    }
                    if (Vehicle.isBraking){
                        Brake(plr)
                    }
                    if (Vehicle.isDrifting){
                        //TODO: ADD DRIFTING
                        drift(plr, playerKeyStates.get(plr)!!)
                    }

                    if (Vehicle.steering <= -1.0){
                        SteerLeft(plr)
                    }
                    if (Vehicle.steering >= 1.0){
                        SteerRight(plr)
                    }



                    val Velocity = Vehicle.velocity ?: continue

                    if (Velocity < 0.0) { Vehicle.velocity = 0.0 }


                    val SpeedVector = Vector(Velocity, GravityValue, Velocity)

                    // ROTATION

                    Vehicle.VehicleHorse.setRotation(Vehicle.rotation, 0.0f)
                    Vehicle.VehicleItemDisplays[1].setRotation(Vehicle.rotation, 0.0f)
                    Vehicle.VehicleItemDisplays[2].setRotation(Vehicle.rotation, 0.0f)

                    Vehicle.VehicleItemDisplays[0].teleport(Vehicle.VehicleHorse.location.add(CONSTANTS.KartOffset), TeleportFlag.EntityState.RETAIN_PASSENGERS)
                    println(Vehicle.steering)


                    //FORCES
                    if (Vehicle.isDrifting){ // && Velocity > (Velocity * 20/100)
                        applyDriftingOffset(plr, Velocity, Vehicle.VehicleHorse, SpeedVector)
                    }
                    if (!Vehicle.isDrifting){ // Vehicle.isAccelerating &&
                        applyAccVelocity(Velocity, Vehicle.VehicleHorse, SpeedVector)
                    }//w

                    // DECELERATION
                    if (!Vehicle.isAccelerating && Vehicle.velocity > 0.0 && !Vehicle.isDrifting) {
                        //println("Decelerate plr")
                        DecreaseVel(plr)

                    }

                }

            }
        }.runTaskTimer(Main.instance!!, 1, 1)
    }
}