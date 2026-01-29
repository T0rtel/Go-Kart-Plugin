package tortel.gokartsecondtry.Commands



import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Suggest
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils
import tortel.gokartsecondtry.data.Memory
import tortel.gokartsecondtry.data.RaceData
import tortel.gokartsecondtry.data.TempQueue


class  RaceCommand { //:CommandExecutor
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
    @Command("createrace")
    @Description("Create a race")
    fun racea(sender: Player,@Suggest("worldName")TrackName : String,@Suggest("laps") value: String) { // , value : String
        if (!sender.isOp) return
        Memory.server?.raceData?.put(TrackName, RaceData(TrackName, Integer.parseInt(value)))
        sender.sendMessage("Added track $TrackName with $value laps, do /racepos to set other attributes")


    }

    @Command("debug")
    @Description("debug")
    fun debug(sender: Player,@Suggest("knockout","miniknockout")debug : String) {
        if (!sender.isOp) return
        when (debug) {
           "knockout" -> {
               VehicleUtils.knockout(sender)
           }
            "miniknockout" -> {
                VehicleUtils.miniKnockout(sender)
            }
        }
    }
        @Command("race")
    @Description("Race command, spawns karts and lets you ride them, make sure to use /racepos before using this.")
    fun race(sender: Player, @Suggest("forcestart","queue" ,"stop","list") value : String, TrackName : String){ // , value : String
       // TODO: IF PLAYER IS IN RACE DONT LET HIM GO TO GARAGE
        //TODO: FIGURE OUT HOW TO SETUP MULTIPLE RACES THAT CAN RUN IN THE SAME TIME
        var track = TrackName
        if (value == "forcestart"){
            if (!sender.isOp) return
            if (track == "this") {
                track = sender.world.name
            }
            if (Memory.server?.queues?.containsKey(track)!!) {
                Memory.server?.queues?.get(track)?.forceStart = true
                sender.sendMessage("§7Successfully force started queue for §6§l$track")

            } else {
                Memory.server?.queues?.put(track, TempQueue(sender, track,sender.location).TempQueue())
                Memory.server?.queues?.get(track)?.forceStart = true
                sender.sendMessage("§7Successfully force started a game for §6§l$track")

            }
        }
        if (value == "stop"){
            if (!sender.isOp) return
            println("sotting race here")
            Memory.server?.races?.get(track)?.raceUtil?.stopRace(track)
        }
            if (value == "list") {
                sender.sendMessage(" click on the id to copy")
                for (va in Memory.server?.races?.keys!!) {
                    val textComponent: TextComponent = Component.text("${Memory.server?.races?.get(va)?.track} | $va")
                   textComponent.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, va))
                    sender.sendMessage(textComponent)
                }
            }
        if (value == "queue") {
        if (Memory.server?.queues?.containsKey(track)!!) {
            Memory.server?.queues?.get(track)?.players?.add(sender)
            sender.sendMessage("§7Successfully added to queue for §6§l$track")
        } else {
            Memory.server?.queues?.put(track, TempQueue(sender, track,sender.location).TempQueue())
        }
        }

        return
    }
}