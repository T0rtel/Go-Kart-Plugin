package tortel.gokartsecondtry.Utils



import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.AxisAngle4f
import org.joml.Vector3f
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.RaceUtils.RaceStarted
import tortel.gokartsecondtry.Utils.RaceUtils.playersInRace


object VehicleUtils {

    val PlayersAccelerating = mutableListOf<Player>()
    val PlayersVelocities = mutableMapOf<Player, Double>()
    val PlayerRotations = mutableMapOf<Player, Float>()
    val PlayerHorses = mutableMapOf<Player, Entity>()
    val PlayerArmorStands = mutableMapOf<Player, Entity>()

    val Acceleration = 0.05 //per tick
    val deceleration = 0.1
    val MaxSpeed = 1.0 // 1 * 20 = 20 blocks/second
    val MaxRotationSpeed = 5f
    val bouncePower = -2.5


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


        if (PlayersVelocities[plr]!! > 0.0){//PlayersAccelerating.contains(plr)
            val lastRot = PlayerRotations[plr]!!
            val RotationSpeed = MaxRotationSpeed
            val newRot = lastRot + RotationSpeed

           // ArmorStand.setRotation(newRot.toFloat(), 0.0f)
            PlayerRotations[plr] = newRot
        }

    }

    fun SteerLeft(plr : Player){

        /*
        if (PlayersAccelerating.contains(plr)){

            val RotationSpeed = MaxRotationSpeed

            ArmorStand.setRotation(ArmorStand.yaw - RotationSpeed, 0.0f)
        }

         */

        if (PlayersVelocities[plr]!! > 0.0){//PlayersAccelerating.contains(plr)
            val lastRot = PlayerRotations[plr]!!
            val RotationSpeed = MaxRotationSpeed
            val newRot = lastRot - RotationSpeed

            //ArmorStand.setRotation(newRot.toFloat(), 0.0f)
            PlayerRotations[plr] = newRot
        }
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


    /*
    fun spawnHorse(worldname : String, plr : Player){
        val Horse = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.HORSE) as Horse


        Horse.isInvulnerable = true
       // Horse.isInvisible = true
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
        Horse.setRotation(0.0f,0.0f)

       // Horse.addPassenger(plr)
        PlayerHorses[plr] = Horse
    }

     */
    /*
    fun spawnHorse(worldname: String, plr:Player){
        val location = Location(Bukkit.getWorld(worldname),plr.location.x, plr.location.y, plr.location.z)
        val horse: Horse = location.world?.spawnEntity(location, EntityType.HORSE) as Horse
        horse.isTamed = true
        horse.isSilent = true
        horse.isInvulnerable = true
        horse.age = -1000000 // Set the age

        // Set horse attributes
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.0

        // Create the first snowball passenger
        val snowball1: Snowball = location.world?.spawnEntity(location, EntityType.SNOWBALL) as Snowball
        val itemStack1 = ItemStack(Material.GUNPOWDER, 1)
        val meta1 = itemStack1.itemMeta
        meta1?.setCustomModelData(1) // Set CustomModelData
        itemStack1.itemMeta = meta1
        snowball1.setItem(itemStack1) // Set the item for the snowball
        snowball1.addPassenger(horse) // Add snowball as passenger of the horse

        // Create the second snowball passenger
        val snowball2: Snowball = location.world?.spawnEntity(location, EntityType.SNOWBALL) as Snowball
        val itemStack2 = ItemStack(Material.GUNPOWDER, 1)
        val meta2 = itemStack2.itemMeta
        meta2?.setCustomModelData(1) // Set CustomModelData
        itemStack2.itemMeta = meta2
        snowball2.setItem(itemStack2) // Set the item for the snowball
        snowball1.addPassenger(snowball2) // Add the second snowball as passenger of the first snowball

        // Finally, add the first snowball as a passenger of the horse
        horse.addPassenger(snowball1)

        Bukkit.getMobGoals().removeAllGoals(horse)
        PlayerHorses[plr] = horse
    }



     */


    fun spawnHorse(worldname : String, plr : Player){
        val Pig = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.PIG) as Pig
        Pig.isInvulnerable = true
        Pig.isInvisible = true
        Pig.setBaby()
        Pig.isSilent = true

        val Item = ItemStack(Material.YELLOW_DYE)
        val meta = Item.itemMeta
        meta.setCustomModelData(1)
        Item.setItemMeta(meta)

        Bukkit.getMobGoals().removeAllGoals(Pig)

        //ArmorStand.setItem(EquipmentSlot.HEAD, Item)

        //ArmorStand.setItemStack(ItemStack(Material.PAPER))
        //ArmorStand.setCanMove(true)

        //ArmorStand.teleport(Location(ArmorStand.world, ArmorStand.location.x,ArmorStand.location.y + 50,ArmorStand.location.z))

        Pig.addPassenger(plr)
        PlayerHorses[plr] = Pig
    }


    /*
    fun spawnAreaEffectCloud(worldname: String, plr : Player){
        val location = Location(Bukkit.getWorld(worldname),plr.location.x, plr.location.y, plr.location.z)
        val areaEffectCloud = location.world!!.spawn(location, AreaEffectCloud::class.java).apply {
            radius = 0.0f
            duration = 1000000000
        }

        // Create item displays (use ItemFrame for simplicity)
        val item1 = spawnCustomItemDisplay(location, Material.YELLOW_DYE, 1).apply {
            transformation = Transformation(
                Vector3f(0.0f, 0.5f, 0.0f),
                AxisAngle4f(0.0f,0.0f,0.0f,1.0f),
                Vector3f(2f, 2f, 2f),
                AxisAngle4f(0.0f, 0.0f, 0.0f, 1.0f)
            )
        }

        val item2 = spawnCustomItemDisplay(location, Material.YELLOW_DYE, 150, true).apply {
            transformation = Transformation(
                Vector3f(0.0f, 0.5f, 0.0f),
                AxisAngle4f(0.0f,0.0f,0.0f,1.0f),
                Vector3f(2f, 2f, 2f),
                AxisAngle4f(0.0f, 0.0f, 0.0f, 1.0f)
            )
        }  // glider (invisible)
        val item3 = spawnCustomItemDisplay(location, Material.YELLOW_DYE, 100).apply {
            transformation = Transformation(
                Vector3f(0.0f, 0.5f, 0.0f),
                AxisAngle4f(0.0f,0.0f,0.0f,1.0f),
                Vector3f(2f, 2f, 2f),
                AxisAngle4f(0.0f, 0.0f, 0.0f, 1.0f)
            )
        }  // item attachment

        // Summon the pig (which the player will ride)
        val pig = spawnPigWithProperties(location)

        // Set passengers: pig is the last passenger, and the AreaEffectCloud is at the base

        pig.addPassenger(plr)  // Attach item3 to pig
        item1.addPassenger(item2)
        item1.addPassenger(item3)  // Attach item2 to item3
        item1.addPassenger(pig)  // Attach item1 to item2
        areaEffectCloud.addPassenger(item1)  // Attach everything to AreaEffectCloud

        PlayerArmorStands[plr] = item1
    }



    private fun spawnCustomItemDisplay(location: Location, material: Material, customModelData: Int, invisible: Boolean = false): ItemDisplay {
        val itemDisplay = location.world!!.spawn(location, ItemDisplay::class.java).apply {
            setItemStack(ItemStack(material).apply {
                itemMeta = itemMeta!!.apply {
                    setCustomModelData(customModelData)
                }
            })
            //isVisible = !invisible  // Set visibility based on 'invisible' flag
        }
        return itemDisplay
    }

    // Helper to spawn and customize a pig
    private fun spawnPigWithProperties(location: Location): Pig {
        return location.world!!.spawn(location, Pig::class.java).apply {
            isSilent = true
            isInvulnerable = true
            setAI(false)
            addPotionEffect(org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 1, false, false))
            setBaby()
        }
    }
*/

    fun spawnArmorStand(worldname: String, plr : Player){
        val ArmorStand = Bukkit.getWorld(worldname)!!.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.ITEM_DISPLAY) as ItemDisplay
        ArmorStand.isInvulnerable = true

        ArmorStand.setNoPhysics(true)
        ArmorStand.setGravity(false)
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

        //ArmorStand.addPassenger(plr
        PlayerArmorStands[plr] = ArmorStand
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



                    // VELOCITY
                    Horse.velocity = Vector(Horse.location.direction.x, 0.5, Horse.location.direction.z).multiply(
                        Vector(Velocity, -5.0, Velocity)
                    )

                    // ROTATION
                    Horse.setRotation(plryawRotation, 0.0f)

                    // ARMOR STAND
                   // ArmorStand.removePassenger(plr)
                    //plr.teleport(ArmorStand)
                    ArmorStand.teleport(Horse.location.add(0.0,0.0,0.0)) // -1.7
                   // ArmorStand.addPassenger(plr)

                    //ArmorStand.teleport(Location(ArmorStand.world, ArmorStand.location.x,ArmorStand.location.y + 1,ArmorStand.location.z))
                    //println("Teleported ArmorStand to Horse at: ${Horse.location}")

                    // DECELERATION
                    if (!PlayersAccelerating.contains(plr) && PlayersVelocities[plr]!! > 0.0) {
                        //println("Decelerate plr")
                        DecreaseVel(plr)
                    }

                    PlayersAccelerating.remove(plr)

                    println("$Horse $ArmorStand $plryawRotation ${PlayersVelocities[plr]}")
                    if (!RaceStarted) {
                        this.cancel()
                        return
                    }
                }

            }
        }.runTaskTimer(Main.instance!!, 1, 1)
    }
}