package tortel.gokartsecondtry.Utils.Vehicle


import com.destroystokyo.paper.ParticleBuilder
import io.papermc.paper.entity.TeleportFlag
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.CONSTANTS

import tortel.gokartsecondtry.Vehicle.VehicleManager
import tortel.gokartsecondtry.data.Memory
import kotlin.math.cos
import kotlin.math.sin


object VehicleUtils {
    //TODO: DATA LEAK ALERT WAWAWAWAWA
    data class KeyState(
        var wPressed : Boolean = false,
        var aPressed : Boolean = false,
        var sPressed: Boolean = false,
        var dPressed : Boolean = false,
        var jumpPressed : Boolean = false
    )

    //driving
    private const val Acceleration = 0.05 //per tick
    private const val deceleration = 0.01
    private const val braking = 0.1
    const val MaxSpeed = 0.8 // 0.8 * 20 = 16 blocks/second
    const val MaxRotationSpeed = 3f // 3f
    const val bouncePower = -2.5
    const val GravityValue = -2.5
    const val boostPadAmount = 0.3
    const val boostItemAmount = 0.2
    //drifting
    private const val driftingforwardSpeed = 1 // Increased forward speed
    private const val driftingdiagonalOffset = 0.3 // Decreased rightward offset

    const val DriftingStartOffset = 15f // when we start drifting, rotate this much to start drifting
    private const val MaxDriftingRotationSpeed = 6f
    private const val MinDriftingRotationSpeed = 4f
    const val MaxDriftingSpeed = 0.5
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

