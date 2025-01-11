package tortel.gokartsecondtry.Utils


import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import tortel.gokartsecondtry.Utils.RacingTracksConfigUtils.getTrackCoords
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

    var chosenPlayers = mutableListOf<String>()

    //TODO: CHANGE - or + depending on the map name
    fun setupAndStartRace(TrackName: String){
        //teleporting players

        if (!canSetup(TrackName)) return
        println("setting up the race")
        //setting up
        tpPlayerstoMap(TrackName)

        spawnVehicles(TrackName)

        //starting
        RaceStarted = true

        //VehicleUtils.startRaceTicking()

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

    fun tpPlayerstoMap(TrackName: String){
        println("Tping players to track")

        var i = 1
        Bukkit.getOnlinePlayers().forEach { plr ->
            val players = Bukkit.getOnlinePlayers()
                .filter { !chosenPlayers.contains(it.name) } // Exclude already chosen players

            if (players.isEmpty()) return

            val selectedplr: Player = players.random()

            //chosenPlayers.plus(selectedplr.name)
            //chosenPlayers += selectedplr.name
            chosenPlayers.add(selectedplr.name)

            val loc = getTrackCoords(TrackName, i)
            //println("want to tp ${selectedplr.name} to ${loc.x} ${loc.y} ${loc.z} ${loc.pitch} in which his number is $i  player")
            //println(chosenPlayers)
            selectedplr.teleport(loc)

            i++
        }

        chosenPlayers.clear()
        println("cleared chosen players")
        /*

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
         */

    }

    fun spawnVehicles(TrackName: String){
        for (onlineplayer in Bukkit.getOnlinePlayers()) {
            //TODO: IF PLAYER IS READY FOR A RACE
            spawnHorse(TrackName, onlineplayer)
            spawnArmorStand(TrackName, onlineplayer)

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