package tortel.gokartsecondtry.Commands.TrackSetup

import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.annotation.Suggest
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import tortel.gokartsecondtry.Utils.RacingTracksConfigUtils
import tortel.gokartsecondtry.data.RaceTracksConfig


@Command("racepos")
@Description("Sets the racers positions when race starts.")
class RacePosCommands {
    @Subcommand("add")
    fun addRacePos(sender: Player, racerNumber : Int){
        if (!sender.isOp) return
        val loc = sender.location

        val replaced = RacingTracksConfigUtils.addRacePos(loc, racerNumber)
        if (replaced){
            sender.sendMessage("Successfully Replaced $racerNumber in ${loc.world.name} Track!")
        }else{
            sender.sendMessage("Successfully Added $racerNumber To ${loc.world.name} Track!")
        }

    }

    @Subcommand("remove")
    fun removeRacePos(sender: Player, racerNumber : Int){
        if (!sender.isOp) return
        val loc = sender.location

        RacingTracksConfigUtils.removeRacePos(loc, racerNumber)
        sender.sendMessage("Successfully Removed $racerNumber from ${loc.world.name} Track!")
    }

    @Subcommand("remove all")
    fun removeAllRacePos(sender: Player){
        if (!sender.isOp) return
        val loc = sender.location

        RacingTracksConfigUtils.removeAllRacePos(loc)
        sender.sendMessage("Successfully Removed ${loc.world.name} from Tracks!")
    }
}