    fun jump(plr : Player) {
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)
        object : BukkitRunnable() {
            var tick = 0
            override fun run() {
                if (tick == 5) {
                    Vehicle.VehicleHorse.velocity = Vector(Vehicle.VehicleHorse.velocity.x, 1.0, Vehicle.VehicleHorse.velocity.z)

                    this.cancel()

                }

                tick++
            }
        }.runTaskTimer(Main.instance!!,0,1)
    }

    fun miniKnockout(plr : Player) {
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)
        if (Memory.getPlayerMemory(plr)?.shields!! > 0) {
            Memory.getPlayerMemory(plr)?.shields = Memory.getPlayerMemory(plr)?.shields!! - 1
            plr.world.playSound(plr.location,Sound.BLOCK_ANVIL_LAND,1f,2f)

            return
        }
        Vehicle.stopped = true
        plr.world.playSound(plr.location, Sound.ENTITY_VILLAGER_HURT,1f,1f)
        plr.world.playSound(plr.location, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE,1f,2f)
        object : BukkitRunnable() {
            var ticks = 0
            val radius = 1.2
            val speed = 0.2
            var angle = 0.0
            override fun run() {
                if (ticks < 15) {
                    val a = Vehicle.VehicleHorse.location
                    a.yaw += ((360 / 15) * ticks)
                    Vehicle.VehicleHorse.teleport(a)
                    Vehicle.VehicleItemDisplays[1].setRotation(a.yaw, 0.0f)
                    Vehicle.VehicleItemDisplays[2].setRotation(a.yaw, 0.0f)
                }
                angle += speed
                val x = plr.location.x + radius * cos(angle)
                val z = plr.location.z + radius * sin(angle)
                val particleLocation = Location(plr.location.world, x, plr.eyeLocation.y+0.5, z)
                ParticleBuilder(org.bukkit.Particle.WAX_OFF).count(1).location(particleLocation).extra(0.5).spawn()
                if (ticks >= 15) {
                    Vehicle.stopped = false
                    this.cancel()
                }
                ticks++
            }

        }.runTaskTimer(Main.instance!!,0,1)
    }

    fun knockout(plr : Player) {
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)
        if (Memory.getPlayerMemory(plr)?.shields!! > 0) {
            Memory.getPlayerMemory(plr)?.shields = Memory.getPlayerMemory(plr)?.shields!! - 1
            plr.world.playSound(plr.location,Sound.BLOCK_ANVIL_LAND,1f,2f)

            return
        }
        Vehicle.stopped = true
        plr.world.playSound(plr.location, Sound.ENTITY_VILLAGER_HURT,1f,1f)
        plr.world.playSound(plr.location, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE,1f,2f)
        object : BukkitRunnable() {
            var ticks = 0
            val radius = 1.2
            val speed = 0.2
            var angle = 0.0
            override fun run() {
                if (ticks < 15) {
                    val a = Vehicle.VehicleHorse.location
                    a.yaw += ((360 / 15) * ticks)
                    Vehicle.VehicleItemDisplays[1].setRotation(a.yaw, 0.0f)
                    Vehicle.VehicleItemDisplays[2].setRotation(a.yaw, 0.0f)

                    Vehicle.VehicleHorse.teleport(a)
                }
                angle += speed
                val x = plr.location.x + radius * cos(angle)
                val z = plr.location.z + radius * sin(angle)
                val particleLocation = Location(plr.location.world, x, plr.eyeLocation.y+0.5, z)
                ParticleBuilder(org.bukkit.Particle.WAX_OFF).count(1).location(particleLocation).extra(0.5).spawn()
                if (ticks >= 35) {
                    Vehicle.stopped = false
                    this.cancel()
                }
                ticks++
            }

        }.runTaskTimer(Main.instance!!,0,1)
    }

    fun stop(plr : Player) {
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)
        Vehicle.stopped = true
    }
    fun unstop(plr : Player) {
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)
        Vehicle.stopped = false
    }
    fun boost(plr : Player) {
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)
        if (Memory.getPlayerMemory(plr)?.boosting!!) {
            return
        }
        Memory.getPlayerMemory(plr)?.boosting = true
        object : BukkitRunnable() {
            var tick = 0
            override fun run() {
                // set vehicle to max velocity plus boost value
                Vehicle.velocity = MaxSpeed + boostPadAmount
                if (tick == 40) {
                    Memory.getPlayerMemory(plr)?.boosting = false
                    this.cancel()

                }

                boostParticle(Vehicle.VehicleHorse)
                tick++
            }
        }.runTaskTimer(Main.instance!!, 0, 1)
    }
        fun boostItem(plr : Player) {
            val VehicleManager = getVehicleManager()!!
            val Vehicle = VehicleManager.getVehicle(plr)
            if (Memory.getPlayerMemory(plr)?.boosting!!) {
                return
            }
            Memory.getPlayerMemory(plr)?.boosting = true
            object : BukkitRunnable() {
                var tick = 0
                override fun run() {
                    // set vehicle to max velocity plus boost value
                    Vehicle.velocity = MaxSpeed + boostItemAmount
                    if (tick == 30) {
                        Memory.getPlayerMemory(plr)?.boosting = false
                        this.cancel()

                    }

                    boostParticle(Vehicle.VehicleHorse)
                    tick++
                }
            }.runTaskTimer(Main.instance!!,0,1)
    }
    fun boostParticle (Vehicle: Entity) {
        val particleLoc = Vehicle.location.add(-Vehicle.location.direction.x,0.0,-Vehicle.location.direction.z)
        val left: Vector = particleLoc.direction.clone().rotateAroundY(Math.toRadians(90.0)).multiply(0.6)
        val right: Vector = particleLoc.direction.clone().rotateAroundY(Math.toRadians(-90.0)).multiply(0.6)

        ParticleBuilder(org.bukkit.Particle.CAMPFIRE_COSY_SMOKE).location(particleLoc).count(4).offset(0.25,0.2,0.25).extra(0.0).spawn()
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
        if (Vehicle.isDrifting) return

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
        Horse.addScoreboardTag("kart")
        Horse.addScoreboardTag(plr.uniqueId.toString())
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
        println("drift 2")
        //changed to ParticleBuilder for performance and my sanity, and changed dust plume to two black dust particles ->
        ParticleBuilder(org.bukkit.Particle.BLOCK).location(particleLoc).count(25).offset(0.35,0.1,0.35).data(particleData).spawn()
        ParticleBuilder(org.bukkit.Particle.CAMPFIRE_COSY_SMOKE).location(particleLoc).count(4).offset(0.25,0.2,0.25).extra(0.0).spawn()
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

    fun applyAccVelocity(Velocity : Double , Horse : Entity, SpeedVector : Vector, plr : Player){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)
        if (Vehicle.stopped) {
            Horse.velocity = Vector(0.0,0.0,0.0)
            return;
        }
        Horse.velocity = Vector(Horse.location.direction.x, 0.5, Horse.location.direction.z).multiply(
            //Vector(Velocity, GravityValue, Velocity)
            SpeedVector
        )
    }
    fun applyDriftingOffset(plr : Player, Velocity: Double, Horse: Entity, SpeedVector : Vector){
        val VehicleManager = getVehicleManager()!!
        val Vehicle = VehicleManager.getVehicle(plr)


        val keys = Vehicle.playerKeyState
        if (!keys.dPressed && !keys.aPressed) {// player let go of both a and d key
            DecreaseVel(plr)
            applyAccVelocity(Velocity, Horse, SpeedVector, plr)
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
    fun startVehicleTicking(id : String){

        val VehicleManager = getVehicleManager()!!
        val plyrs = Memory.server?.races?.get(id)?.PlayersInRace!!
        object : BukkitRunnable() {
            override fun run() {
                if (!Memory.server?.races?.containsKey(id)!!) return
                for (plr in plyrs) {
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
                        drift(plr, Vehicle.playerKeyState)
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


                    //FORCES
                    if (Vehicle.isDrifting){ // && Velocity > (Velocity * 20/100)
                        applyDriftingOffset(plr, Velocity, Vehicle.VehicleHorse, SpeedVector)
                    }
                    if (!Vehicle.isDrifting){ // Vehicle.isAccelerating &&
                        applyAccVelocity(Velocity, Vehicle.VehicleHorse, SpeedVector, plr)

                    }//w

                    // DECELERATION
                    if (!Vehicle.isAccelerating && Vehicle.velocity > 0.0 && !Vehicle.isDrifting) {
                        //println("Decelerate plr")
                        DecreaseVel(plr)

                    }

                }

            }
        }.runTaskTimer(Main.instance!!, 0, 1)
    }
}