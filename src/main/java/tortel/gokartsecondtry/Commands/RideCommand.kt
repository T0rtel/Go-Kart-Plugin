package tortel.gokartsecondtry.Commands


import net.kyori.adventure.text.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.animal.horse.Horse

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftEntity

import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Utils.VehicleUtils


class RideCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, p2: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        val plr = sender
        val craftPlayer = plr as CraftPlayer
        val player = craftPlayer.handle
        val server = player.server
        val level = player.level()
        val serverlevel = player.serverLevel() // the world essentially


        val Vehicle = sender.world.spawnEntity(sender.location, EntityType.ARMOR_STAND) as ArmorStand


        Vehicle.isInvulnerable = true
        Vehicle.isInvisible = false
        Vehicle.isCustomNameVisible = false
        Vehicle.setNoPhysics(false)
        Vehicle.setGravity(true)
        //Vehicle.setAI(false)
       // Vehicle.setBaby()
        //Vehicle.isTamed = true
        //Vehicle.owner = sender

        //make the player sit on armor stand
        //Vehicle.addPassenger(sender)

        Vehicle.customName(Component.text(sender.name))
        //Vehicle.velocity = Vehicle.velocity.add(Vector(0.0,10.0,0.0))


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

        /*
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

                 */

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


            horse.customName = net.minecraft.network.chat.Component.literal("NMS Horse")

            // Add the horse entity to the world
            nmsWorld.addFreshEntity(horse)

            return horse

        }
        fun moveHorse(horse: Horse) {
            // Set the horse's velocity based on the direction vector
            //horse.setDeltaMovement(direction.x, direction.y, direction.z)
            tortel.gokartsecondtry.Main.instance?.let {
                object : BukkitRunnable() {
                    override fun run() {
                        val direction = Vector(horse.direction.rotation.x.toDouble(), 0.0, horse.direction.rotation.z.toDouble()).multiply(Vector(
                            VehicleUtils.PlayersVelocities[plr]!!,
                            0.0,
                            VehicleUtils.PlayersVelocities[plr]!!))
                        horse.setDeltaMovement(direction.x, direction.y, direction.z)
                    }
                }.runTaskTimer(it, 1, 1)
            }

        }

        val horse = spawnNMSHorse()
        //horse.passengers.plus(plr)

        player.startRiding(horse)
        horse.isTamed = true
        horse.ownerUUID = player.uuid

        moveHorse(horse)


        return false
    }
}