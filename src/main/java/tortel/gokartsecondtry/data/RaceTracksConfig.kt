package tortel.gokartsecondtry.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
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

        config = YamlConfiguration()
        //config.options().parseComments(true)

        try{
            //config.save(file)
            config.load(file)
            config.set("working", true)
            Main.instance?.logger?.info("RACETRACKS Config Setup Status : ${config.get("working")}")
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
            config.set("working", false)
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
    fun setTrackNumberPosition(plr : Player,TrackName : String, number : String){
        val loc = plr.location
        val x = loc.x
        val y = loc.y
        val z = loc.z
        val yaw = loc.yaw
        val pitch = loc.pitch
        config.set("track.$TrackName.$number.x", x)
        config.set("track.$TrackName.$number.y", y)
        config.set("track.$TrackName.$number.z", z)
        config.set("track.$TrackName.$number.yaw", yaw)
        config.set("track.$TrackName.$number.pitch", pitch)
    }
    fun setplrVal(plr: OfflinePlayer, key: String, value: Any?): Any?{
        return config.set("cached.${plr.name}.${key}", value)
    }
    fun getplrBal(plr: OfflinePlayer): Int{
        return config.getInt("cached.${plr.name}.balance")
    }
    fun getplrGameVals(plr : Player, game : String) : List<Map<*, *>> {
        return config.getMapList("cached.${plr.name}.${game}")
    }


    fun getall(plr : Player){
        plr.sendMessage("${config.getStringList("cached")}")
        for (key in config.getStringList("cached")){
            plr.sendMessage(key)
        }
    }



}