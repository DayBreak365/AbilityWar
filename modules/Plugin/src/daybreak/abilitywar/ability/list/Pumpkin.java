package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.decorator.ActiveHandler;
import daybreak.abilitywar.config.ability.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.game.interfaces.TeamGame;
import daybreak.abilitywar.game.manager.object.DeathManager;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.base.math.LocationUtil;
import daybreak.abilitywar.utils.library.MaterialX;
import daybreak.abilitywar.utils.library.SoundLib;
import daybreak.abilitywar.utils.library.item.EnchantLib;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@AbilityManifest(name = "호박", rank = Rank.C, species = Species.OTHERS, explain = {
		"철괴를 우클릭하면 주변 30블록 내에 있었던 플레이어들에게 $[DurationConfig]초간",
		"귀속 저주가 걸린 호박을 씌웁니다. $[COOLDOWN_CONFIG]",
		"§5♪ §f호박 같은 네 얼굴 §5♪"
})
public class Pumpkin extends AbilityBase implements ActiveHandler {

	public static final SettingObject<Integer> COOLDOWN_CONFIG = abilitySettings.new SettingObject<Integer>(Pumpkin.class, "Cooldown", 80,
			"# 쿨타임") {

		@Override
		public boolean condition(Integer value) {
			return value >= 0;
		}

		@Override
		public String toString() {
			return Formatter.formatCooldown(getValue());
		}

	};

	public static final SettingObject<Integer> DurationConfig = abilitySettings.new SettingObject<Integer>(Pumpkin.class, "Duration", 15,
			"# 지속 시간") {

		@Override
		public boolean condition(Integer value) {
			return value >= 1;
		}

	};

	public Pumpkin(Participant participant) {
		super(participant);
	}

	private final Cooldown cooldownTimer = new Cooldown(COOLDOWN_CONFIG.getValue());

	private final AbilityTimer music = new AbilityTimer(32) {

		private List<Player> players;

		private int count;

		@Override
		public void onStart() {
			this.players = new ArrayList<>(Pumpkin.this.players.keySet());
			players.add(getPlayer());
			count = 1;
		}

		@Override
		public void run(int count) {
			switch (this.count) {
				case 1:
					SoundLib.BELL.playInstrument(players, Note.natural(0, Tone.D));
					break;
				case 3:
				case 20:
					SoundLib.BELL.playInstrument(players, Note.natural(0, Tone.E));
					break;
				case 13:
					SoundLib.BELL.playInstrument(players, Note.natural(0, Tone.A));
					SoundLib.BELL.playInstrument(players, Note.natural(0, Tone.E));
					break;
				case 4:
				case 7:
				case 32:
					SoundLib.BELL.playInstrument(players, Note.natural(0, Tone.D));
					SoundLib.BELL.playInstrument(players, Note.sharp(1, Tone.F));
					break;
				case 12:
				case 22:
					SoundLib.BELL.playInstrument(players, Note.sharp(1, Tone.F));
					break;
				case 10:
				case 31:
					SoundLib.BELL.playInstrument(players, Note.natural(1, Tone.G));
					break;
				case 23:
				case 26:
					SoundLib.BELL.playInstrument(players, Note.natural(0, Tone.A));
					SoundLib.BELL.playInstrument(players, Note.natural(1, Tone.G));
					break;
				case 29:
					SoundLib.BELL.playInstrument(players, Note.natural(1, Tone.A));
					break;
			}

			this.count++;
		}

	}.setPeriod(TimeUnit.TICKS, 3).register();

	private final Predicate<Entity> predicate = new Predicate<Entity>() {
		@Override
		public boolean test(Entity entity) {
			if (entity.equals(getPlayer())) return false;
			if (entity instanceof Player) {
				if (!getGame().isParticipating(entity.getUniqueId())
						|| (getGame() instanceof DeathManager.Handler && ((DeathManager.Handler) getGame()).getDeathManager().isExcluded(entity.getUniqueId()))
						|| !getGame().getParticipant(entity.getUniqueId()).attributes().TARGETABLE.getValue()) {
					return false;
				}
				if (getGame() instanceof TeamGame) {
					final TeamGame teamGame = (TeamGame) getGame();
					final Participant entityParticipant = getGame().getParticipant(entity.getUniqueId());
					return !teamGame.hasTeam(entityParticipant) || !teamGame.hasTeam(getParticipant()) || (!teamGame.getTeam(entityParticipant).equals(teamGame.getTeam(getParticipant())));
				}
			}
			return true;
		}
	};

	private Map<Player, ItemStack> players;

	private final Duration durationTimer = new Duration(DurationConfig.getValue(), cooldownTimer) {

		@Override
		public void onDurationStart() {
			players = new HashMap<>();
			for (Player p : LocationUtil.getNearbyEntities(Player.class, getPlayer().getLocation(), 30, 30, predicate)) {
				players.put(p, p.getInventory().getHelmet());
			}
			music.start();
		}

		@Override
		public void onDurationProcess(int seconds) {
			ItemStack pumpkin = getPumpkin(seconds);
			players.keySet().forEach(p -> p.getInventory().setHelmet(pumpkin));
		}

		@Override
		public void onDurationEnd() {
			players.forEach((Player p, ItemStack stack) -> p.getInventory().setHelmet(stack));
		}

		@Override
		public void onDurationSilentEnd() {
			players.forEach((Player p, ItemStack stack) -> p.getInventory().setHelmet(stack));
		}

		private ItemStack getPumpkin(int time) {
			ItemStack stack = MaterialX.CARVED_PUMPKIN.parseItem();
			ItemMeta meta = stack.getItemMeta();
			meta.setDisplayName("§6호박");
			meta.setLore(Arrays.asList("§d♪ §f호박 같은 네 얼굴 §d♪", "§f남은 시간§5: §d" + time + "초"));
			stack.setItemMeta(meta);
			EnchantLib.BINDING_CURSE.addUnsafeEnchantment(stack, 1);
			return stack;
		}

	};

	@SubscribeEvent
	private void onInventoryClick(InventoryClickEvent e) {
		if (durationTimer.isRunning() && players.containsKey(e.getWhoClicked()) && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PUMPKIN && e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "호박")) {
			e.setCancelled(true);
		}
	}

	@Override
	public boolean ActiveSkill(Material material, ClickType clickType) {
		if (material == Material.IRON_INGOT && clickType == ClickType.RIGHT_CLICK && !durationTimer.isDuration() && !cooldownTimer.isCooldown()) {
			durationTimer.start();
			return true;
		}
		return false;
	}

}
