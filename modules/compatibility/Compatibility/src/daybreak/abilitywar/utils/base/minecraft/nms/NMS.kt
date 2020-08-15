package daybreak.abilitywar.utils.base.minecraft.nms

import daybreak.abilitywar.utils.base.minecraft.version.ServerVersion
import daybreak.abilitywar.utils.base.minecraft.version.VersionNotSupportedException
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class NMS private constructor() {
	companion object INSTANCE : INMS {
		private val INSTANCE: INMS = try {
			Class.forName("daybreak.abilitywar.utils.base.minecraft.nms." + ServerVersion.name + ".NMSImpl").asSubclass(INMS::class.java).getConstructor().newInstance()
		} catch (e: Exception) {
			throw VersionNotSupportedException()
		}

		@JvmStatic
		override fun respawn(player: Player) {
			INSTANCE.respawn(player)
		}

		@JvmStatic
		override fun clearTitle(player: Player) {
			INSTANCE.clearTitle(player)
		}

		@JvmStatic
		override fun sendTitle(player: Player, title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
			INSTANCE.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut)
		}

		@JvmStatic
		override fun sendActionbar(player: Player, string: String, fadeIn: Int, stay: Int, fadeOut: Int) {
			INSTANCE.sendActionbar(player, string, fadeIn, stay, fadeOut)
		}

		@JvmStatic
		override fun getAttackCooldown(player: Player): Float {
			return INSTANCE.getAttackCooldown(player)
		}

		@JvmStatic
		override fun rotateHead(receiver: Player, entity: Entity, yaw: Float, pitch: Float) {
			INSTANCE.rotateHead(receiver, entity, yaw, pitch)
		}

		@JvmStatic
		override fun newHologram(world: World, x: Double, y: Double, z: Double, text: String): IHologram {
			return INSTANCE.newHologram(world, x, y, z, text)
		}

		@JvmStatic
		override fun newHologram(world: World, x: Double, y: Double, z: Double): IHologram {
			return INSTANCE.newHologram(world, x, y, z)
		}

		@JvmStatic
		override fun getAbsorptionHearts(player: Player): Float {
			return INSTANCE.getAbsorptionHearts(player)
		}

		@JvmStatic
		override fun setAbsorptionHearts(player: Player, absorptionHearts: Float) {
			INSTANCE.setAbsorptionHearts(player, absorptionHearts)
		}

		@JvmStatic
		override fun broadcastEntityEffect(entity: Entity, status: Byte) {
			INSTANCE.broadcastEntityEffect(entity, status)
		}

		@JvmStatic
		override fun setLocation(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
			INSTANCE.setLocation(entity, x, y, z, yaw, pitch)
		}

		@JvmStatic
		override fun removeBoundingBox(armorStand: ArmorStand) {
			INSTANCE.removeBoundingBox(armorStand)
		}

		@JvmStatic
		override fun removeArrow(player: Player) {
			INSTANCE.removeArrow(player)
		}

		@JvmStatic
		override fun setInvisible(player: Player, invisible: Boolean) {
			INSTANCE.setInvisible(player, invisible)
		}

		@JvmStatic
		override fun setCooldown(player: Player, material: Material, ticks: Int) {
			INSTANCE.setCooldown(player, material, ticks)
		}

		@JvmStatic
		override fun hasCooldown(player: Player, material: Material): Boolean {
			return INSTANCE.hasCooldown(player, material)
		}

		@JvmStatic
		override fun fakeCollect(entity: Entity, item: Item) {
			INSTANCE.fakeCollect(entity, item)
		}

		@JvmStatic
		override fun clearActiveItem(livingEntity: LivingEntity?) {
			INSTANCE.clearActiveItem(livingEntity)
		}
	}
}