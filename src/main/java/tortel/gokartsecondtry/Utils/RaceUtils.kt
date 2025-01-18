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
import tortel.gokartsecondtry.Utils.VehicleUtils.resetAllValues
import tortel.gokartsecondtry.Utils.VehicleUtils.resetValues
import tortel.gokartsecondtry.Utils.VehicleUtils.spawnItemDisplay
import tortel.gokartsecondtry.Utils.VehicleUtils.spawnHorse
import kotlin.math.cos
import kotlin.math.sin


object RaceUtils {
    var RaceStarted = false

    val PlayersInRace = mutableListOf<Player>()


    var chosenPlayers = mutableListOf<String>()

    //TODO: CHANGE - or + depending on the map name
    fun setupAndStartRace(TrackName: String){
     
        if (!canSetup(TrackName)) return
        println("setting up the race")
        //setting up
        tpPlayerstoMap(TrackName)

        RaceStarted = true

        object : BukkitRunnable() {
            override fun run() {
                spawnVehicles(TrackName)
            }
        }.runTaskLater(Main.instance!!, 5)

        //VehicleUtils.startRaceTicking()

        println("began race")
    }

    fun stopRace(RaceName : String){
        //TODO: IMPLEMENT
        RaceStarted = false
        println("Stopping race function")

        despawnVehicles()
        //resetAllValues()
        println("finished despawnvehciles()")

    }
    fun stopRaceIfEmpty(){
        if (PlayersInRace.size == 0){
            RaceStarted = false
            //println("$PlayersInRace ")
            despawnVehicles()
            //resetAllValues()
            println("stopped race2")
        }
    }

    fun tpPlayerstoMap(TrackName: String){
        println("Tping players to track")

        var i = 1
        Bukkit.getOnlinePlayers().forEach { plr ->
            val loc = getTrackCoords(TrackName, i)
            val players = Bukkit.getOnlinePlayers()
                .filter { !chosenPlayers.contains(it.name) } // Exclude already chosen players

            if (loc.world == null) return
            if (players.isEmpty()) return

            val selectedplr: Player = players.random()

            //chosenPlayers.plus(selectedplr.name)
            //chosenPlayers += selectedplr.name
            chosenPlayers.add(selectedplr.name)



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
        println("despawning ALL vehicles")
        for (onlineplayer in PlayersInRace) {
            despawnHorse(onlineplayer)
            despawnItemDisplay(onlineplayer)

        }
        resetAllValues()
        println("finished despawning all karts")
    }
    fun despawnVehiclesForPlayer(plr : Player){
        despawnHorse(plr)
        despawnItemDisplay(plr)


        resetValues(plr)

        //PlayersInRace.minus(plr)

        println("finished despawning vehicle for ${plr.name}")
    }
    fun canSetup(raceName : String) : Boolean{
        if (Bukkit.getWorld(raceName) == null) return false
        if (RaceStarted) return false // race already going on
        //if (!PlayerHorses.isEmpty()) return false
        //if (!PlayersInRace.isEmpty()) return false
        return true
    }

    fun onPlayerEnterGame(plr : Player){ // when player connects
        VehicleUtils.PlayersVelocities.put(plr, 0.0)
        VehicleUtils.PlayerRotations.put(plr, 0.0f)
    }
    fun onPlayerLeaveGame(plr : Player){ // when player Disconnects
        if (RaceStarted == true && PlayersInRace.contains(plr)){
            //TODO: REJOIN GAME
            despawnVehiclesForPlayer(plr)

            VehicleUtils.PlayersVelocities.remove(plr)
            VehicleUtils.PlayerRotations.remove(plr)

            stopRaceIfEmpty()
        }
    }
}