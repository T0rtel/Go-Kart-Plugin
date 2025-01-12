package tortel.gokartsecondtry.Utils


import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.RacingTracksConfigUtils.getTrackCoords
import tortel.gokartsecondtry.Utils.VehicleUtils.PlayerHorses
import tortel.gokartsecondtry.Utils.VehicleUtils.despawnItemDisplay
import tortel.gokartsecondtry.Utils.VehicleUtils.despawnHorse
import tortel.gokartsecondtry.Utils.VehicleUtils.despawnPigs
import tortel.gokartsecondtry.Utils.VehicleUtils.resetAllValues
import tortel.gokartsecondtry.Utils.VehicleUtils.spawnItemDisplay
import tortel.gokartsecondtry.Utils.VehicleUtils.spawnHorse
import kotlin.math.cos
import kotlin.math.sin


object RaceUtils {
    var RaceStarted = false

    val PlayersInRace = mutableListOf<Player>()

    val RaceCoords = mapOf<String, Location>(
        "kartmap" to Location(Bukkit.getWorld("kartmap"), 123.5, 32.0, 10.5, 90F, 0F)
    )

    var chosenPlayers = mutableListOf<String>()

    //TODO: CHANGE - or + depending on the map name
    fun setupAndStartRace(TrackName: String){
     
        if (!canSetup(TrackName)) return
        println("setting up the race")
        //setting up
        tpPlayerstoMap(TrackName)

        object : BukkitRunnable() {
            override fun run() {
                spawnVehicles(TrackName)
            }
        }.runTaskLater(Main.instance!!, 5)


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
            spawnItemDisplay(TrackName, onlineplayer)

            PlayersInRace.add(onlineplayer)
        }
        println("finished spawning everything , players in race : $PlayersInRace")
    }
    fun despawnVehicles(){
        for (onlineplayer in PlayersInRace) {
            despawnHorse(onlineplayer)
            despawnItemDisplay(onlineplayer)
            despawnPigs(onlineplayer)

            PlayersInRace.minus(onlineplayer)
        }
        println("finished despawning everything")
    }
    fun canSetup(raceName : String) : Boolean{
        if (Bukkit.getWorld(raceName) == null) return false
        if (RaceCoords[raceName] == null) return false
        if (!PlayerHorses.isEmpty()) return false
        if (!PlayersInRace.isEmpty()) return false
        return true
    }
}