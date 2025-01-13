package tortel.gokartsecondtry.Commands



import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location

import org.bukkit.entity.*
import revxrsal.commands.annotation.Command
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import tortel.gokartsecondtry.Utils.VehicleUtils.PlayerItemDisplays
import tortel.gokartsecondtry.Utils.VehicleUtils.PlayerHorses
import tortel.gokartsecondtry.Utils.VehicleUtils.getMobPlayerIsRiding


class RideCommand  { // : CommandExecutor
    /*
    override fun onCommand(sender: CommandSender, command: Command, p2: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val plr = sender

        //TODO: ADD VEHICLE MODEL

        val Horse = plr.world.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z), EntityType.HORSE) as org.bukkit.entity.Horse


        Horse.isInvulnerable = true
        Horse.isInvisible = false
        Horse.isCustomNameVisible = false
        Horse.setNoPhysics(false)
        Horse.setGravity(true)
        //Vehicle.setAI(false)
        //Horse.setBaby()
        Horse.isTamed = true
        Horse.owner = sender
        Horse.isSilent = true

        //make the player sit on armor stand
        //Vehicle.addPassenger(sender)

        Horse.customName(Component.text(plr.name))
        getMobPlayerIsRiding(plr)!!.let { horse -> Bukkit.getMobGoals().removeAllGoals(horse)}
        Horse.setRotation(0.0f,0.0f)

        PlayerHorses[plr] = Horse

        val ItemDisplay = plr.world.spawnEntity(plr.location, EntityType.ARMOR_STAND) as ItemDisplay
        //ItemDisplay.isInvulnerable = true
        // ItemDisplay.isInvisible = false
        // ItemDisplay.isCustomNameVisible = false
        // ItemDisplay.setGravity(false)
        //ItemDisplay.isMarker = true
       // ItemDisplay.isSilent = true
        // ItemDisplay.isSmall = true

         ItemDisplay.addPassenger(plr)
        // ItemDisplay.customName(Component.text(plr.name))

        /*
        Atest.isInvulnerable = true
                    Atest.isInvisible = false
                    Atest.isCustomNameVisible = false
                    Atest.setGravity(false)
                    Atest.isMarker = true
                    Atest.isSilent = true
         */


        PlayerItemDisplays[plr] = ItemDisplay

        println(ItemDisplay)
        println(PlayerItemDisplays[plr])

        //Vehicle.velocity = Vehicle.velocity.add(Vector(0.0,10.0,0.0))
/*

        val Vehicleone = sender.world.spawnEntity(sender.location.add(Vector(0.0,0.0,3.0)), EntityType.HORSE) as org.bukkit.entity.Horse

        Vehicleone.isInvulnerable = true
        Vehicleone.isCustomNameVisible = false
        Vehicleone.setAI(false)
        Vehicleone.setNoPhysics(false)
        Vehicleone.setGravity(true)

        Vehicleone.setBaby()
        Vehicleone.isTamed = true
        //Vehicle.owner = sender

        //make the player sit on armor stand
        Vehicleone.addPassenger(sender)

        Vehicleone.velocity = Vehicleone.velocity.add(Vector(0.0,10.0,0.0))
        val nmsHorse = (Vehicleone as CraftEntity).handle


                val Vehicletwo = sender.world.spawnEntity(sender.location.add(Vector(0.0,0.0,6.0)), EntityType.HORSE) as org.bukkit.entity.Horse

                Vehicletwo.isInvulnerable = true
                Vehicletwo.isInvisible = false
                Vehicletwo.isCustomNameVisible = false
                Vehicletwo.setNoPhysics(true)
                Vehicletwo.setGravity(false)
                //Vehicle.setAI(false)
                // Vehicle.setBaby()
                Vehicletwo.isTamed = true
                //Vehicle.owner = sender

                //make the player sit on armor stand
                Vehicletwo.velocity = Vehicletwo.velocity.add(Vector(0.0,10.0,0.0))
                Vehicletwo.customName(Component.text(sender.name))

                val Vehiclethree = sender.world.spawnEntity(sender.location.add(Vector(0.0,10.0,9.0)), EntityType.HORSE) as org.bukkit.entity.Horse

                Vehiclethree.isInvulnerable = true
                Vehiclethree.isInvisible = false
                Vehiclethree.isCustomNameVisible = false
                Vehiclethree.setAI(false)
                Vehiclethree.setNoPhysics(false)
                Vehiclethree.setGravity(true)

                // Vehicle.setBaby()
                Vehiclethree.isTamed = true
                //Vehicle.owner = sender

                //make the player sit on armor stand
                Vehiclethree.velocity = Vehicletwo.velocity.add(Vector(0.0,10.0,0.0))
                Vehiclethree.customName(Component.text(sender.name))

                //val Vehiclefour = (level.world as CraftWorld).spaw



                fun spawnNMSNPC(location: Location) {
                    val craftPlayer = plr as CraftPlayer
                    val sp = craftPlayer.handle
                    val server = sp.server
                    val level = sp.level()
                    val serverlevel = sp.serverLevel() // the world essentially
                    val EntityClass = net.minecraft.world.entity.animal.horse.Horse(net.minecraft.world.entity.EntityType.HORSE, level)
                    //val npc = ServerEntity(serverlevel, EntityClass, 1, true)
                }



        fun spawnNMSHorse(): Horse {
            // Get the NMS world (WorldServer)
            val nmsWorld = (level.world as CraftWorld).handle

            // Create a new NMS horse using the correct class for newer versions of Minecraft
            val horse = Horse(net.minecraft.world.entity.EntityType.HORSE, nmsWorld)

            // Set the horse's position
            horse.setPos(plr.x, plr.y + 0.0, plr.z)

            // Optionally set some properties, such as custom name
            horse.isCustomNameVisible = false
            horse.isBaby = true
            //horse.isNoAi = true


            horse.customName = net.minecraft.network.chat.Component.literal(plr.name)

            // Add the horse entity to the world
            nmsWorld.addFreshEntity(horse)

            return horse

        }
        */
        /*
        fun adjustForSlabs(horse: Horse) {
            val blockInFront = horse.level().getBlockState(horse.blockPosition().offset(horse.lookAngle.x.toInt(), 0, horse.lookAngle.z.toInt()))

            // Check if the block in front is a slab or stair
            println("${horse.blockPosition().offset(Vec3i(horse.lookAngle.x.toInt(), 0 , horse.lookAngle.z.toInt()))}")
            if (blockInFront.bukkitMaterial.name.lowercase().contains("slab")) {
                // Apply a slight upward velocity
                println("SLAB")
                horse.setDeltaMovement(horse.deltaMovement.x, 0.5, horse.deltaMovement.z)
            }
        }

        fun moveHorse(horse: Horse) {
            // Set the horse's velocity based on the direction vector
            //horse.setDeltaMovement(direction.x, direction.y, direction.z)

            tortel.gokartsecondtry.Main.instance?.let {
                object : BukkitRunnable() {
                    override fun run() {
                        val direction =
                            Vector(VehicleUtils.PlayerRotations[plr]!!.first, 0.0, VehicleUtils.PlayerRotations[plr]!!.last).multiply(Vector(
                            VehicleUtils.PlayersVelocities[plr]!!,
                            0.0,
                            VehicleUtils.PlayersVelocities[plr]!!))



                    //adjustForSlabs(horse)
                    }
                }.runTaskTimer(it, 1, 1)
            }

        }

         */

