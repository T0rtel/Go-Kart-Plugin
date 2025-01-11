package tortel.gokartsecondtry.data

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.RacingTracksConfigUtils
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

        config = YamlConfiguration()
        //config.options().parseComments(true)

        try{
            //config.save(file)
            config.load(file)
            config.set("working", true)
            save()
            Main.instance?.logger?.info("RACETRACKS Config Setup Status : ${config.get("working")}")

            //RacingTracksConfigUtils.loadRacingTracksConfig()
            //Main.instance?.logger?.info("${config.get("cached.TTortel.discordId")}")
        }catch (e:Exception){
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