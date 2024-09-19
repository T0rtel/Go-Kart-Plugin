package tortel.gokartsecondtry.Commands

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Horse
import org.bukkit.entity.Player

class RideCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, p2: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        // val Vehicle = sender.world.spawnEntity(sender.location, EntityType.ARMOR_STAND) as ArmorStand
        val Vehicle = sender.world.spawnEntity(sender.location, EntityType.ARMOR_STAND) as ArmorStand
        Vehicle.isInvulnerable = true
        Vehicle.isInvisible = false
        Vehicle.isCustomNameVisible = false
        Vehicle.setAI(false)
       // Vehicle.setBaby()
        //Vehicle.isTamed = true
        //Vehicle.owner = sender

        //make the player sit on armor stand
        Vehicle.addPassenger(sender)
        Vehicle.customName(Component.text(sender.name))

        return false
    }
}