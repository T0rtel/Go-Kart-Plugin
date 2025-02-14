package tortel.gokartsecondtry.Utils

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

object CONSTANTS {
    fun getMainkart(): ItemStack {
        val Item = ItemStack(Material.LEATHER_HORSE_ARMOR)
        val meta = Item.itemMeta
        meta.setCustomModelData(10055) // default kart model
        Item.setItemMeta(meta)

        return Item
    }

    fun getKartWheel(): ItemStack {
        val Item = ItemStack(Material.LEATHER_HORSE_ARMOR)
        val meta = Item.itemMeta
        meta.setCustomModelData(10052) // wheel kart model
        Item.setItemMeta(meta)

        return Item
    }

    fun getKartMetal(): ItemStack {
        val Item = ItemStack(Material.LEATHER_HORSE_ARMOR)
        val meta = Item.itemMeta
        meta.setCustomModelData(10051) // metal kart model
        Item.setItemMeta(meta)

        return Item
    }

    val KartOffset = Vector(0.0,0.5,0.0)
}