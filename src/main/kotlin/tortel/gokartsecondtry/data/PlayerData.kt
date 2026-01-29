package tortel.gokartsecondtry.data

import org.bukkit.util.Vector

class PlayerData {
    //-------AdminData
    var StartedStart : Boolean = false
    var direction : Boolean = false
    var length: Int = 1
    var vector: Vector = Vector(0,0,1)


    //--------PlayerData
    var racePos: Int = 0
    var laps: Int = 0
    var inRace : Boolean = false
    var raceID : String = ""
    var tempTrackQueue : String = ""
    var boosting : Boolean = false
    var time : Double = 0.0
    var lapTime: Double = 0.0
    var currentItem : String = ""
    var shields = 0
}