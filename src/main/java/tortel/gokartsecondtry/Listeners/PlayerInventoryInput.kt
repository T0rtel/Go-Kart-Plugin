package tortel.gokartsecondtry.Listeners

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class PlayerInventoryInput : Listener {
    fun ClickEvent(event: PlayerInteractEvent) {
        if (event.item?.hasItemMeta() == true) {
            if (event.item?.itemMeta?.persistentDataContainer?.isEmpty == false) {
                        if (event.item?.persistentDataContainer?.get(NamespacedKey("gokart","what"), PersistentDataType.STRING) == "builder") {
                         val player : Player = event.player
                            val inventory: Inventory = Bukkit.createInventory(null, 27, "My Custom Inventory")

                            // Create items with different names and PDCs
                            val item1 = ItemStack(Material.DIAMOND)
                            val itemMeta1 = item1.itemMeta
                            itemMeta1?.setDisplayName("§aJump pad")
                            itemMeta1?.persistentDataContainer?.set(NamespacedKey("gokart", "what"), PersistentDataType.STRING, "jump")
                            item1.itemMeta = itemMeta1

                            val item2 = ItemStack(Material.GOLD_INGOT)
                            val itemMeta2 = item2.itemMeta
                            itemMeta2?.setDisplayName("§eSpeed boost")
                            itemMeta1?.persistentDataContainer?.set(NamespacedKey("gokart", "what"), PersistentDataType.STRING, "boost")
                            item2.itemMeta = itemMeta2

                            val item3 = ItemStack(Material.EMERALD)
                            val itemMeta3 = item3.itemMeta
                            itemMeta3?.setDisplayName("§aLaunch Pad")
                            itemMeta1?.persistentDataContainer?.set(NamespacedKey("gokart", "what"), PersistentDataType.STRING, "launch")
                            item3.itemMeta = itemMeta3

                            // Add items to the inventory
                            inventory.addItem(item1, item2, item3)

                            // Open the inventory for a player (assuming you have a player object)
                            player.openInventory(inventory)
                        }
            }
        }
    }
    /*


     */
    
}