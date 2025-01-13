package tortel.gokartsecondtry.Utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.data.RaceTracksConfig
import javax.sound.midi.Track

object RacingTracksConfigUtils {

    fun addRacePos(loc : Location, racerNumber : Int) : Boolean{
        val config = RaceTracksConfig.getConfig()
        val raceTrack = loc.world.name
        val x = loc.blockX
        val y = loc.blockY
        val z = loc.blockZ
        val yaw = loc.yaw
        var isaReplace = false

        if (config.get("racetracks.$raceTrack.$racerNumber") != null){
            isaReplace = true
        }

        //config.set("racetracks.${raceTrack}", raceTrack)
        config.set("racetracks.$raceTrack.$racerNumber.x", x)
        config.set("racetracks.$raceTrack.$racerNumber.y", y)
        config.set("racetracks.$raceTrack.$racerNumber.z", z)
        config.set("racetracks.$raceTrack.$racerNumber.yaw", yaw)
        save()

        return isaReplace

    }

    fun removeRacePos(loc: Location, racerNumber: Int){
        val config = RaceTracksConfig.getConfig()
        val raceTrack = loc.world
        if (config.get("racetracks.$raceTrack.$racerNumber") == null) return

        config.set("racetracks.$raceTrack.$racerNumber", null)
        save()
    }

    fun removeAllRacePos(loc: Location){
        val config = RaceTracksConfig.getConfig()
        val raceTrack = loc.world
        if (config.get("racetracks.$raceTrack") == null) return

        config.set("racetracks.$raceTrack", null)
        save()
    }

    fun getTrackCoords(TrackName : String, i: Int): Location {
        println("getting coords for ${TrackName} for playr in $i")
        val config = RaceTracksConfig.getConfig()
        if (config.get("racetracks.$TrackName") != null){
            val x = config.getDouble("racetracks.$TrackName.$i.x") + 0.5
            val y = config.getDouble("racetracks.$TrackName.$i.y")
            val z = config.getDouble("racetracks.$TrackName.$i.z") + 0.5
            val yaw = config.getDouble("racetracks.$TrackName.$i.yaw").toFloat()
            val finalLoc = Location(Bukkit.getWorld(TrackName),x,y,z,yaw,0.0f)
            return finalLoc
        }else{

        }
       return Location(null,0.0,0.0,0.0)
    }

    fun save(){
        RaceTracksConfig.save()
    }

}