        //val horse = spawnNMSHorse()
        /*
        player.startRiding(horse)
        horse.isTamed = true
        horse.ownerUUID = player.uuid

         */

        //moveHorse(horse)


        return false
    }
     */
   // @Command("ride")
    fun ride (sender : BukkitCommandActor){
        if (sender !is Player) return
        val plr = sender

        //TODO: ADD VEHICLE MODEL

        val Horse = plr.world.spawnEntity(Location(plr.world,plr.location.x, plr.location.y, plr.location.z, plr.location.yaw, 0.0f), EntityType.HORSE) as org.bukkit.entity.Horse


        Horse.isInvulnerable = true
        Horse.isInvisible = false
        Horse.isCustomNameVisible = false
        Horse.setNoPhysics(false)
        Horse.setGravity(true)
        //Vehicle.setAI(false)
        //Horse.setBaby()
        Horse.isTamed = true
        Horse.owner = sender
        Horse.isSilent = true

        //make the player sit on armor stand
        //Vehicle.addPassenger(sender)

        Horse.customName(Component.text(plr.name))
        getMobPlayerIsRiding(plr)!!.let { horse -> Bukkit.getMobGoals().removeAllGoals(horse)}
        Horse.setRotation(0.0f,0.0f)

        PlayerHorses[plr] = Horse

        val ItemDisplay = plr.world.spawnEntity(plr.location, EntityType.ARMOR_STAND) as ItemDisplay
        //ItemDisplay.isInvulnerable = true
        // ItemDisplay.isInvisible = false
        // ItemDisplay.isCustomNameVisible = false
        // ItemDisplay.setGravity(false)
        //ItemDisplay.isMarker = true
        // ItemDisplay.isSilent = true
        // ItemDisplay.isSmall = true

        ItemDisplay.addPassenger(plr)
        // ItemDisplay.customName(Component.text(plr.name))

        /*
        Atest.isInvulnerable = true
                    Atest.isInvisible = false
                    Atest.isCustomNameVisible = false
                    Atest.setGravity(false)
                    Atest.isMarker = true
                    Atest.isSilent = true
         */


        PlayerItemDisplays[plr] = ItemDisplay

        println(ItemDisplay)
        println(PlayerItemDisplays[plr])

        //Vehicle.velocity = Vehicle.velocity.add(Vector(0.0,10.0,0.0))
        /*

                val Vehicleone = sender.world.spawnEntity(sender.location.add(Vector(0.0,0.0,3.0)), EntityType.HORSE) as org.bukkit.entity.Horse

                Vehicleone.isInvulnerable = true
                Vehicleone.isCustomNameVisible = false
                Vehicleone.setAI(false)
                Vehicleone.setNoPhysics(false)
                Vehicleone.setGravity(true)

                Vehicleone.setBaby()
                Vehicleone.isTamed = true
                //Vehicle.owner = sender

                //make the player sit on armor stand
                Vehicleone.addPassenger(sender)

                Vehicleone.velocity = Vehicleone.velocity.add(Vector(0.0,10.0,0.0))
                val nmsHorse = (Vehicleone as CraftEntity).handle


                        val Vehicletwo = sender.world.spawnEntity(sender.location.add(Vector(0.0,0.0,6.0)), EntityType.HORSE) as org.bukkit.entity.Horse

                        Vehicletwo.isInvulnerable = true
                        Vehicletwo.isInvisible = false
                        Vehicletwo.isCustomNameVisible = false
                        Vehicletwo.setNoPhysics(true)
                        Vehicletwo.setGravity(false)
                        //Vehicle.setAI(false)
                        // Vehicle.setBaby()
                        Vehicletwo.isTamed = true
                        //Vehicle.owner = sender

                        //make the player sit on armor stand
                        Vehicletwo.velocity = Vehicletwo.velocity.add(Vector(0.0,10.0,0.0))
                        Vehicletwo.customName(Component.text(sender.name))

                        val Vehiclethree = sender.world.spawnEntity(sender.location.add(Vector(0.0,10.0,9.0)), EntityType.HORSE) as org.bukkit.entity.Horse

                        Vehiclethree.isInvulnerable = true
                        Vehiclethree.isInvisible = false
                        Vehiclethree.isCustomNameVisible = false
                        Vehiclethree.setAI(false)
                        Vehiclethree.setNoPhysics(false)
                        Vehiclethree.setGravity(true)

                        // Vehicle.setBaby()
                        Vehiclethree.isTamed = true
                        //Vehicle.owner = sender

                        //make the player sit on armor stand
                        Vehiclethree.velocity = Vehicletwo.velocity.add(Vector(0.0,10.0,0.0))
                        Vehiclethree.customName(Component.text(sender.name))

                        //val Vehiclefour = (level.world as CraftWorld).spaw



                        fun spawnNMSNPC(location: Location) {
                            val craftPlayer = plr as CraftPlayer
                            val sp = craftPlayer.handle
                            val server = sp.server
                            val level = sp.level()
                            val serverlevel = sp.serverLevel() // the world essentially
                            val EntityClass = net.minecraft.world.entity.animal.horse.Horse(net.minecraft.world.entity.EntityType.HORSE, level)
                            //val npc = ServerEntity(serverlevel, EntityClass, 1, true)
                        }



                fun spawnNMSHorse(): Horse {
                    // Get the NMS world (WorldServer)
                    val nmsWorld = (level.world as CraftWorld).handle

                    // Create a new NMS horse using the correct class for newer versions of Minecraft
                    val horse = Horse(net.minecraft.world.entity.EntityType.HORSE, nmsWorld)

                    // Set the horse's position
                    horse.setPos(plr.x, plr.y + 0.0, plr.z)

                    // Optionally set some properties, such as custom name
                    horse.isCustomNameVisible = false
                    horse.isBaby = true
                    //horse.isNoAi = true


                    horse.customName = net.minecraft.network.chat.Component.literal(plr.name)

                    // Add the horse entity to the world
                    nmsWorld.addFreshEntity(horse)

                    return horse

                }
                */
        /*
        fun adjustForSlabs(horse: Horse) {
            val blockInFront = horse.level().getBlockState(horse.blockPosition().offset(horse.lookAngle.x.toInt(), 0, horse.lookAngle.z.toInt()))

            // Check if the block in front is a slab or stair
            println("${horse.blockPosition().offset(Vec3i(horse.lookAngle.x.toInt(), 0 , horse.lookAngle.z.toInt()))}")
            if (blockInFront.bukkitMaterial.name.lowercase().contains("slab")) {
                // Apply a slight upward velocity
                println("SLAB")
                horse.setDeltaMovement(horse.deltaMovement.x, 0.5, horse.deltaMovement.z)
            }
        }

        fun moveHorse(horse: Horse) {
            // Set the horse's velocity based on the direction vector
            //horse.setDeltaMovement(direction.x, direction.y, direction.z)

            tortel.gokartsecondtry.Main.instance?.let {
                object : BukkitRunnable() {
                    override fun run() {
                        val direction =
                            Vector(VehicleUtils.PlayerRotations[plr]!!.first, 0.0, VehicleUtils.PlayerRotations[plr]!!.last).multiply(Vector(
                            VehicleUtils.PlayersVelocities[plr]!!,
                            0.0,
                            VehicleUtils.PlayersVelocities[plr]!!))



                    //adjustForSlabs(horse)
                    }
                }.runTaskTimer(it, 1, 1)
            }

        }

         */

        //val horse = spawnNMSHorse()
        /*
        player.startRiding(horse)
        horse.isTamed = true
        horse.ownerUUID = player.uuid

         */

        //moveHorse(horse)


        return
    }
}

