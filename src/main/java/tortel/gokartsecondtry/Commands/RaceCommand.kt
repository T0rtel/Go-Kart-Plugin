package tortel.gokartsecondtry.Commands



import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Suggest
import revxrsal.commands.bukkit.actor.BukkitCommandActor

import tortel.gokartsecondtry.Utils.RaceUtils
import javax.sound.midi.Track


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
    fun race(sender: Player, @Suggest("start", "stop") value : String, @Suggest("this") TrackName : String){ // , value : String
        if (!sender.isOp) return
        var track = TrackName
        if (value == "start"){
            if (track == "this") {
                track = sender.world.name
            }
            println("Track name is $track")
            RaceUtils.setupAndStartRace(track)
        }
        if (value == "stop"){
            //TODO:STOP RACE AND STUFF
            RaceUtils.stopRace(track)
        }

        return
    }
}