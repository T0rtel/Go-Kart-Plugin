package tortel.gokartsecondtry.Utils.Race

import org.bukkit.Location
import org.bukkit.entity.Player
import tortel.gokartsecondtry.data.Checkpoint
import tortel.gokartsecondtry.data.Memory

object RacingTracksConfigUtils {

    fun addRacePos(loc : Location, racerNumber : Int) : Boolean{

        val raceTrack = loc.world.name
        loc.pitch = 0F
        var isaReplace = false
        if (racerNumber <= Memory.server?.raceData?.get(raceTrack)?.racePosistions?.size!!) {
            isaReplace= true
            Memory.server?.raceData?.get(raceTrack)?.racePosistions?.set(racerNumber-1,loc)
        } else {
            Memory.server?.raceData?.get(raceTrack)?.racePosistions?.add(loc)
        }


        return isaReplace

    }

    fun addStartPos(loc : Location, player : Player) : Boolean{
        val raceTrack = loc.world.name

        val vector = Memory.getPlayerMemory(player)?.vector!!
        var isaReplace = false
        if (Memory.server?.raceData?.containsKey(raceTrack)!!) {
            Memory.server?.raceData?.get(raceTrack)?.finishLine = (Checkpoint(
                loc,
                Memory.getPlayerMemory(player)?.length!!,
                Memory.getPlayerMemory(player)?.direction!!,
                vector,
                0
            ))
        } else {
            player.sendMessage("No race track for world provided.")
            return false;
        }
        return isaReplace

    }

    fun addCheckpointPos(loc : Location, player : Player,numa :Int) : Boolean{
        val raceTrack = loc.world.name
        val vector = Memory.getPlayerMemory(player)?.vector!!
        var isaReplace = false
        var num : Int = 1
        if (Memory.server?.raceData?.containsKey(raceTrack)!!) {
            for (ch in Memory.server?.raceData?.get(raceTrack)?.checkpoint!!) {
                num +=1
            }

            Memory.server?.raceData?.get(raceTrack)?.checkpoint!!.add(Checkpoint(loc, Memory.getPlayerMemory(player)?.length!!, Memory.getPlayerMemory(player)?.direction!! ,vector, num))
            Memory.server?.raceData?.get(raceTrack)?.checkpoint?.add(Checkpoint(loc, Memory.getPlayerMemory(player)?.length!!, Memory.getPlayerMemory(player)?.direction!! ,vector, num))

        } else {
            player.sendMessage("No race track for world provided.")
            return false;

        }


        return isaReplace

    }

    fun delCheckpointPos(raceTrack : String, player : Player,numa :Int){
        val vector = Memory.getPlayerMemory(player)?.vector!!
        var e = false
        if (Memory.server?.raceData?.containsKey(raceTrack)!!) {
          for (ch in Memory.server?.raceData?.get(raceTrack)?.checkpoint!!) {
              if (ch.number == numa) {
                  Memory.server?.raceData?.get(raceTrack)?.checkpoint?.remove(ch)
                return;

              }
          }
            player.sendMessage("Not an actual checkpoint number")
        } else {
            player.sendMessage("No race track for world provided.")
            return;

        }

    }
    fun removeRacePos(loc: Location, racerNumber: Int){
        Memory.server?.raceData?.get(loc.world.name)?.racePosistions?.remove(loc)
    }

    fun removeAllRacePos(loc: Location){
        val mutableList : MutableList<Location> = mutableListOf()
        Memory.server?.raceData?.get(loc.world.name)?.racePosistions = mutableList;
    }



}