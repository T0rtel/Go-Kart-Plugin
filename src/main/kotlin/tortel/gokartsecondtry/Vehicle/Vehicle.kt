package tortel.gokartsecondtry.Vehicle

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import tortel.gokartsecondtry.Utils.Race.RaceUtils.PlayersInRace
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.spawnHorse
import tortel.gokartsecondtry.Utils.Vehicle.VehicleUtils.spawnItemDisplay


class Vehicle(private val player: Player) {
    lateinit var VehicleHorse : Entity
    lateinit var VehicleItemDisplays : List<Entity>

    val owner = player

    var velocity = 0.0
    var rotation = 0.0f

    var isAccelerating = false
    var isBraking = false
    var isDrifting = false

    var DriftingDir = ""

    var steering = 0.0 // -1 to 1 (-1 is left, 1 is right)

    // Example: Spawn a vehicle for the player
    fun spawn(TrackName : String) {
        // Logic to spawn the vehicle for the player
        player.sendMessage("Kart spawned!")

        VehicleHorse = spawnHorse(TrackName, player)
        VehicleItemDisplays = spawnItemDisplay(TrackName, player)

        //VehicleHorse = Horse
        //VehicleItemDisplays = ItemDisplays

        PlayersInRace.add(player)
    }

    // Example: Despawn the vehicle
    fun despawn() {
        // Logic to despawn the vehicle
        VehicleHorse.remove()
        VehicleItemDisplays.forEach { it.remove() }
        player.sendMessage("Kart despawned!")
    }
}