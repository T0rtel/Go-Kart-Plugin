package tortel.gokartsecondtry.Utils

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils
import tortel.gokartsecondtry.data.Memory
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class items {

    fun speedb(plr : Player) {
        VehicleUtils.boostItem(plr)
    }

    fun missile(plr : Player) {
        val itemDisplay = plr.world.spawn(plr.eyeLocation.clone().add(plr.eyeLocation.direction), ItemDisplay::class.java)
        itemDisplay.setItemStack(ItemStack(Material.FIREWORK_ROCKET))




        val sortedPlayers =
            Memory.server?.races?.get(Memory.getPlayerMemory(plr)?.raceID)?.PlayersInRace?.sortedByDescending {
                Memory.getPlayerMemory(it)?.racePos!! + (Memory.getPlayerMemory(it)?.laps!! * 50)
            }
        val positions = mutableMapOf<Player, Int>()
        sortedPlayers?.forEachIndexed { index, player ->
            positions[player] = index + 1
        }
        if (plr == sortedPlayers?.first()) {
            //if player is first place, shoot rocket out

            object : BukkitRunnable() {
                var ticks = 0
                val driection = VehicleUtils.getVehicleManager()?.getVehicle(plr)?.VehicleHorse?.location?.direction!!
                var stopped = false
                override fun run() {

                    itemDisplay.teleport(itemDisplay.location.clone().add(driection.clone().multiply(0.7)))
                    //particle
                    ParticleBuilder(Particle.FLAME).count(2).location(itemDisplay.location).extra(0.0).offset(0.1, 0.1, 0.1)
                        .spawn()
                    ParticleBuilder(Particle.FLAME).count(2)
                        .location(itemDisplay.location.clone().add(driection.clone().multiply(-0.1))).extra(0.0).offset(0.1, 0.2, 0.1).spawn()
                    ParticleBuilder(Particle.SMOKE).count(3)
                        .location(itemDisplay.location.clone().add(driection.clone().multiply(-0.3))).extra(0.0).offset(0.1, 0.2, 0.1).spawn()
                    ParticleBuilder(Particle.SMOKE).count(3)
                        .location(itemDisplay.location.clone().add(driection.clone().multiply(-0.2))).extra(0.0).offset(0.1, 0.2, 0.1).spawn()
                    ParticleBuilder(Particle.SMOKE).count(3)
                        .location(itemDisplay.location.clone().add(driection.clone().multiply(-0.4))).extra(0.0).offset(0.1, 0.2, 0.1).spawn()
                    itemDisplay.world.playSound(itemDisplay.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.5f, 2f)

                    for (a in itemDisplay.getNearbyEntities(0.2, 0.2, 0.2)) {
                        if (a.type == EntityType.HORSE) {
                            for (a2 in a.scoreboardTags) {
                                if (a2 != "kart") {
                                    val plr2 = Bukkit.getPlayer(UUID.fromString(a2))
                                    VehicleUtils.knockout(plr2!!)
                                    stopped = true
                                    break;
                                }
                            }
                        } else if (a.type == EntityType.PLAYER) {
                            if (Memory.getPlayerMemory(a as Player)?.inRace!!) {
                                if (a != plr) {
                                    VehicleUtils.knockout(a)
                                    stopped = true
                                    break;
                                }
                            }
                        }
                    }
                    if (ticks == 120 || itemDisplay.location.block.isSolid || stopped) {

                        ParticleBuilder(Particle.EXPLOSION).count(1).location(itemDisplay.location)
                        itemDisplay.world.playSound(itemDisplay.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                        itemDisplay.remove()


                        this.cancel()
                    }

                    ticks++

                }

            }.runTaskTimer(Main.instance!!, 0, 1)
        } else {
            //if player isn't first place have rocket go towards next placer
            val target = sortedPlayers?.get(positions.get(plr)!! + 1)

            object : BukkitRunnable() {
                var ticks = 0
                var stopped = false
                override fun run() {

                    val direction = target?.location?.toVector()?.subtract(itemDisplay.location.toVector())?.normalize()
                target?.sendTitle("","§c⚠")

                    itemDisplay.teleport(itemDisplay.location.clone().add(direction!!.multiply(0.7)))
                    //particle
                    ParticleBuilder(Particle.FLAME).count(2).location(itemDisplay.location).extra(0.0).offset(0.1, 0.1, 0.1)
                        .spawn()
                    ParticleBuilder(Particle.FLAME).count(2)
                        .location(itemDisplay.location.clone().add(direction.multiply(-0.1))).extra(0.0).offset(0.1, 0.2, 0.1).spawn()
                    ParticleBuilder(Particle.SMOKE).count(3)
                        .location(itemDisplay.location.clone().add(direction.multiply(-0.3))).extra(0.0).offset(0.1, 0.2, 0.1).spawn()
                    ParticleBuilder(Particle.SMOKE).count(3)
                        .location(itemDisplay.location.clone().add(direction.multiply(-0.2))).extra(0.0).offset(0.1, 0.2, 0.1).spawn()
                    ParticleBuilder(Particle.SMOKE).count(3)
                        .location(itemDisplay.location.clone().add(direction.multiply(-0.4))).extra(0.0).offset(0.1, 0.2, 0.1).spawn()
                    itemDisplay.world.playSound(itemDisplay.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.5f, 2f)

                    for (a in itemDisplay.getNearbyEntities(0.2, 0.2, 0.2)) {
                        if (a.type == EntityType.HORSE) {
                            for (a2 in a.scoreboardTags) {
                                if (a2 != "kart") {
                                    val plr2 = Bukkit.getPlayer(UUID.fromString(a2))
                                    VehicleUtils.knockout(plr2!!)
                                    stopped = true
                                    break;
                                }
                            }
                        } else if (a.type == EntityType.PLAYER) {
                            if (Memory.getPlayerMemory(a as Player)?.inRace!!) {
                                if (a != plr) {
                                    VehicleUtils.knockout(a)
                                    stopped = true
                                    break;
                                }
                            }
                        }
                    }
                    if (ticks == 160 || stopped) {

                        ParticleBuilder(Particle.EXPLOSION).count(1).location(itemDisplay.location).spawn()
                        itemDisplay.world.playSound(itemDisplay.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                        itemDisplay.remove()


                        this.cancel()
                    }

                    ticks++

                }

            }.runTaskTimer(Main.instance!!, 0, 1)
        }
    }

    fun fireball(player: Player) {
        val fireballAmount = 5
        val ticksBetweenFireballs = 6
        for (i2 in 0..fireballAmount) {
            object : BukkitRunnable() {
                override fun run() {


                    val itemDisplay = player.world.spawn(player.location, ItemDisplay::class.java)
                    val a2 = ItemStack(Material.LEATHER_HORSE_ARMOR)
                    a2.itemMeta.setCustomModelData(1227)
                    itemDisplay.setItemStack(a2)
                    player.world.playSound(player.location, Sound.ENTITY_EGG_THROW, 1f, 1f)
                    player.world.playSound(player.location, Sound.ITEM_FIRECHARGE_USE, 1f, 1f)
                    val a = VehicleUtils.getVehicleManager()?.getVehicle(player)?.VehicleHorse!!

                    var targetLocation = a.location.clone().add(a.location.direction.clone().multiply(Random().nextDouble(6.0, 12.5)))
                    when (Random().nextInt(1, 3)) {
                        //decide to deviant projectile to the left or right from 0.1-0.3
                        1 -> {
                            //no deviant
                        }

                        2 -> {
                            //deviant left
                            targetLocation = targetLocation.add(
                                a.location.direction.normalize().clone().rotateAroundY(Math.toRadians(90.0))
                                    .multiply(Random().nextDouble(0.1, 0.3))
                            )
                        }

                        3 -> {
                            //deviant right
                            targetLocation = targetLocation.add(
                                a.location.direction.normalize().clone().rotateAroundY(Math.toRadians(-90.0))
                                    .multiply(Random().nextDouble(0.1, 0.3))
                            )
                        }
                    }
                    if (targetLocation.block.isSolid) targetLocation = targetLocation.add(0.0, 1.0, 0.0)
                    if (targetLocation.block.isSolid) targetLocation = targetLocation.add(0.0, 1.0, 0.0)
                    if (targetLocation.block.isSolid) targetLocation = targetLocation.add(0.0, 1.0, 0.0)
                    if (!targetLocation.clone().add(0.0, -1.0, 0.0).block.isSolid) targetLocation =
                        targetLocation.add(0.0, -1.0, 0.0)
                    if (!targetLocation.clone().add(0.0, -1.0, 0.0).block.isSolid) targetLocation =
                        targetLocation.add(0.0, -1.0, 0.0)
                    if (!targetLocation.clone().add(0.0, -1.0, 0.0).block.isSolid) targetLocation =
                        targetLocation.add(0.0, -1.0, 0.0)
                    val arcHeight = 2
                    val distance = player.location.distance(targetLocation)
                    //ticks/smoothness of arc
                    val steps = 12

                    //throw in arc
                    for (i in 0..steps) {
                        val t = i.toDouble() / steps
                        val x = player.location.x + (targetLocation.x - player.location.x) * t
                        val y = player.location.y + arcHeight * sin(Math.PI * t) // Arc effect
                        val z = player.location.z + (targetLocation.z - player.location.z) * t

                        val newLocation = Location(player.world, x, y, z)
                        object : BukkitRunnable() {
                            override fun run() {
                                itemDisplay.teleport(newLocation)
                                var hit = false
                                for (a4 in itemDisplay.getNearbyEntities(0.3, 0.3, 0.3)) {
                                    if (a4.type == EntityType.HORSE) {
                                        for (a5 in a4.scoreboardTags) {
                                            if (a5 != "kart") {
                                                val plr2 = Bukkit.getPlayer(UUID.fromString(a5))
                                                if (plr2 != player) {
                                                    hit = true
                                                    break;
                                                }
                                            }
                                        }
                                    } else if (a4.type == EntityType.PLAYER) {
                                        if (Memory.getPlayerMemory(a4 as Player)?.inRace!!) {
                                            if (player != a4) {
                                                hit = true
                                                break;
                                            }

                                        }
                                    }
                                }
                                if (i == steps || hit) {
                                    ParticleBuilder(Particle.GUST).count(1).extra(0.0).location(itemDisplay.location).spawn()
                                    ParticleBuilder(Particle.SMOKE).count(15).offset(0.3,0.3,0.3).extra(0.0).location(itemDisplay.location).spawn()
                                    ParticleBuilder(Particle.CAMPFIRE_COSY_SMOKE).count(6).extra(0.02).offset(0.3,0.3,0.3).location(itemDisplay.location).spawn()
                                    itemDisplay.world.playSound(itemDisplay.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,0.6f,1f)
                                    for (a4 in itemDisplay.getNearbyEntities(1.0, 1.0, 1.0)) {
                                        if (a4.type == EntityType.HORSE) {
                                            for (a5 in a4.scoreboardTags) {
                                                if (a5 != "kart") {
                                                    val plr2 = Bukkit.getPlayer(UUID.fromString(a5))
                                                    //todo: mini knockout
                                                    VehicleUtils.miniKnockout(plr2!!)
                                                    break;
                                                }
                                            }
                                        } else if (a4.type == EntityType.PLAYER) {
                                            if (Memory.getPlayerMemory(a4 as Player)?.inRace!!) {
                                                VehicleUtils.miniKnockout(a4)
                                                break;

                                            }
                                        }
                                    }
                                    this.cancel()
                                }
                            }

                        }.runTaskLater(Main.instance!!, i.toLong())


                    }
                }
            }.runTaskLater(Main.instance!!, (i2 * ticksBetweenFireballs).toLong())
        }
    }


    fun lightning(plr: Player) {
        val sortedPlayers =
            Memory.server?.races?.get(Memory.getPlayerMemory(plr)?.raceID)?.PlayersInRace?.sortedByDescending {
                Memory.getPlayerMemory(it)?.racePos!! + (Memory.getPlayerMemory(it)?.laps!! * 50)
            }
        val positions = mutableMapOf<Player, Int>()
        sortedPlayers?.forEachIndexed { index, player ->
            positions[player] = index + 1
        }
        val target : Player = if (sortedPlayers?.first()!! == plr) {
            sortedPlayers[1]
        } else {
            sortedPlayers.first()
        }

        target.isInvulnerable = true
        target.world.strikeLightning(target.location)
        target.isInvulnerable = false
        target.sendTitle("","§eStruck by §f${plr.name}")
        VehicleUtils.knockout(target)

    }

    fun banana(player: Player) {
        val itemDisplay = player.world.spawn(player.location, ItemDisplay::class.java)
        val a2 = ItemStack(Material.LEATHER_HORSE_ARMOR)
        a2.itemMeta.setCustomModelData(1225)
        itemDisplay.setItemStack(a2)
        player.world.playSound(player.location, Sound.ENTITY_EGG_THROW, 1f, 1f)
        val a = VehicleUtils.getVehicleManager()?.getVehicle(player)?.VehicleHorse!!
        var targetLocation = a.location.clone().add(a.location.direction.clone().multiply(-2))
        if (targetLocation.block.isSolid) targetLocation = targetLocation.add(0.0, 1.0, 0.0)
        val arcHeight = 1.5
        val distance = player.location.distance(targetLocation)
        //ticks/smoothness of arc
        val steps = 15

        //throw in arc
        for (i in 0..steps) {
            val t = i.toDouble() / steps
            val x = player.location.x + (targetLocation.x - player.location.x) * t
            val y = player.location.y + arcHeight * sin(Math.PI * t) // Arc effect
            val z = player.location.z + (targetLocation.z - player.location.z) * t

            val newLocation = Location(player.world, x, y, z)
            object : BukkitRunnable() {
                override fun run() {
                    itemDisplay.teleport(newLocation)
                }
            }.runTaskLater(Main.instance!!, i.toLong())

            if (i == steps) {
                //once at final destination
                object : BukkitRunnable() {
                    var ticks = 0
                    override fun run() {
                        if (ticks == i/2) {
                            for (e in itemDisplay.getNearbyEntities(0.3, 0.3, 0.3)) {
                                if (e.type == EntityType.HORSE) {
                                    for (a3 in e.scoreboardTags) {
                                        if (a3 != "kart") {
                                            val plr2 = Bukkit.getPlayer(UUID.fromString(a3))
                                            VehicleUtils.knockout(plr2!!)
                                            itemDisplay.world.playSound(
                                                itemDisplay.location,
                                                Sound.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM,
                                                1f,
                                                2f
                                            )
                                            itemDisplay.remove()
                                            this.cancel()
                                            break;
                                        }
                                    }
                                } else if (e.type == EntityType.PLAYER) {
                                    if (Memory.getPlayerMemory(e as Player)?.inRace!!) {
                                        itemDisplay.world.playSound(
                                            itemDisplay.location,
                                            Sound.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM,
                                            1f,
                                            2f
                                        )

                                        VehicleUtils.knockout(e)
                                        itemDisplay.remove()
                                        this.cancel()
                                        break;
                                    }
                                }
                            }

                            if (ticks == 1200) {
                                itemDisplay.remove()
                                this.cancel()
                            }
                        }
                        ticks++
                    }
                }.runTaskTimer(Main.instance!!, 0, 2)
            }
        }
    }

        fun spider(player: Player) {
            val itemDisplay = player.world.spawn(player.location, BlockDisplay::class.java)
            itemDisplay.block = Material.COBWEB.createBlockData()
            player.world.playSound(player.location,Sound.ENTITY_EGG_THROW,1f,1f)
            player.world.playSound(player.location,Sound.ENTITY_SPIDER_AMBIENT,1f,1f)
            val a = VehicleUtils.getVehicleManager()?.getVehicle(player)?.VehicleHorse!!
            var targetLocation = a.location.clone().add(a.location.direction.clone().multiply(-1.5))
            if (targetLocation.block.isSolid) targetLocation = targetLocation.add(0.0,1.0,0.0)
            val arcHeight = 1.5
            val distance = player.location.distance(targetLocation)
            //ticks/smoothness of arc
            val steps = 15

            //throw in arc
            for (i in 0..steps) {
                val t = i.toDouble() / steps
                val x = player.location.x + (targetLocation.x - player.location.x) * t
                val y = player.location.y + arcHeight * sin(Math.PI * t) // Arc effect
                val z = player.location.z + (targetLocation.z - player.location.z) * t

                val newLocation = Location(player.world, x, y, z)
                object : BukkitRunnable() {
                    override fun run() {
                        itemDisplay.teleport(newLocation)
                    }
                }.runTaskLater(Main.instance!!,i.toLong())

                if (i == steps) {
                    //once at final destination
                    val originalType = itemDisplay.block.createBlockState().type
                    itemDisplay.block.createBlockState().type = Material.COBWEB

                    object : BukkitRunnable() {
                        override fun run() {
                            itemDisplay.block.createBlockState().type = originalType
                            itemDisplay.remove()
                        }
                    }.runTaskLater(Main.instance!!, 1200)
                }
            }
    }

        fun tnt(plr:Player) {
            val fuseInTicks = 50
            val r = plr.world.spawn(plr.location,TNTPrimed::class.java)
            r.fuseTicks = fuseInTicks
            val explosionSize = 2.0
            object : BukkitRunnable() {
                override fun run() {
                    for (a in r.getNearbyEntities(explosionSize,explosionSize,explosionSize)) {
                        if (a.type == EntityType.HORSE) {
                            for (a2 in a.scoreboardTags) {
                                if (a2 != "kart") {
                                    val plr2 = Bukkit.getPlayer(UUID.fromString(a2))
                                    VehicleUtils.knockout(plr2!!)
                                    break;
                                }
                            }
                        } else if (a.type == EntityType.PLAYER) {
                            if (Memory.getPlayerMemory(a as Player)?.inRace!!) {
                                VehicleUtils.knockout(a)
                                break;

                            }
                        }
                    }
                    r.world.playSound(r.location,Sound.ENTITY_GENERIC_EXPLODE,1f,1f)
                    ParticleBuilder(Particle.EXPLOSION_EMITTER).count(1).location(r.location).spawn()
                    r.remove()

                }
            }.runTaskLater(Main.instance!!, (fuseInTicks-2).toLong())

        }

    fun smoke(plr:Player) {
        object : BukkitRunnable() {
            var ticks = 0
            var vehicle = VehicleUtils.getVehicleManager()?.getVehicle(plr)?.VehicleHorse!!
            var location = vehicle.location.clone().add(vehicle.location.direction.clone().multiply(-1))
            override fun run() {
                ParticleBuilder(Particle.CAMPFIRE_COSY_SMOKE).count(12).location(location).extra(0.0).offset(3.5,2.5,3.5).spawn()
               if (ticks == 200) {
                   this.cancel()
               }
                ticks++
            }
        }.runTaskTimer(Main.instance!!,0,1)
        plr.world.playSound(plr.location,Sound.ENTITY_TNT_PRIMED,1f,2f)
    }

    fun shield(player: Player) {
        val radius = 1.5
        val ticksPerCircle = 12
        var tickCount = 0
        if (Memory.getPlayerMemory(player)?.shields!! >= 3) {
            player.sendMessage("§c§lMax shields equipped")
            return
        }
        Memory.getPlayerMemory(player)?.shields = Memory.getPlayerMemory(player)?.shields!! + 1
        val shields = Memory.getPlayerMemory(player)?.shields
        val itemDisplay = player.world.spawn(player.location, ItemDisplay::class.java)
        val a2 = ItemStack(Material.LEATHER_HORSE_ARMOR)
        a2.itemMeta.setCustomModelData(1226)
        itemDisplay.setItemStack(a2)
        player.playSound(player, Sound.BLOCK_ANVIL_DESTROY,1f,2f)
      object: BukkitRunnable(){
          override fun run() {
              val angle = (tickCount * (360.0 / ticksPerCircle)) * (Math.PI / 180) // Convert to radians
              val x = radius * cos(angle)
              val z = radius * sin(angle)

              val newLocation = player.location.clone().add(Vector(x, 0.5, z))
              itemDisplay.teleport(newLocation)

              tickCount = (tickCount + 1) % ticksPerCircle

            if (Memory.getPlayerMemory(player)?.shields!! < shields!! || !Memory.getPlayerMemory(player)?.inRace!!) {

                itemDisplay.remove()
                this.cancel()
            }
          }
        }.runTaskTimer(Main.instance!!,0,1)
    }


}