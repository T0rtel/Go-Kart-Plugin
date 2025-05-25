package tortel.gokartsecondtry.Utils.KeyListener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class KeyListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onKeyPress(event: KeyPressEvent) {
        if (event.key == 'S') {
            event.player.sendMessage("S clicked")
        }
    }

    @EventHandler
    fun onKeyRelease(event: KeyReleaseEvent) {
        if (event.key == 'S') {
            event.player.sendMessage("S let go of")
        }
    }
}