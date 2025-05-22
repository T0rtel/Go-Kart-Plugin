package tortel.gokartsecondtry.Utils.Race

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import tortel.gokartsecondtry.data.Checkpoint
import tortel.gokartsecondtry.data.Memory
import tortel.gokartsecondtry.data.RaceTracksConfig

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

    fun addStartPos(loc : Location, player : Player) : Boolean{
        val config = RaceTracksConfig.getConfig()
        val raceTrack = loc.world.name
        val x = loc.blockX
        val y = loc.blockY
        val z = loc.blockZ
        val yaw = loc.yaw
        val vector = Memory.getPlayerMemory(player)?.vector!!
        var isaReplace = false
        Memory.server?.finishLine?.put(raceTrack, Checkpoint(loc, Memory.getPlayerMemory(player)?.length!!, Memory.getPlayerMemory(player)?.direction!! ,vector, 0))

        /* if (Memory?.server?.checkpoint?.get(raceTrack)?.isEmpty() == true) {
             Memory?.server?.checkpoint?.put(raceTrack, listOf(Checkpoint()))
         } else {

         }
 */
        if (config.get("racetracks.$raceTrack.start") != null){
            isaReplace = true
        }

        //config.set("racetracks.${raceTrack}", raceTrack)
        config.set("racetracks.$raceTrack.length", Memory.getPlayerMemory(player)?.length)
        if (Memory.getPlayerMemory(player)?.direction == false) {
            config.set("racetracks.$raceTrack.dir", "x")
        } else {
            config.set("racetracks.$raceTrack.dir", "z")
        }
        config.set("racetracks.$raceTrack.start.V.x", vector.x)
        config.set("racetracks.$raceTrack.start.V.y", vector.y)
        config.set("racetracks.$raceTrack.start.V.z", vector.z)
        config.set("racetracks.$raceTrack.start.x", x)
        config.set("racetracks.$raceTrack.start.y", y)
        config.set("racetracks.$raceTrack.start.z", z)
        config.set("racetracks.$raceTrack.start.yaw", yaw)
        save()

        return isaReplace

    }

    fun addCheckpointPos(loc : Location, player : Player) : Boolean{
        val config = RaceTracksConfig.getConfig()
        val raceTrack = loc.world.name
        val x = loc.blockX
        val y = loc.blockY
        val z = loc.blockZ
        val yaw = loc.yaw
        val vector = Memory.getPlayerMemory(player)?.vector!!
        var isaReplace = false
        var num : Int = 1
        if (Memory.server?.checkpoint?.containsKey(raceTrack) == false) {
            Memory.server?.checkpoint?.put(raceTrack, mutableListOf(Checkpoint(loc, Memory.getPlayerMemory(player)?.length!!, Memory.getPlayerMemory(player)?.direction!! ,vector, num)))
        } else {
            for (ch in Memory.server?.checkpoint?.get(raceTrack)!!) {
                num +=1
            }
            val a : MutableList<Checkpoint> = Memory.server?.checkpoint?.get(raceTrack)!!
            a.add(Checkpoint(loc, Memory.getPlayerMemory(player)?.length!!, Memory.getPlayerMemory(player)?.direction!! ,vector, num))
            Memory.server?.checkpoint?.put(raceTrack, a)

        }

        if (config.get("racetracks.$raceTrack.C.$num") != null){
            isaReplace = true
        }

        //config.set("racetracks.${raceTrack}", raceTrack)
        config.set("racetracks.$raceTrack.C.$num.length", Memory.getPlayerMemory(player)?.length)
        if (Memory.getPlayerMemory(player)?.direction == false) {
            config.set("racetracks.$raceTrack.C.$num.dir", "x")
        } else {
            config.set("racetracks.$raceTrack.C.$num.dir", "z")
        }
        config.set("racetracks.$raceTrack.C.$num.V.x", vector.x)
        config.set("racetracks.$raceTrack.C.$num.V.y", vector.y)
        config.set("racetracks.$raceTrack.C.$num.V.z", vector.z)
        config.set("racetracks.$raceTrack.C.$num.x", x)
        config.set("racetracks.$raceTrack.C.$num.y", y)
        config.set("racetracks.$raceTrack.C.$num.z", z)
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