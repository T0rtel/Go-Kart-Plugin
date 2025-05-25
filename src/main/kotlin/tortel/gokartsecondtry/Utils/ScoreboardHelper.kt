package tortel.gokartsecondtry.Utils

    import org.bukkit.Bukkit
    import org.bukkit.ChatColor
    import org.bukkit.entity.Player
    import org.bukkit.scoreboard.DisplaySlot
    import java.util.*

/**
     *
     * @author crisdev333
     *
     */
//Provided by this API I found here: https://www.spigotmc.org/threads/create-scoreboards-in-a-simple-way.272337/
class ScoreHelper private constructor(player: Player) {
    private val scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val sidebar = scoreboard.registerNewObjective("sidebar", "dummy")

    init {
        sidebar.displaySlot = DisplaySlot.SIDEBAR
        // Create Teams
        for (i in 1..15) {
            val team = scoreboard.registerNewTeam("SLOT_$i")
            team.addEntry(genEntry(i))
        }
        player.scoreboard = scoreboard
        players[player.uniqueId] = this
    }

    fun setTitle(title: String) {
        var title = title
        title = ChatColor.translateAlternateColorCodes('&', title)
        sidebar.displayName = if (title.length > 32) title.substring(0, 32) else title
    }

    fun setSlot(slot: Int, text: String?) {
        var text = text
        val team = scoreboard.getTeam("SLOT_$slot")
        val entry = genEntry(slot)
        if (!scoreboard.entries.contains(entry)) {
            sidebar.getScore(entry).score = slot
        }

        text = ChatColor.translateAlternateColorCodes('&', text!!)
        val pre = getFirstSplit(text)
        val suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text))
        team!!.prefix = pre
        team.suffix = suf
    }

    fun removeSlot(slot: Int) {
        val entry = genEntry(slot)
        if (scoreboard.entries.contains(entry)) {
            scoreboard.resetScores(entry)
        }
    }

    fun setSlotsFromList(list: kotlin.collections.MutableList<String?>) {
        while (list.size > 15) {
            list.removeAt(list.size - 1)
        }

        var slot = list.size

        if (slot < 15) {
            for (i in (slot + 1)..15) {
                removeSlot(i)
            }
        }

        for (line in list) {
            setSlot(slot, line)
            slot--
        }
    }

    private fun genEntry(slot: Int): String {
        return ChatColor.entries[slot].toString()
    }

    private fun getFirstSplit(s: String): String {
        return if (s.length > 16) s.substring(0, 16) else s
    }

    private fun getSecondSplit(s: String): String {
        var s = s
        if (s.length > 32) {
            s = s.substring(0, 32)
        }
        return if (s.length > 16) s.substring(16) else ""
    }

    companion object {
        private val players = HashMap<UUID, ScoreHelper>()

        fun hasScore(player: Player): Boolean {
            return players.containsKey(player.uniqueId)
        }

        fun createScore(player: Player): ScoreHelper {
            return ScoreHelper(player)
        }

        fun getByPlayer(player: Player): ScoreHelper? {
            return players[player.uniqueId]
        }

        fun removeScore(player: Player): ScoreHelper? {
            return players.remove(player.uniqueId)
        }
    }
}