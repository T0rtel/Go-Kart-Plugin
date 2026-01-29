package tortel.gokartsecondtry.data

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import tortel.gokartsecondtry.Main
import java.io.File

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

        config = YamlConfiguration.loadConfiguration(file)
        //config.options().parseComments(true)

        try {
            Bukkit.broadcastMessage("sigmammamamamaa")
            for (raceTrack in Bukkit.getWorlds().map(World::getName)) {
                Bukkit.getLogger().warning(raceTrack + " fuhsndgf][dasg][vkopsdfds" + config.name + " s  " + config.currentPath)
                val va = config.get("raceData.track.$raceTrack")
                if (va != null) {
                    Memory.server?.raceData?.put(raceTrack,(va as RaceData))
                    Bukkit.getLogger().warning("pass2 ----------------------------------------------------------------------------------------------------")
                }else {
                    Bukkit.getLogger().warning("Key not found for race track: $raceTrack")
                }
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
            for (aaa in Memory.server?.raceData?.keys!!) {
                config.set("raceData.track.$aaa", Memory.server?.raceData?.get(aaa))
            }
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