package tortel.gokartsecondtry.data

import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs

@SerializableAs("raceData")
class RaceData(trac:String, lapas: Int) : ConfigurationSerializable {
    //todo initialize racedata and tempracedata
   var checkpoint : MutableList<Checkpoint> = mutableListOf()
    var finishLine: Checkpoint? = null
    private var track: String? = trac
    var laps: Int = lapas
    var itemboxes: MutableList<Location> = mutableListOf()
    var racePosistions : MutableList<Location> = mutableListOf()
    override fun serialize(): MutableMap<String, Any> {
        val a : MutableMap<String, Any> = mutableMapOf()
        a["itemboxes"] = itemboxes
        a["checkpoint"] = checkpoint
        a["finish"] = finishLine!!
        a["track"] = track!!
        a["laps"] = laps
        a["racePos"] = racePosistions
        return a
    }

    companion object {
        @JvmStatic
        fun deserialize(args: Map<String, Any>): RaceData {
            val raceData = RaceData(args["track"] as String, args["laps"] as Int)
            raceData.checkpoint = args["checkpoint"] as MutableList<Checkpoint>
            raceData.finishLine = args["finish"] as Checkpoint
            raceData.racePosistions = args["racePos"] as MutableList<Location>
            raceData.itemboxes = args["itemboxes"] as MutableList<Location>
            return raceData
        }
    }
}