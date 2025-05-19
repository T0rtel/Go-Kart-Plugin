package tortel.gokartsecondtry.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import tortel.gokartsecondtry.Main
import java.io.File
import java.util.ArrayList

object RaceTracksConfig {

    private var file: File = File("")
    private var config: YamlConfiguration = YamlConfiguration()

    fun FileDoesntExist(){
        println("file doesnt exist")
        Main.instance?.saveResource("racetracks.yml", false)
        Main.instance?.logger?.warning("RACETRACKS CONFIG FOUND")
    }

    fun load(){
        file = File(Main.instance!!.dataFolder.path, "racetracks.yml")

        if (!file.exists()){
            FileDoesntExist()
        }

        config = YamlConfiguration()
        //config.options().parseComments(true)

        try {
            //config.save(file)
            config.load(file)
            config.set("working", true)
            save()
            Main.instance?.logger?.info("RACETRACKS Config Setup Status : ${config.get("working")}")
            val config = RaceTracksConfig.getConfig()
            for (raceTrack in Bukkit.getWorlds().map(World::getName)) {
                val checkpoints = config.getConfigurationSection("racetracks.$raceTrack.C") ?: return
                val checkList : MutableList<Checkpoint> = ArrayList()
                checkpoints.getKeys(false).forEach { key ->
                    val length = config.getInt("racetracks.$raceTrack.C.$key.length")
                    val direction = config.getString("racetracks.$raceTrack.C.$key.dir") == "z"
                    val x = config.getDouble("racetracks.$raceTrack.C.$key.x")
                    val y = config.getDouble("racetracks.$raceTrack.C.$key.y")
                    val z = config.getDouble("racetracks.$raceTrack.C.$key.z")

                    val x2 = config.getDouble("racetracks.$raceTrack.C.$key.V.x")
                    val y2 = config.getDouble("racetracks.$raceTrack.C.$key.V.y")
                    val z2 = config.getDouble("racetracks.$raceTrack.C.$key.V.z")
                    val vector = Vector(x2,y2,z2)

                    val checkpoint = Checkpoint(Location(Bukkit.getWorld(raceTrack), x, y, z), length, direction, vector, key.toInt())
                    checkList.add(checkpoint)
                }
                val length = config.getInt("racetracks.$raceTrack.start.length")
                val direction = config.getString("racetracks.$raceTrack.start.dir") == "z"
                val x = config.getDouble("racetracks.$raceTrack.start.x")
                val y = config.getDouble("racetracks.$raceTrack.start.y")
                val z = config.getDouble("racetracks.$raceTrack.start.z")
                val x2 = config.getDouble("racetracks.$raceTrack.start.V.x")
                val y2 = config.getDouble("racetracks.$raceTrack.start.V.y")
                val z2 = config.getDouble("racetracks.$raceTrack.start.V.z")
                val vector = Vector(x2,y2,z2)
                val checkpoint = Checkpoint(Location(Bukkit.getWorld(raceTrack), x, y, z), length, direction, vector, 0)

                Memory.server?.finishLine?.put(raceTrack, checkpoint)
                Memory.server?.checkpoint?.put(raceTrack,checkList)
            }
                //RacingTracksConfigUtils.loadRacingTracksConfig()
                //Main.instance?.logger?.info("${config.get("cached.TTortel.discordId")}")
            } catch (e:Exception){
                e.printStackTrace()
            }


    }
    fun reload(){
        file = File(Main.instance!!.dataFolder.path, "racetracks.yml")

        if (!file.exists()){
            FileDoesntExist()
        }

        config = YamlConfiguration()

        try{
            config.load(file)
            config.set("working", true)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    fun save(){
        try{
            //config.set("working", false)
            config.save(file)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    fun getConfig(): YamlConfiguration{
        return config
    }
    fun set(key: String, value: Any?){
        config.set(key, value)
    }
    fun get(key : String): Any? {
        return config.get(key)
    }
    fun getTrackPositions(plr : Player,TrackName : String): List<String>{
        return config.getStringList("track.$TrackName")
    }


    fun getall(plr : Player){
        if (config.contains("racetracks")){
            println("racetracks found.")
        }

        plr.sendMessage("${config.getList("racetracks.kartmap")}")

    }



}