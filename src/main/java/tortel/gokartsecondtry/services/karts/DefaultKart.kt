package tortel.gokartsecondtry.services.karts

import org.bukkit.entity.Entity
import tortel.gokartsecondtry.services.KartInterface

class DefaultKart : KartInterface {
    // Build kart object it self.
    // Horse, and model ext.
    override val KartHorseEntity: Entity? = null
    override val KartDisplayEntity: Entity? = null
}