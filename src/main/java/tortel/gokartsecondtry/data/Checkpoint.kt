package tortel.gokartsecondtry.data

import org.bukkit.Location
import org.bukkit.util.Vector

class Checkpoint(loc: Location, length: Int, direction: Boolean, vector: Vector, numb : Int) {

    var x: Double? = loc.x
    var z: Double? = loc.z
    var y: Double? = loc.y
    var loc: Location? = loc
    var length: Int? = length
    var direction: Boolean? = direction
    var track: String? = loc.world.name
    var vector : Vector? = vector
    var number : Int = numb

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