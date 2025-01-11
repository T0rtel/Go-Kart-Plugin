package tortel.gokartsecondtry.Commands



import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.bukkit.actor.BukkitCommandActor

import tortel.gokartsecondtry.Utils.RaceUtils


class RaceCommand { //:CommandExecutor
    /*
     override fun onCommand(sender: CommandSender, command: Command, p2: String, args: Array<out String>): Boolean {
        if (sender !is Player || args.isNullOrEmpty()) return false
        if (args[0] == "start"){
            RaceUtils.setupAndStartRace(args[1])
        }
        if (args[0] == "stop"){
            //TODO:STOP RACE AND STUFF
            RaceUtils.stopRace(args[1])
        }

        return false
    }
     */
    @Command("race")
    fun race(sender: Player, value : String, TrackName : String){ // , value : String
        if (!sender.isOp) return
        if (value == "start"){
            println("Track name is $TrackName")
            RaceUtils.setupAndStartRace(TrackName)
        }
        if (value == "stop"){
            //TODO:STOP RACE AND STUFF
            RaceUtils.stopRace(TrackName)
        }

        return
    }
}