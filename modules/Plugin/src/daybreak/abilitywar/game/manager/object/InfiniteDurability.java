package daybreak.abilitywar.game.manager.object;

import com.google.common.collect.ImmutableSet;
import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.game.AbstractGame.GameUpdate;
import daybreak.abilitywar.game.AbstractGame.Observer;
import daybreak.abilitywar.utils.library.item.ItemLib;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InfiniteDurability implements Listener, Observer {

	private static final Set<String> toolNames = ImmutableSet.of("AXE", "HOE", "PICKAXE", "SPADE", "SWORD", "BOOTS", "LEGGINGS", "CHESTPLATE", "HELMET");
	private static final Set<String> itemNames = ImmutableSet.of("BOW", "SHEARS", "FISHING_ROD", "FLINT_AND_STEEL");

	private static final Set<Material> hasDurability;

	static {
		ImmutableSet.Builder<Material> builder = ImmutableSet.builder();
		for (Material material : Material.values()) {
			String name = material.toString();
			String[] split = name.split("_");
			if (split.length > 1) {
				if (toolNames.contains(split[1])) {
					builder.add(material);
				}
			} else {
				if (itemNames.contains(name)) {
					builder.add(material);
				}
			}
		}
		hasDurability = builder.build();
	}

	public InfiniteDurability() {
		Bukkit.getPluginManager().registerEvents(this, AbilityWar.getPlugin());
	}

	@EventHandler
	private void onItemDurability(PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (hasDurability.contains(e.getItem().getType())) {
				ItemLib.setDurability(e.getItem(), (short) 0);
			}
		}
	}

	@EventHandler
	private void onArmorDurability(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player player = (Player) e.getEntity();
			final ItemStack boots = player.getInventory().getBoots();
			if (boots != null && hasDurability.contains(boots.getType())) {
				ItemLib.setDurability(boots, (short) 0);
				player.getInventory().setBoots(boots);
			}
			final ItemStack leggings = player.getInventory().getLeggings();
			if (leggings != null && hasDurability.contains(leggings.getType())) {
				ItemLib.setDurability(leggings, (short) 0);
				player.getInventory().setLeggings(leggings);
			}
			final ItemStack chestplate = player.getInventory().getChestplate();
			if (chestplate != null && hasDurability.contains(chestplate.getType())) {
				ItemLib.setDurability(chestplate, (short) 0);
				player.getInventory().setChestplate(chestplate);
			}
			final ItemStack helmet = player.getInventory().getHelmet();
			if (helmet != null && hasDurability.contains(helmet.getType())) {
				ItemLib.setDurability(helmet, (short) 0);
				player.getInventory().setHelmet(helmet);
			}
		}
	}

	@Override
	public void update(GameUpdate update) {
		if (update.equals(GameUpdate.END)) {
			HandlerList.unregisterAll(this);
		}
	}

}
