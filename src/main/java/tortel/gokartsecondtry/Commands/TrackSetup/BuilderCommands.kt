package tortel.gokartsecondtry.Commands.TrackSetup

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.Race.RacingTracksConfigUtils


@Command("build")
@Description("Opens build menu, where you can build,edit, and remove custom parts.")
class BuilderCommands {
    @Subcommand("menu")
    fun addRacePos(sender: Player) {
        if (!sender.isOp) return
        val inventory1: Inventory = sender.inventory
        val openSlots = inventory1.size - inventory1.contents.count { it != null }
        if (openSlots >= 1) {
            val item1 = ItemStack(Material.MACE)
            val itemMeta1 = item1.itemMeta
            itemMeta1?.setDisplayName("§aBuild tool")
            itemMeta1?.persistentDataContainer?.set(
                NamespacedKey("gokart", "what"),
                PersistentDataType.STRING,
                "builder"
            )
            item1.itemMeta = itemMeta1


            // Add items to the inventory0
            inventory1.addItem(item1  )

            // Open the inventory for a player (assuming you have a player object)



        } else {
            sender.sendMessage("Open at least (1) empty slot(s) in your inventory")
        }
    }
}