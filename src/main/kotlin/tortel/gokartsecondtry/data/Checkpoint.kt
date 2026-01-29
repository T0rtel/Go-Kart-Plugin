package tortel.gokartsecondtry.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import org.bukkit.util.Vector

@SerializableAs("checkpoint")
class Checkpoint(loc: Location, length: Int, direction: Boolean, vector: Vector, numb : Int) : ConfigurationSerializable {

    var x: Double? = loc.x
    var z: Double? = loc.z
    var y: Double? = loc.y
    var loc: Location? = loc
    var length: Int? = length
    var direction: Boolean? = direction
    var track: String? = loc.world.name
    var vector : Vector? = vector
    var number : Int = numb
    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf(
            "x" to x!!,
            "y" to y!!,
            "z" to z!!,
            "length" to length!!,
            "direction" to direction!!,
            "track" to track!!,
            "vector" to vector!!,
            "number" to number
        )
    }
    companion object {
        fun deserialize(args: Map<String, Any>): Checkpoint {
            val loc = Location(
                Bukkit.getWorld(args["track"] as String),
                args["x"] as Double,
                args["y"] as Double,
                args["z"] as Double
            )
            return Checkpoint(
                loc,
                args["length"] as Int,
                args["direction"] as Boolean,
                args["vector"] as Vector,
                args["number"] as Int
            )
        }
    }
    /*fun Checkpoint(loc : Location, length : Int, dir : Boolean, vec : Vector) {
        x = loc.x
        z = loc.z
        y = loc.y
        this.length = length
        this.loc = loc
        this.direction = dir
        this.track = loc.world.name
        this@Checkpoint.vector = vec
    }*/
}