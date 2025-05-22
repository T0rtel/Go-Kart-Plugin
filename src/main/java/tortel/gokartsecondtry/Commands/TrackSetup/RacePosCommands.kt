package tortel.gokartsecondtry.Commands.TrackSetup

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.data.Memory
import tortel.gokartsecondtry.Utils.Race.RacingTracksConfigUtils


@Command("racepos")
@Description("Sets the racers positions when race starts.")
class   RacePosCommands {

    @Subcommand("debug")
    fun debug(sender: Player) {
        if (!sender.isOp) return
        val a : Int = Memory.server?.checkpoint?.get(sender.world.name)?.size!!
    Bukkit.broadcastMessage("$a")
        for (ch in Memory.server?.checkpoint?.get(sender.world.name)!!) {
            Bukkit.broadcastMessage(ch.number.toString())
        }


    }


    @Subcommand("add racer")
    fun addRacePos(sender: Player, racerNumber: Int) {
        if (!sender.isOp) return
        val loc = sender.location

        val replaced = RacingTracksConfigUtils.addRacePos(loc, racerNumber)
        if (replaced) {
            sender.sendMessage("Successfully Replaced $racerNumber in ${loc.world.name} Track!")
        } else {
            sender.sendMessage("Successfully Added $racerNumber To ${loc.world.name} Track!")
        }

    }

