package tortel.gokartsecondtry.Utils.Race


import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.ScoreHelper
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.getVehicleManager
import tortel.gokartsecondtry.data.Memory
import tortel.gokartsecondtry.data.Memory.server
import tortel.gokartsecondtry.data.TempRaceData
import java.util.*


class RaceUtils {


    //TODO: CHANGE - or + depending on the map name
    //Crook/ idk what that means ^
    fun setupAndStartRace(TrackName: String, players: MutableList<Player>, id:String) {

        if (!canSetup(TrackName)) return
        println("setting up the race")
        //setting up
        tpPlayerstoMap(TrackName, id, players)
        for (player in players) {
            Memory.getPlayerMemory(player)?.time = 0.0
            Memory.getPlayerMemory(player)?.lapTime = 0.0
            Memory.getPlayerMemory(player)?.racePos = 0
            Memory.getPlayerMemory(player)?.inRace = true
            Memory.getPlayerMemory(player)?.currentItem = ""
            Memory.getPlayerMemory(player)?.shields = 0
            Memory.getPlayerMemory(player)?.raceID = id
        }
        setupBoxes(id,TrackName)
        object : BukkitRunnable() {
            override fun run() {
                VehicleUtils.startVehicleTicking(id)
                spawnVehicles(TrackName, id)
                object : BukkitRunnable() {
                    var ticks : Int = 0
                    override fun run() {
                        for (player in players) {
                            VehicleUtils.stop(player)
                        }
                        when (ticks) {
                            20 -> {
                                for (player in players) {
                                    player.sendTitle("§c§l3..","",2,20,2)
                                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f,1f)
                                }
                            }
                            40 -> {
                                for (player in players) {
                                    player.sendTitle("§6§l2..","",2,20,2)
                                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f,1f)
                                }
                            }
                            60 -> {
                                for (player in players) {
                                    player.sendTitle("§e§l1..","",2,20,2)
                                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f,1f)
                                }
                            }
                            80 -> {
                                for (player in players) {
                                    player.sendTitle("§a§lGO!.","",2,20,2)
                                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f,1.2f)
                                }
                                raceTick(TrackName, id)
                                for (player in players) {
                                    VehicleUtils.unstop(player)
                                }
                                this.cancel()

                            }
                        }

                        ticks++
                    }
                }.runTaskTimer(Main.instance!!,0,1)

            }
        }.runTaskLater(Main.instance!!, 15)
        println("began race")
    }
