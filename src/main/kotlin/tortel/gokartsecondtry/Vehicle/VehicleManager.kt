package tortel.gokartsecondtry.Vehicle

import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class VehicleManager {

    private val vehicles: MutableMap<UUID, Vehicle> = HashMap()

    // Get or create a vehicle for a player
    fun getVehicle(player: Player): Vehicle {
        return vehicles.computeIfAbsent(player.uniqueId) { Vehicle(player) }
    }
    fun VehicleExists(player: Player) : Boolean{
        if (vehicles.contains(player.uniqueId)){
            return true
        } else {

            return false
        }
    }

    // Remove a vehicle for a player
    fun removeVehicle(player: Player) {
        vehicles.remove(player.uniqueId)?.despawn()

    }

    // Spawn vehicles for all players
    fun removeAllVehicles() {
        vehicles.values.forEach { it.despawn() }
        vehicles.clear()
    }

    fun isEntityInVehicle(entity: Player): Boolean {
        return vehicles.containsKey(entity.uniqueId)
    }
}