        @Suppress("DEPRECATION")
        @Subcommand("add finish")
    fun addStartPos(sender: Player) {
        if (!sender.isOp) return

        if (Memory.getPlayerMemory(sender)?.StartedStart == false) {
            Memory.getPlayerMemory(sender)?.StartedStart = true
            sender.sendMessage(Memory.getPlayerMemory(sender)?.StartedStart.toString())
            object : BukkitRunnable() {
                override fun run() {
                    val loc: Location? = sender.getTargetBlock(20)?.getLocation()
                    val height: Double? = Memory.getPlayerMemory(sender)?.length?.toDouble()
                    val loc1: Location?
                    val loc2: Location?
                    if (Memory.getPlayerMemory(sender)?.direction == true) {
                        loc1 = loc?.clone()?.add(0.5, 2.0, -height!!)
                        loc2 = loc?.clone()?.add(-0.5, 0.0, height!!)
                    } else {
                        loc1 = loc?.clone()?.add(-height!!, 2.0, 0.5)
                        loc2 = loc?.clone()?.add(height!!, 0.0, -0.5)
                    }
                    sender.sendActionBar("Run command again to confirm | Scroll to change length | Shift to change rotation")
                    createParticleBox(loc1!!, loc2!!)
                    createPointyArrow(loc!!, Memory.getPlayerMemory(sender)?.vector!!)
                    if (Memory.getPlayerMemory(sender)?.StartedStart == false) {
                        this.cancel()
                    }
                }
            }.runTaskTimer(Main.instance!!, 0, 5)
        } else {
            val loc = sender.getTargetBlock(20)?.getLocation()!!
            val replaced = RacingTracksConfigUtils.addStartPos(loc, sender)
            Memory.getPlayerMemory(sender)?.StartedStart = false
            if (replaced) {
                sender.sendMessage("Successfully Replaced Finish line in ${loc.world.name} Track!")
            } else {
                sender.sendMessage("Successfully Added Finish line To ${loc.world.name} Track!")
            }
        }
    }
    @Subcommand("add checkpoint")
    fun addCheckpointPos(sender: Player) {
        if (!sender.isOp) return

        if (Memory.getPlayerMemory(sender)?.StartedStart == false) {
            Memory.getPlayerMemory(sender)?.StartedStart = true
            sender.sendMessage(Memory.getPlayerMemory(sender)?.StartedStart.toString())
            object : BukkitRunnable() {
                override fun run() {
                    val loc: Location? = sender.getTargetBlock(20)?.getLocation()
                    val height: Double? = Memory.getPlayerMemory(sender)?.length?.toDouble()
                    val loc1: Location?
                    val loc2: Location?
                    if (Memory.getPlayerMemory(sender)?.direction == true) {
                        loc1 = loc?.clone()?.add(0.5, 2.0, -height!!)
                        loc2 = loc?.clone()?.add(-0.5, 0.0, height!!)
                    } else {
                        loc1 = loc?.clone()?.add(-height!!, 2.0, 0.5)
                        loc2 = loc?.clone()?.add(height!!, 0.0, -0.5)
                    }
                    sender.sendActionBar("Run command again to confirm | Scroll to change length | Shift to change rotation")
                    createParticleBox(loc1!!, loc2!!)
                    createPointyArrow(loc!!, Memory.getPlayerMemory(sender)?.vector!!)
                    if (Memory.getPlayerMemory(sender)?.StartedStart == false) {
                        this.cancel()
                    }
                }
            }.runTaskTimer(Main.instance!!, 0, 5)
        } else {
            val loc = sender.getTargetBlock(20)?.getLocation()!!
            val replaced = RacingTracksConfigUtils.addCheckpointPos(loc, sender)
            Memory.getPlayerMemory(sender)?.StartedStart = false
            var num : Int = 0
            for (ch in Memory.server?.checkpoint?.get(loc.world.name)!!) {
                num +=1
            }
            sender.sendMessage("Successfully Added Checkpoint ${num} line To ${loc.world.name} Track!")
        }
    }
    fun createPointyArrow(loc: Location , direction: Vector) {
        val startLocation = loc.add(0.0, 1.0, 0.0)
        val arrowLength = 1.5
        val arrowHeadLength = 0.5
        //Main line
        for (i in 0..(arrowLength * 10).toInt()) {
            val point = startLocation.clone().add(direction.clone().normalize().multiply(i / 10.0))
            ParticleBuilder(Particle.DUST).color(Color.RED).count(1).location(point).spawn()
          //      Bukkit.broadcastMessage(point.toString())
        }
        //arrow line
        val firstHeadDirection = direction.clone().rotateAroundY(Math.toRadians(225.0))
        for (i in 0..(arrowHeadLength * 10).toInt()) {
            val point = startLocation.clone().add(direction.clone().normalize().multiply(arrowLength)).add(firstHeadDirection.clone().normalize().multiply(i / 10.0))
            ParticleBuilder(Particle.DUST).color(Color.RED).count(1).location(point).spawn()
        }
        //other arrow line
        val secondHeadDirection = direction.clone().rotateAroundY(Math.toRadians(120.0))
        for (i in 0..(arrowHeadLength * 10).toInt()) {
            val point = startLocation.clone().add(direction.clone().normalize().multiply(arrowLength)).add(secondHeadDirection.clone().normalize().multiply(i / 10.0))
            ParticleBuilder(Particle.DUST).color(Color.RED).count(1).location(point).spawn()
        }
    }
    private fun createParticleBox(bottomLeft: Location, topRight: Location) {
        val particleBuilder = ParticleBuilder(Particle.FLAME).extra(0.0).count(1)

        val x1 = bottomLeft.block.x
        val y1 = bottomLeft.block.y
        val z1 = bottomLeft.block.z
        val x2 = topRight.block.x
        val y2 = topRight.block.y
        val z2 = topRight.block.z

        for (x in x1..x2) {
            particleBuilder.location(Location(bottomLeft.world, x.toDouble(), y1.toDouble(), z1.toDouble())).spawn()
            particleBuilder.location(Location(bottomLeft.world, x.toDouble(), y1.toDouble(), z2.toDouble())).spawn()
        }
        for (z in z1..z2) {
            particleBuilder.location(Location(bottomLeft.world, x1.toDouble(), y1.toDouble(), z.toDouble())).spawn()
            particleBuilder.location(Location(bottomLeft.world, x2.toDouble(), y1.toDouble(), z.toDouble())).spawn()
        }
        for (y in y1..y2) {
            particleBuilder.location(Location(bottomLeft.world, x1.toDouble(), y.toDouble(), z1.toDouble())).spawn()
            particleBuilder.location(Location(bottomLeft.world, x2.toDouble(), y.toDouble(), z1.toDouble())).spawn()
            particleBuilder.location(Location(bottomLeft.world, x1.toDouble(), y.toDouble(), z2.toDouble())).spawn()
            particleBuilder.location(Location(bottomLeft.world, x2.toDouble(), y.toDouble(), z2.toDouble())).spawn()
        }
    }


    @Subcommand("remove racer")
    fun removeRacePos(sender: Player, racerNumber: Int) {
        if (!sender.isOp) return
        val loc = sender.location

        RacingTracksConfigUtils.removeRacePos(loc, racerNumber)
        sender.sendMessage("Successfully Removed $racerNumber from ${loc.world.name} Track!")
    }

    @Subcommand("remove allracer")
    fun removeAllRacePos(sender: Player) {
        if (!sender.isOp) return
        val loc = sender.location

        RacingTracksConfigUtils.removeAllRacePos(loc)
        sender.sendMessage("Successfully Removed ${loc.world.name} from Tracks!")
    }
}