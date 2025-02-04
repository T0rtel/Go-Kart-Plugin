package tortel.gokartsecondtry.services.karts

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.*
import tortel.gokartsecondtry.services.KartInterface

class DefaultKart : KartInterface {
    override lateinit var KartHorseEntity: Horse
    override lateinit var KartDisplayEntity: ItemDisplay


    fun spawnHorseEntity(player : Player){
        KartHorseEntity = player.world
            .spawnEntity(
                Location(player.world, player.location.x, player.location.y, player.location.z, player.location.yaw, player.location.pitch),
                EntityType.HORSE) as Horse

        Bukkit.getMobGoals().removeAllGoals(KartHorseEntity)
        KartHorseEntity.isInvulnerable = true
        KartHorseEntity.isInvisible = true
        KartHorseEntity.isCustomNameVisible = false
        KartHorseEntity.setNoPhysics(false)
        KartHorseEntity.setGravity(true)
        KartHorseEntity.isCollidable = false
        KartHorseEntity.isPersistent = true
        KartHorseEntity.removeWhenFarAway = false

        KartHorseEntity.setBaby()
        KartHorseEntity.isTamed = true
        KartHorseEntity.owner = player
        KartHorseEntity.isSilent = true

        KartHorseEntity.customName(Component.text(player.name))
        Bukkit.getMobGoals().removeAllGoals(KartHorseEntity)
    }

    fun spawnDisplayEntity(player : Player){
        KartDisplayEntity = player.world
            .spawnEntity(
                Location(player.world, player.location.x, player.location.y, player.location.z),
                EntityType.ITEM_DISPLAY) as ItemDisplay

        KartDisplayEntity.isInvulnerable = true
        KartDisplayEntity.setNoPhysics(true)
        KartDisplayEntity.setGravity(false)
        KartDisplayEntity.teleportDuration = 1
        KartDisplayEntity.customName(Component.text(player.name))
        KartDisplayEntity.addScoreboardTag("needstotp")
        KartDisplayEntity.addPassenger(player)

        //KartDisplayEntity.setItemStack()
    }
}