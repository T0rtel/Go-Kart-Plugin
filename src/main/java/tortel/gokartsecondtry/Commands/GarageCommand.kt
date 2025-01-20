package tortel.gokartsecondtry.Commands

import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.annotation.Suggest
import tortel.gokartsecondtry.Main
import tortel.gokartsecondtry.Utils.Garage.GarageVehicleUtils
import tortel.gokartsecondtry.Utils.Garage.GarageVehicleUtils.PlayersInGarage


class GarageCommand {


    @Command("garage")
    @Description("Go to Garage, where you can Customize your Kart.")
    fun goToGarage(sender: Player){
        println("plr wanna go garage wow")
        val plrloc = Main.instance!!.config.getLocation("spawn.player") ?: return
        val vehicleloc = Main.instance!!.config.getLocation("spawn.vehicle") ?: return

        sender.teleport(plrloc)
        GarageVehicleUtils.playerEnterGarage(sender, vehicleloc)
        PlayersInGarage.add(sender)
    }
    @Command("garage")
    @Subcommand("leave")
    fun leavegarage(sender: Player){
        if (!PlayersInGarage.contains(sender)) return

        GarageVehicleUtils.playerLeaveGarage(sender)
        println("plr wanna leave")
    }

    @Command("garage")
    @Subcommand("setspawn")
    fun SetSpawnOf(sender: Player, @Suggest("vehicle", "player") PlayerOrVehicle : String){
        println("set spawn of type $PlayerOrVehicle to here")
        val value = PlayerOrVehicle.lowercase()
        val config = Main.instance!!.config
        config.set("spawn.$value", sender.location)
        Main.instance!!.saveConfig()
    }


}