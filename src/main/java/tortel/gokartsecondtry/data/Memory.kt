package tortel.gokartsecondtry.data

import org.bukkit.entity.Player
import tortel.gokartsecondtry.Main
import java.util.HashMap

object Memory {
    private val memory: MutableMap<String, PlayerData> = HashMap()
    private val e: Main = Main.instance as Main
    var server: ServerData? = null
        get() {
            if (field == null) {
                field = ServerData()
            }
            return field
        }

    fun getPlayerMemory(player: Player): PlayerData? {
        if (!memory.containsKey(player.uniqueId.toString())) {
            val m = PlayerData()
            memory[player.uniqueId.toString()] = m
            return m
        }
        return memory[player.uniqueId.toString()]
    }

    fun setMemory(player: Player, m2emory: PlayerData?) {
        if (m2emory == null) memory.remove(player.uniqueId.toString())
        else memory[player.uniqueId.toString()] = m2emory
    }

    fun getFolderPath(player: Player): String {
        return Main.dataFolderDir.toString() + "/player/" + "/" + player.uniqueId + "/" + player.uniqueId
    }
}