package tortel.gokartsecondtry.Utils.KeyListener


import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class KeyPressEvent(val player: Player, val key: Char) : Event() {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}