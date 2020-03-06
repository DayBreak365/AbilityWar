package daybreak.abilitywar.utils.base.minecraft;

import daybreak.abilitywar.utils.annotations.Beta;
import daybreak.abilitywar.utils.library.MaterialX;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class DamageUtil {

	private DamageUtil() {
	}

	public static boolean canDamage(Entity damager, Entity victim, EntityDamageEvent.DamageCause damageCause, double damage) {
		EntityDamageByEntityEvent fakeEvent = new EntityDamageByEntityEvent(damager, victim, damageCause, damage);
		Bukkit.getPluginManager().callEvent(fakeEvent);
		return !fakeEvent.isCancelled();
	}

	public static boolean canDamage(Entity victim, EntityDamageEvent.DamageCause damageCause, double damage) {
		EntityDamageEvent fakeEvent = new EntityDamageEvent(victim, damageCause, damage);
		Bukkit.getPluginManager().callEvent(fakeEvent);
		return !fakeEvent.isCancelled();
	}

	@Beta
	public static class Damage {

		private static final Map<Material, Double> damageMap = new HashMap<>();

		private static double register(MaterialX material, double value) {
			damageMap.putIfAbsent(material.parseMaterial(), value);
			return value;
		}

		public static final double TRIDENT = register(MaterialX.TRIDENT, 9);

		public static final double WOODEN_SWORD = register(MaterialX.WOODEN_SWORD, 4);
		public static final double STONE_SWORD = register(MaterialX.STONE_SWORD, 5);
		public static final double IRON_SWORD = register(MaterialX.IRON_SWORD, 6);
		public static final double GOLDEN_SWORD = register(MaterialX.GOLDEN_SWORD, 4);
		public static final double DIAMOND_SWORD = register(MaterialX.DIAMOND_SWORD, 7);

		public static final double WOODEN_SHOVEL = register(MaterialX.WOODEN_SHOVEL, 2.5);
		public static final double STONE_SHOVEL = register(MaterialX.STONE_SHOVEL, 3.5);
		public static final double IRON_SHOVEL = register(MaterialX.IRON_SHOVEL, 4.5);
		public static final double GOLDEN_SHOVEL = register(MaterialX.GOLDEN_SHOVEL, 2.5);
		public static final double DIAMOND_SHOVEL = register(MaterialX.DIAMOND_SHOVEL, 5.5);

		public static final double WOODEN_PICKAXE = register(MaterialX.WOODEN_PICKAXE, 2);
		public static final double STONE_PICKAXE = register(MaterialX.STONE_PICKAXE, 3);
		public static final double IRON_PICKAXE = register(MaterialX.IRON_PICKAXE, 4);
		public static final double GOLDEN_PICKAXE = register(MaterialX.GOLDEN_PICKAXE, 2);
		public static final double DIAMOND_PICKAXE = register(MaterialX.DIAMOND_PICKAXE, 5);

		public static final double WOODEN_AXE = register(MaterialX.WOODEN_AXE, 7);
		public static final double STONE_AXE = register(MaterialX.STONE_AXE, 9);
		public static final double IRON_AXE = register(MaterialX.IRON_AXE, 9);
		public static final double GOLDEN_AXE = register(MaterialX.GOLDEN_AXE, 7);
		public static final double DIAMOND_AXE = register(MaterialX.DIAMOND_AXE, 9);

		public static final double DEFAULT = 1;

		public static double of(Material material) {
			return damageMap.getOrDefault(material, 1.0);
		}

	}

	@Beta
	public static class DefensePoint {

		private static final Map<Material, Integer> defenseMap = new HashMap<>();

		public static int getDefensePoint(PlayerInventory inventory) {
			int defensePoint = 0;
			for (ItemStack stack : inventory.getArmorContents()) {
				if (stack != null) defensePoint += getDefensePoint(stack.getType());
			}
			return defensePoint;
		}

		public static int getToughness(PlayerInventory inventory) {
			int toughness = 0;
			for (ItemStack stack : inventory.getArmorContents()) {
				if (stack != null) toughness += getToughness(stack.getType());
			}
			return toughness;
		}

		public static int getDefensePoint(Material material) {
			return defenseMap.getOrDefault(material, 0) >> 4;
		}

		public static int getToughness(Material material) {
			return defenseMap.getOrDefault(material, 0) & 0xF;
		}

		private DefensePoint() {
		}

		static {
			put(MaterialX.LEATHER_HELMET, 1, 0);
			put(MaterialX.LEATHER_CHESTPLATE, 3, 0);
			put(MaterialX.LEATHER_LEGGINGS, 2, 0);
			put(MaterialX.LEATHER_BOOTS, 1, 0);

			put(MaterialX.GOLDEN_HELMET, 2, 0);
			put(MaterialX.GOLDEN_CHESTPLATE, 5, 0);
			put(MaterialX.GOLDEN_LEGGINGS, 3, 0);
			put(MaterialX.GOLDEN_BOOTS, 1, 0);

			put(MaterialX.CHAINMAIL_HELMET, 2, 0);
			put(MaterialX.CHAINMAIL_CHESTPLATE, 5, 0);
			put(MaterialX.CHAINMAIL_LEGGINGS, 4, 0);
			put(MaterialX.CHAINMAIL_BOOTS, 1, 0);

			put(MaterialX.IRON_HELMET, 2, 0);
			put(MaterialX.IRON_CHESTPLATE, 6, 0);
			put(MaterialX.IRON_LEGGINGS, 5, 0);
			put(MaterialX.IRON_BOOTS, 2, 0);

			put(MaterialX.DIAMOND_HELMET, 3, 2);
			put(MaterialX.DIAMOND_CHESTPLATE, 8, 2);
			put(MaterialX.DIAMOND_LEGGINGS, 6, 2);
			put(MaterialX.DIAMOND_BOOTS, 3, 2);

			put(MaterialX.TURTLE_HELMET, 2, 0);
		}

		private static void put(MaterialX material, int defensePoint, int toughness) {
			if (material.isSupported())
				defenseMap.put(material.parseMaterial(), (defensePoint << 4) | (toughness & 0xF));
		}

	}

}
