package tortel.gokartsecondtry.Utils

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

object CONSTANTS {
    fun getMainkart(): ItemStack {
        val Item = ItemStack(Material.PAPER)
        val meta = Item.itemMeta
        meta.setCustomModelData(9) // default kart model
        Item.setItemMeta(meta)

        return Item
    }

    val KartOffset = Vector(0.0,0.5,0.0)
}