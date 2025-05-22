package tortel.gokartsecondtry.Listeners

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import tortel.gokartsecondtry.data.Memory

class PlayerInventoryInput : Listener {
    @EventHandler
    fun scroll(event: PlayerItemHeldEvent) {
        event.player.sendMessage(Memory.getPlayerMemory(event.player)?.StartedStart.toString())
        if (Memory.getPlayerMemory(event.player)?.StartedStart == true) {
            if (push(event.previousSlot, event.newSlot)) {
                Memory.getPlayerMemory(event.player)?.let { memory ->
                    memory.length -= 1
                    event.player.sendTitle("", "Length changed | " + (memory.length + 1) + " -> " + memory.length)
                }
            } else if (pull(event.previousSlot, event.newSlot)) {
                event.player.sendMessage("pass2")
                Memory.getPlayerMemory(event.player)?.let { memory ->
                    memory.length += 1
                    event.player.sendTitle("", "Length changed | " + (memory.length - 1) + " -> " + memory.length)
                        }
            }
            event.isCancelled = true
        }
    }

    @EventHandler
    fun sneak(event: PlayerToggleSneakEvent) {
        if (event.isSneaking) {
            if (Memory.getPlayerMemory(event.player)?.StartedStart == true) {
                val vec : Vector = Memory.getPlayerMemory(event.player)?.vector!!
                if (vec.x == 1.0) {
                Memory.getPlayerMemory(event.player)?.vector = Vector(-1,0,0)
                } else if (vec.x == -1.0) {
                    Memory.getPlayerMemory(event.player)?.vector = Vector(0,0,1)
                    Memory.getPlayerMemory(event.player)?.direction = false
                } else if (vec.z == 1.0) {
                    Memory.getPlayerMemory(event.player)?.vector = Vector(0,0,-1)

                } else if (vec.z == -1.0) {
                    Memory.getPlayerMemory(event.player)?.vector = Vector(1,0,0)

                    Memory.getPlayerMemory(event.player)?.direction = true
                }
            }
        }
    }
    fun push(previousSlot: Int, newSlot: Int): Boolean {
        return (newSlot > previousSlot || (previousSlot == 8 && newSlot == 0))
    }

    fun pull(previousSlot: Int, newSlot: Int): Boolean {
        return (newSlot < previousSlot || (previousSlot == 0 && newSlot == 8))
    }
    @EventHandler
    fun ClickEvent(event: PlayerInteractEvent) {
        if (event.item?.hasItemMeta() == true) {
            if (event.item?.itemMeta?.persistentDataContainer?.isEmpty == false) {
                if (event.item?.persistentDataContainer?.get(
                        NamespacedKey("gokart", "what"),
                        PersistentDataType.STRING
                    ) == "builder"
                ) {
                    val player: Player = event.player
                    val inventory: Inventory = Bukkit.createInventory(null, 27, "Build Menu")

                    // Create items with different names and PDCs
                    val item1 = ItemStack(Material.DIAMOND)
                    val itemMeta1 = item1.itemMeta
                    itemMeta1?.setDisplayName("§aJump pad")
                    itemMeta1?.persistentDataContainer?.set(
                        NamespacedKey("gokart", "what"),
                        PersistentDataType.STRING,
                        "jump"
                    )
                    item1.itemMeta = itemMeta1

                    val item2 = ItemStack(Material.GOLD_INGOT)
                    val itemMeta2 = item2.itemMeta
                    itemMeta2?.setDisplayName("§eSpeed boost")
                    itemMeta2?.persistentDataContainer?.set(
                        NamespacedKey("gokart", "what"),
                        PersistentDataType.STRING,
                        "boost"
                    )
                    item2.itemMeta = itemMeta2

                    val item3 = ItemStack(Material.EMERALD)
                    val itemMeta3 = item3.itemMeta
                    itemMeta3?.setDisplayName("§aLaunch Pad")
                    itemMeta3?.persistentDataContainer?.set(
                        NamespacedKey("gokart", "what"),
                        PersistentDataType.STRING,
                        "launch"
                    )
                    item3.itemMeta = itemMeta3

                    // Add items to the inventory
                    inventory.addItem(item1, item2, item3)

                    // Open the inventory for a player (assuming you have a player object)
                    player.openInventory(inventory)
                }
            }
            when (event.action) {
                // Detecting right-click
                Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                    if (event.item?.persistentDataContainer?.get(
                            NamespacedKey("gokart", "what"),
                            PersistentDataType.STRING
                        ) == "builder"
                    ) {
                    }
                }
                // Detecting left-click
                Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                    if (event.item?.persistentDataContainer?.get(
                            NamespacedKey("gokart", "what"),
                            PersistentDataType.STRING
                        ) == "builder"
                    ) {
                    }
                }

                else -> {}
            }

        }
    }

    @EventHandler
    fun InventoryClickEvent(event: InventoryClickEvent) {
        if (event.currentItem?.hasItemMeta() == true) {

            if (event.currentItem?.itemMeta?.persistentDataContainer?.isEmpty == false) {
                val sr: String? = event.currentItem?.persistentDataContainer?.get(
                    NamespacedKey("gokart", "what"),
                    PersistentDataType.STRING
                )
                val plr: Player = event.whoClicked as Player
                if (sr == "boost") {
                    val item2 = ItemStack(Material.GOLD_INGOT)
                    val itemMeta2 = item2.itemMeta
                    itemMeta2?.setDisplayName("§eSpeed boost")
                    itemMeta2?.persistentDataContainer?.set(
                        NamespacedKey("gokart", "what"),
                        PersistentDataType.STRING,
                        "boost"
                    )
                    item2.itemMeta = itemMeta2
                    plr.inventory.addItem(item2)
                } else if (sr == "jump") {

                } else if (sr == "launch") {

                }
            }
        }
        /*


     */

    }
}