package tortel.gokartsecondtry.Utils.Race


import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.Race.RacingTracksConfigUtils.getTrackCoords
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.getVehicleManager


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
        }.runTaskLater(Main.instance!!, 10)

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


            chosenPlayers.add(selectedplr.name)

            selectedplr.teleport(loc)

            i++
        }

        chosenPlayers.clear()
    }

    fun spawnVehicles(TrackName: String){
        for (onlineplayer in Bukkit.getOnlinePlayers()) {
            //TODO: IF PLAYER IS READY FOR A RACE

            Main.vehicleManager?.getVehicle(onlineplayer)?.spawn(TrackName)

        }
        println("finished spawning everything , players in race : $PlayersInRace")
    }
    fun despawnVehicles(){
        val VehicleManager = getVehicleManager()!!

        println("despawning ALL vehicles")
        VehicleManager.removeAllVehicles()

        println("finished despawning all karts")
    }

    fun canSetup(raceName : String) : Boolean{
        if (Bukkit.getWorld(raceName) == null) return false
        if (RaceStarted) return false // race already going on

        return true
    }

    fun onPlayerLeaveGame(plr : Player){ // when player Disconnects
        val VehicleManager = getVehicleManager()!!

        if (RaceStarted == true && PlayersInRace.contains(plr)){
            //TODO: REJOIN GAME
            VehicleManager.removeVehicle(plr)

            stopRaceIfEmpty()
        }
    }
}