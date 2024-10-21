package tortel.gokartsecondtry.Utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import tortel.gokartsecondtry.Utils.VehicleUtils.despawnArmorStand
import tortel.gokartsecondtry.Utils.VehicleUtils.despawnHorse
import tortel.gokartsecondtry.Utils.VehicleUtils.resetAllValues
import tortel.gokartsecondtry.Utils.VehicleUtils.spawnArmorStand
import tortel.gokartsecondtry.Utils.VehicleUtils.spawnHorse

object RaceUtils {
    var RaceStarted = false

    val playersInRace = mutableListOf<Player>()

    val RaceCoords = mapOf<String, Location>(
        "kartmap" to Location(Bukkit.getWorld("kartmap"), 123.5, 32.0, 10.5, 90F, 0F)
    )

    //TODO: CHANGE - or + depending on the map name
    fun setupAndStartRace(RaceName : String){
        //teleporting players

        if (!canSetup(RaceName)) return
        //setting up
        tpPlayerstoMap(RaceName)

        spawnVehicles(RaceName)

        //starting
        RaceStarted = true

        VehicleUtils.startRaceTicking()

        println("began race")
    }

    fun stopRace(RaceName : String){
        //TODO: IMPLEMENT
        //stopping
        RaceStarted = false

        despawnVehicles()
        resetAllValues()
        println("stopped race")
    }

    fun tpPlayerstoMap(raceName: String){
        val coords = RaceCoords[raceName]!!
        for ((index, onlineplayer) in Bukkit.getOnlinePlayers().withIndex()) {
            if (index < 5 ){
                println(index)
                onlineplayer.teleport(Location(Bukkit.getWorld("kartmap"),
                    (coords.x + (index) * 2.0),
                    coords.y,
                    coords.z - ((index) * 2.0),coords.yaw, coords.pitch)) //((index - 1) * 2.0),0.0,((index - 1) * 2.0))
            }else if(index in 5..8){
                //onlineplayer.teleport(RaceCoords[RaceName]!!.add(Vector((index - 1) * 2.0,0.0, 5 + (index - 1) * 2.0)))
                onlineplayer.teleport(Location(Bukkit.getWorld("kartmap"),
                    (coords.x + (5 + (index - 5) * 2.0)),
                    coords.y,
                    coords.z - ((index - 5) * 2.0),coords.yaw, coords.pitch)) //((index - 1) * 2.0),0.0,((index - 1) * 2.0))
            }
        }
    }

    fun spawnVehicles(raceName: String){
        for (onlineplayer in Bukkit.getOnlinePlayers()) {
            //TODO: IF PLAYER IS READY FOR A RACE
            spawnHorse(raceName, onlineplayer)
            spawnArmorStand(raceName, onlineplayer)

            playersInRace.add(onlineplayer)
        }
        println("finished spawning everything , players in race : $playersInRace")
    }
    fun despawnVehicles(){
        for (onlineplayer in playersInRace) {
            despawnHorse(onlineplayer)
            despawnArmorStand(onlineplayer)

            playersInRace.minus(onlineplayer)
        }
        println("finished despawning everything")
    }
    fun canSetup(raceName : String) : Boolean{
        if (Bukkit.getWorld(raceName) == null) return false
        if (RaceCoords[raceName] == null) return false

        return true
    }
}