//todo: afk checker & troll prevent by ending race after x secs

    fun raceTick(RaceName: String, id : String) {
        //val b : HashMap<Int, Checkpoint> = Memory.server?.checkpoint?.get(RaceName)?.associateBy { it.number } as HashMap<Int, Checkpoint>
        //val finish : Checkpoint = Memory.server?.finishLine?.get(RaceName)!!
        val PlayersInRace: MutableList<Player> = Memory.server?.races?.get(id)?.PlayersInRace!!
        for (player in PlayersInRace) {
            Memory.getPlayerMemory(player)?.racePos = 0
            Memory.getPlayerMemory(player)?.laps = 0

            //create initial scoreboard
            val helper = ScoreHelper.createScore(player)
            helper.setTitle("&aCLOBBING YOUR NETWORK")
            helper.setSlot(7, "&7&m--------------------------------")
            helper.setSlot(6, "&aPosition: &f${PlayersInRace.size}&8/&7${PlayersInRace.size}")
            helper.setSlot(5, "&aLap: &f${Memory.getPlayerMemory(player)?.laps}")
            helper.setSlot(4, "&aLap Time: ${Memory.getPlayerMemory(player)?.lapTime?.div(20)}s")
            helper.setSlot(3, "&aOverall Time: ${Memory.getPlayerMemory(player)?.time?.div(20)}s ")

            helper.setSlot(2, "&e&lclobber.com")
            helper.setSlot(1, "&7&m--------------------------------")
        }
        val laps = Memory.server?.raceData?.get(RaceName)?.laps!!
        object : BukkitRunnable() {
            override fun run() {
                val sortedPlayers = PlayersInRace.sortedByDescending { Memory.getPlayerMemory(it)?.racePos!! + (Memory.getPlayerMemory(it)?.laps!! * 50)}
                val positions = mutableMapOf<Player, Int>()
                sortedPlayers.forEachIndexed { index, player ->
                    positions[player] = index + 1
                }

                //update scoreboard
                for (player in PlayersInRace) {
                    if (ScoreHelper.hasScore(player)) {
                        val helper = ScoreHelper.getByPlayer(player)
                        //player.sendMessage("yes")
                        Memory.getPlayerMemory(player)?.time = Memory.getPlayerMemory(player)?.time?.plus(5)!!
                        Memory.getPlayerMemory(player)?.lapTime = Memory.getPlayerMemory(player)?.lapTime?.plus(5)!!
                        helper?.setSlot(6, "&aPosition: &7${positions[player]}/${PlayersInRace.size}")
                        helper?.setSlot(5, "&aLap: &7${Memory.getPlayerMemory(player)?.laps}/${laps}")
                        helper?.setSlot(4, "&aLap Time: &7${Memory.getPlayerMemory(player)?.lapTime?.div(20)}s")
                        helper?.setSlot(3, "&aOverall Time: &7${Memory.getPlayerMemory(player)?.time?.div(20)}s ")

                    }
                }
            }
        }.runTaskTimer(Main.instance!!, 0, 5)
    }

    fun stopRace(id: String) {
        //TODO: IMPLEMENT
        println("Stopping race function")

        for (player in server?.races?.get(id)?.PlayersInRace!!) {
            var string = "§8DNF"
            if (server?.races?.get(id)?.finished?.contains(player)!!) {
            val place = server?.races?.get(id)?.finished?.indexOf(player)
            place?.plus(1)
            string = when (place) {
                1 -> "§e§l§o${place}st"
                2 -> "§7§l§o${place}nd"
                3 -> "§x§c§d§7§f§3§2§l§o${place}rd"
                else -> "§8§l${place}th"
            }}

        player.sendTitle("§a§lRACE FINISHED!","§fFinished $string")
        }
        despawnVehicles(id)
        Memory.server?.races?.remove(id)
        //resetAllValues()
        println("finished despawnvehciles()")

    }

    fun stopRaceIfEmpty(id : String) {
        if (Memory.server?.races?.get(id)?.PlayersInRace?.size == 0) {
          stopRace(id)
        }
    }

    fun boxOpen(plr: Player) {
        val speedlist = mutableListOf(5,5,3)
        val random = Random()
        Memory.getPlayerMemory(plr)?.currentItem = "rolling"
       val valu = random.nextInt(2,20)
        repeat(valu) {
            speedlist.add(1)
        }
        speedlist.add(2)
        speedlist.add(4)
        speedlist.add(5)
        speedlist.add(15)
        object : BukkitRunnable() {
            var ticks : Int = 0
            var speed = 5
            var pos = 0
            var speedpos = 0


            // ,"\uD83D\uDE80" (rocket) -->
            val list = mutableListOf<String>("❯❯❯","\uD83E\uDDE8","\uD83D\uDD25","⚡","\uD83C\uDF4C","\uD83D\uDD78\uFE0F","\uD83D\uDCA5","⛊","☁")
            override fun run() {

            if (ticks == speed) {
                pos++
                ticks = 0
                speedpos++
                plr.playSound(plr, Sound.UI_BUTTON_CLICK,1f,1f)
                if (pos > list.size-1) {
                    pos = 0

                }
                if (speedpos > speedlist.size-1) {
                    plr.sendActionBar("§8---§f${list[pos]}§8---")
                    hasItem(plr, list[pos])
                    plr.playSound(plr,Sound.ITEM_TOTEM_USE,1f,1f)
                    this.cancel()
                    return
                }
                speed = speedlist[speedpos]
            }


                val posa = list[pos]
                val size = list.size
                val beh = list[(pos - 1 + size) % size]
                val beh2 = list[(pos - 2 + size) % size]
                val beh3 = list[(pos - 3 + size) % size]
                val fron = list[(pos + 1) % size]
                val front2 = list[(pos + 2) % size]
                val front3 = list[(pos + 3) % size]


                plr.sendActionBar("§x§2§9§2§9§2§9§l§o$beh3§x§3§9§3§9§3§9§l§o$beh2§x§4§8§4§8§4§8§l§o$beh§f$posa§x§4§8§4§8§4§8§l§o$fron§x§3§9§3§9§3§9§l§o$front2§x§2§9§2§9§2§9§l§o$front3")

                ticks++;
            }
            }.runTaskTimer(Main.instance!!,0,1)
    }

    fun hasItem(plr: Player, item : String) {
            val itema = ItemStack(Material.LEATHER_HORSE_ARMOR)
            val meta = itema.itemMeta
        meta.setCustomModelData(2940)

            when (item) {
                "❯❯❯" -> {
                    Memory.getPlayerMemory(plr)?.currentItem = "speedb"
                }
               /* "\uD83D\uDE80" -> {
                    Memory.getPlayerMemory(plr)?.currentItem = "rocket"
                }*/
                "\uD83E\uDDE8" -> {
                    Memory.getPlayerMemory(plr)?.currentItem = "missile"
                }
                "\uD83D\uDD25" ->  {
                    Memory.getPlayerMemory(plr)?.currentItem = "fireball"
                }
                "⚡" -> {
                    Memory.getPlayerMemory(plr)?.currentItem = "lightning"
                }
                "\uD83C\uDF4C" -> {
                    Memory.getPlayerMemory(plr)?.currentItem = "banana"
                }
                "\uD83D\uDD78\uFE0F" -> {
                    Memory.getPlayerMemory(plr)?.currentItem = "spider"
                }
                "\uD83D\uDCA5" -> {
                    Memory.getPlayerMemory(plr)?.currentItem = "tnt"
                }
                "⛊" -> {
                    Memory.getPlayerMemory(plr)?.currentItem = "shield"
                }
                "☁" ->  {
                    Memory.getPlayerMemory(plr)?.currentItem = "smoke"
                }
                else -> {

                }
            }
        meta.persistentDataContainer.set(NamespacedKey(Main.instance!!, "box"), PersistentDataType.STRING, Memory.getPlayerMemory(plr)?.currentItem!!)
        meta.setDisplayName("§e§lItem Box §7(Right-Click)")
        itema.setItemMeta(meta)
        plr.inventory.addItem(itema)
            object : BukkitRunnable() {
                override fun run() {
                    object : BukkitRunnable() {
                        override fun run() {
                            if (Memory.getPlayerMemory(plr)?.currentItem?.isEmpty()!!) {

                                this.cancel()
                            }
                            plr.sendActionBar("§f$item")
                        }
                    }.runTaskTimer(Main.instance!!, 0,1)
                }
            }.runTaskLater(Main.instance!!, 10)
    }


    private fun tpPlayerstoMap(trackName: String,id:String, chosen : MutableList<Player>) {
        //todo: queue and shutf
        println("Tping players to track")
        val players : MutableList<Player> = mutableListOf()
        players.addAll(chosen)
        var i = 0
        players.forEach { plr ->
            val loc = Memory.server?.raceData?.get(trackName)?.racePosistions?.get(i)!!

            if (loc.world == null) return
            if (players.isEmpty()) return
            if (!plr.isOnline) return
            Memory.server?.races?.get(id)?.PlayersInRace?.add(plr)
            plr.teleport(loc)

            i++
        }
    }

    fun spawnVehicles(trackName: String, id: String) {

        for (onlineplayer in Memory.server?.races?.get(id)?.PlayersInRace!!) {
            //TODO: IF PLAYER IS READY FOR A RACE

            Main.vehicleManager?.getVehicle(onlineplayer)?.spawn(trackName)

        }
        println("finished spawning everything , players in race : ")
    }

    private fun despawnVehicles(id: String) {
        val VehicleManager = getVehicleManager()!!

        println("despawning ALL vehicles")
        for (plr in Memory.server?.races?.get(id)?.PlayersInRace!!) {
            VehicleManager.removeVehicle(plr)
        }

        println("finished despawning all karts")
    }

    fun canSetup(raceName: String): Boolean {
        if (Bukkit.getWorld(raceName) == null) return false

        return true
    }
    fun setupBoxes(id: String, world : String) {
    for (la in server?.raceData?.get(world)?.itemboxes!!) {
        val paa = la.world.spawn(la.clone().add(0.0,1.0,0.0), ItemDisplay::class.java)
        val a=  ItemStack(Material.LEATHER_HORSE_ARMOR)
        a.itemMeta.setCustomModelData(1223)
        paa.setItemStack(a)
               server?.races?.get(id)?.boxes?.put(la,paa)

    }
    }
    fun onPlayerLeaveGame(plr: Player) { // when player Disconnects
        val VehicleManager = getVehicleManager()!!

        if (Memory.server?.races?.containsKey(Memory.getPlayerMemory(plr)?.raceID!!)!!) {
            if (Memory.server?.races?.get(Memory.getPlayerMemory(plr)?.raceID!!)?.PlayersInRace?.contains(plr)!!) {
                //TODO: REJOIN GAME
                VehicleManager.removeVehicle(plr)

                stopRaceIfEmpty(Memory.getPlayerMemory(plr)?.raceID!!)
            }
        }
    }
}