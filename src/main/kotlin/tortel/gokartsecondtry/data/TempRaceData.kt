package tortel.gokartsecondtry.data

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import tortel.gokartsecondtry.Utils.Race.RaceUtils

class TempRaceData(util : RaceUtils, loc : Location, tracka : String) {
    val PlayersInRace = mutableListOf<Player>()
    val finished = mutableListOf<Player>()
    val raceUtil = util
    val pos = loc
    val track = tracka
    val boxes : HashMap<Location, Entity> = hashMapOf()
}