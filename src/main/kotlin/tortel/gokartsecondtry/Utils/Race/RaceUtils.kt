package tortel.gokartsecondtry.Utils.Race


import io.papermc.paper.command.brigadier.argument.ArgumentTypes.player
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.Race.RacingTracksConfigUtils.getTrackCoords
import tortel.gokartsecondtry.Utils.ScoreHelper
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.getVehicleManager
import tortel.gokartsecondtry.data.Checkpoint
import tortel.gokartsecondtry.data.Memory


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
                raceTick(TrackName)
            }
        }.runTaskLater(Main.instance!!, 10)



        println("began race")
    }

    fun raceTick(RaceName: String) {
        //val b : HashMap<Int, Checkpoint> = Memory.server?.checkpoint?.get(RaceName)?.associateBy { it.number } as HashMap<Int, Checkpoint>
        //val finish : Checkpoint = Memory.server?.finishLine?.get(RaceName)!!
        for (player in PlayersInRace) {
            Memory.getPlayerMemory(player)?.racePos = 0
            Memory.getPlayerMemory(player)?.laps = 0
            //create initial scoreboard
            val helper = ScoreHelper.createScore(player)
            helper.setTitle("&aCLOBBING YOUR NETWORK")
            helper.setSlot(5, "&7&m--------------------------------")
            helper.setSlot(4, "&aPosition: ${PlayersInRace.size}/${PlayersInRace.size}")
            helper.setSlot(3,"&aLap: ${Memory.getPlayerMemory(player)?.laps}")
            helper.setSlot(2, "clobber.com")
            helper.setSlot(1, "&7&m--------------------------------")
        }

        object : BukkitRunnable() {
            override fun run() {
                val sortedPlayers = PlayersInRace.sortedByDescending { Memory.getPlayerMemory(it)?.racePos }
                val positions = mutableMapOf<Player, Int>()
                sortedPlayers.forEachIndexed { index, player ->
                    positions[player] = index + 1
                }

                //update scoreboard
                for (player in PlayersInRace) {
                    if (ScoreHelper.hasScore(player)) {
                        val helper = ScoreHelper.getByPlayer(player)
                    //player.sendMessage("yes")
                        helper?.setSlot(4, "&aPosition: ${positions[player]}/${PlayersInRace.size}")
                        helper?.setSlot(3, "&aLap: ${Memory.getPlayerMemory(player)?.laps}")
                    }
                }
            }
        }.runTaskTimer(Main.instance!!, 0, 5 )
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