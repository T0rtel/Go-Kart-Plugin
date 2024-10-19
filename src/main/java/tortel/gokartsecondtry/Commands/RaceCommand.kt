package tortel.gokartsecondtry.Commands


import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tortel.gokartsecondtry.Race.RaceUtils


class RaceCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, p2: String, args: Array<out String>?): Boolean {
        if (sender !is Player || args.isNullOrEmpty()) return false
        if (args[0] == "start"){
            RaceUtils.setupRace(args[1])
        }
        if (args[0] == "stop"){
            //TODO:STOP RACE AND STUFF
        }

        return false
    }
}