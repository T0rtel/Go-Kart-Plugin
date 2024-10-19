package tortel.gokartsecondtry.Utils

import org.bukkit.Bukkit
import org.bukkit.Location
import tortel.gokartsecondtry.Utils.VehicleUtils.spawnArmorStand
import tortel.gokartsecondtry.Utils.VehicleUtils.spawnHorse

object RaceUtils {
    val RaceCoords = mapOf<String, Location>(
        "kartmap" to Location(Bukkit.getWorld("kartmap"), 123.5, 32.0, 10.5, 90F, 0F)
    )

    //TODO: CHANGE - or + depending on the map name
    fun setupRace(RaceName : String){
        //teleporting players

        if (!canSetup(RaceName)) return

        tpPlayerstoMap(RaceName)

        spawnVehicles(RaceName)
    }

    fun tpPlayerstoMap(raceName: String){
        val coords = RaceCoords[raceName]!!
        for ((index, onlineplayer) in Bukkit.getOnlinePlayers().withIndex()) {
            if (index < 4 ){
                println(index)
                onlineplayer.teleport(Location(Bukkit.getWorld("kartmap"),
                    (coords.x + (index) * 2.0),
                    coords.y,
                    coords.z - ((index) * 2.0),coords.yaw, coords.pitch)) //((index - 1) * 2.0),0.0,((index - 1) * 2.0))
            }else if(index in 4..8){
                //onlineplayer.teleport(RaceCoords[RaceName]!!.add(Vector((index - 1) * 2.0,0.0, 5 + (index - 1) * 2.0)))
                onlineplayer.teleport(Location(Bukkit.getWorld("kartmap"),
                    (coords.x + (index) * 2.0),
                    coords.y,
                    coords.z - (5 + (index - 1) * 2.0),coords.yaw, coords.pitch)) //((index - 1) * 2.0),0.0,((index - 1) * 2.0))
            }
        }
    }

    fun spawnVehicles(raceName: String){
        for ((index, onlineplayer) in Bukkit.getOnlinePlayers().withIndex()) {
            spawnHorse(raceName, onlineplayer)
            spawnArmorStand(raceName, onlineplayer)
        }
    }
    fun canSetup(raceName : String) : Boolean{
        if (Bukkit.getWorld(raceName) == null) return false
        if (RaceCoords[raceName] == null) return false

        return true
    }
}