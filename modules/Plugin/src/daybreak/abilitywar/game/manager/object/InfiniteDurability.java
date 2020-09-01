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
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InfiniteDurability implements Listener, Observer {

	private static final Set<String> toolNames = ImmutableSet.of("AXE", "HOE", "PICKAXE", "SPADE", "SWORD", "BOOTS", "LEGGINGS", "CHESTPLATE", "HELMET");
	private static final Set<String> itemNames = ImmutableSet.of("BOW", "SHEARS", "FISHING_ROD", "FLINT_AND_STEEL");

	private static final Set<Material> hasDurability;

	private static final EquipmentSlot[] ARMOUR_SLOTS = {
			EquipmentSlot.HEAD,
			EquipmentSlot.CHEST,
			EquipmentSlot.LEGS,
			EquipmentSlot.FEET
	};

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
	private void onItemDurability(final PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (hasDurability.contains(e.getItem().getType())) {
				ItemLib.setDurability(e.getItem(), (short) 0);
			}
		}
	}

	@EventHandler
	private void onEntityDamage(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final PlayerInventory playerInventory = ((Player) e.getEntity()).getInventory();
			for (final EquipmentSlot armourSlot : ARMOUR_SLOTS) {
				final ItemStack stack = playerInventory.getItem(armourSlot);
				if (stack != null && hasDurability.contains(stack.getType())) {
					ItemLib.setDurability(stack, (short) 0);
					playerInventory.setItem(armourSlot, stack);
				}
			}
		}
	}

	@EventHandler
	private void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
		onEntityDamage(e);
	}

	@EventHandler
	private void onEntityDamageByBlock(final EntityDamageByBlockEvent e) {
		onEntityDamage(e);
	}

	@Override
	public void update(GameUpdate update) {
		if (update.equals(GameUpdate.END)) {
			HandlerList.unregisterAll(this);
		}
	}

}
