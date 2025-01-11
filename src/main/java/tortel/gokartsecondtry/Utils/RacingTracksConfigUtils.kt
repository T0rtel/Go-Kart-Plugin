package tortel.gokartsecondtry.Utils

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.data.RaceTracksConfig

object RacingTracksConfigUtils {
    private var config = RaceTracksConfig.getConfig()

    fun loadRacingTracksConfig(){

        object : BukkitRunnable() {
            override fun run() {
                config = RaceTracksConfig.getConfig()
            }
        }.runTaskLater(Main.instance!!, 1)

    }
    fun addRacePos(loc : Location, racerNumber : Int) : Boolean{
        val raceTrack = loc.world.name
        val x = loc.blockX
        val y = loc.blockY
        val z = loc.blockZ
        val yaw = loc.yaw
        var isaReplace = false

        if (config.get("racetracks.$raceTrack.$racerNumber") != null){
            isaReplace = true
        }

        config.set("racetracks.${raceTrack}", raceTrack)
        config.set("racetracks.$raceTrack.$racerNumber.x", x)
        config.set("racetracks.$raceTrack.$racerNumber.y", y)
        config.set("racetracks.$raceTrack.$racerNumber.z", z)
        config.set("racetracks.$raceTrack.$racerNumber.yaw", yaw)
        save()

        return isaReplace

    }

    fun removeRacePos(loc: Location, racerNumber: Int){
        val raceTrack = loc.world
        if (config.get("racetracks.$raceTrack.$racerNumber") == null) return

        config.set("racetracks.$raceTrack.$racerNumber", null)
        save()
    }

    fun removeAllRacePos(loc: Location){
        val raceTrack = loc.world
        if (config.get("racetracks.$raceTrack") == null) return

        config.set("racetracks.$raceTrack", null)
        save()
    }

    fun save(){
        RaceTracksConfig.save()
    }

}