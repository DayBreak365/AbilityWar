package daybreak.abilitywar.game.list.mix.synergy.list;

import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.ability.decorator.ActiveHandler;
import daybreak.abilitywar.config.ability.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.game.list.mix.synergy.Synergy;
import daybreak.abilitywar.game.module.DeathManager;
import daybreak.abilitywar.game.team.interfaces.Teamable;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.base.math.LocationUtil;
import daybreak.abilitywar.utils.base.minecraft.FallingBlocks;
import daybreak.abilitywar.utils.base.minecraft.FallingBlocks.Behavior;
import daybreak.abilitywar.utils.base.minecraft.version.ServerVersion;
import daybreak.abilitywar.utils.library.PotionEffects;
import daybreak.abilitywar.utils.library.SoundLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@AbilityManifest(name = "죽음의 손아귀", rank = Rank.A, species = Species.OTHERS, explain = {
		"철괴를 우클릭하면 공중으로 올라갔다 마지막으로 때린 플레이어의 방향으로 날아가",
		"내려 찍으며 주변의 플레이어들에게 대미지를 입히고 날려보냅니다. $[COOLDOWN_CONFIG]"
})
public class DeathGrasp extends Synergy implements ActiveHandler {

	public static final SettingObject<Integer> COOLDOWN_CONFIG = synergySettings.new SettingObject<Integer>(DeathGrasp.class, "cooldown", 120, "# 쿨타임") {

		@Override
		public boolean condition(Integer value) {
			return value >= 0;
		}

		@Override
		public String toString() {
			return Formatter.formatCooldown(getValue());
		}

	};

	public static final SettingObject<Integer> DAMAGE_CONFIG = synergySettings.new SettingObject<Integer>(DeathGrasp.class, "damage", 15, "# 대미지") {

		@Override
		public boolean condition(Integer value) {
			return value >= 1;
		}

	};

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
				if (getGame() instanceof Teamable) {
					final Teamable teamGame = (Teamable) getGame();
					final Participant entityParticipant = teamGame.getParticipant(entity.getUniqueId()), participant = getParticipant();
					return !teamGame.hasTeam(entityParticipant) || !teamGame.hasTeam(participant) || (!teamGame.getTeam(entityParticipant).equals(teamGame.getTeam(participant)));
				}
			}
			return true;
		}
	};

	private final Cooldown cooldownTimer = new Cooldown(COOLDOWN_CONFIG.getValue());
	private final AbilityTimer fallBlockTimer = new AbilityTimer(5) {

		Location center;

		@Override
		public void onStart() {
			this.center = getPlayer().getLocation();
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run(int count) {
			int distance = 6 - count;

			if (ServerVersion.getVersion() >= 13) {
				for (Block block : LocationUtil.getBlocks2D(center, distance, true, true, true)) {
					if (block.getType() == Material.AIR) block = block.getRelative(BlockFace.DOWN);
					if (block.getType() == Material.AIR) continue;
					Location location = block.getLocation().add(0, 1, 0);
					FallingBlocks.spawnFallingBlock(location, block.getType(), false, getPlayer().getLocation().toVector().subtract(location.toVector()).multiply(-0.1).setY(Math.random()), Behavior.FALSE);
				}
			} else {
				for (Block block : LocationUtil.getBlocks2D(center, distance, true, true, true)) {
					if (block.getType() == Material.AIR) block = block.getRelative(BlockFace.DOWN);
					if (block.getType() == Material.AIR) continue;
					Location location = block.getLocation().add(0, 1, 0);
					FallingBlocks.spawnFallingBlock(location, block.getType(), block.getData(), false, getPlayer().getLocation().toVector().subtract(location.toVector()).multiply(-0.1).setY(Math.random()), Behavior.FALSE);
				}
			}

			for (Damageable damageable : LocationUtil.getNearbyEntities(Damageable.class, center, 5, 5, predicate)) {
				if (!damageable.equals(getPlayer())) {
					damageable.setVelocity(center.toVector().subtract(damageable.getLocation().toVector()).multiply(-1).setY(1.2));
				}
			}
		}

	}.setPeriod(TimeUnit.TICKS, 4).register();
	private final List<Runnable> SOUND_RUNNABLES = Arrays.asList(
			() -> SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.G)),
			() -> {
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.G));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.flat(0, Note.Tone.B));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.D));
			},
			() -> {
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.G));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.flat(0, Note.Tone.B));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.C));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.flat(0, Note.Tone.E));
			},
			() -> {
				SoundLib.PIANO.playInstrument(getPlayer(), Note.sharp(0, Note.Tone.F));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.A));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.C));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.flat(0, Note.Tone.E));
			},
			() -> {
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.G));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.flat(0, Note.Tone.B));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.D));
			},
			() -> {
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.G));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.flat(0, Note.Tone.B));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.D));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(1, Note.Tone.G));
			},
			() -> {
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.G));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.flat(0, Note.Tone.B));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.sharp(0, Note.Tone.D));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(1, Note.Tone.G));
			},
			() -> {
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.A));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.natural(0, Note.Tone.D));
				SoundLib.PIANO.playInstrument(getPlayer(), Note.sharp(1, Note.Tone.F));
			}
	);
	private boolean noFallDamage = false;
	private boolean skillEnabled = false;
	private Player lastVictim = null;
	private final AbilityTimer follow = new AbilityTimer(60) {

		private Player target;

		@Override
		public void onStart() {
			this.target = lastVictim;
		}

		@Override
		protected void run(int count) {
			getPlayer().setVelocity(target.getLocation().toVector().subtract(getPlayer().getLocation().toVector()).normalize().multiply(3.5));
		}
	}.setPeriod(TimeUnit.TICKS, 4).register();
	private final AbilityTimer skill = new AbilityTimer(4) {

		@Override
		public void onStart() {
			noFallDamage = true;
			getPlayer().setVelocity(getPlayer().getVelocity().add(new Vector(0, 4, 0)));
		}

		@Override
		public void run(int count) {
		}

		@Override
		public void onEnd() {
			skillEnabled = true;
			follow.start();
		}

	}.setPeriod(TimeUnit.TICKS, 10).register();
	private int stack = 0;

	public DeathGrasp(Participant participant) {
		super(participant);
	}

	@Override
	public boolean ActiveSkill(@NotNull Material material, @NotNull ClickType clickType) {
		if (material == Material.IRON_INGOT) {
			if (clickType == ClickType.RIGHT_CLICK) {
				if (!cooldownTimer.isCooldown()) {
					if (lastVictim != null) {
						for (Player player : LocationUtil.getNearbyEntities(Player.class, getPlayer().getLocation(), 5, 5, null)) {
							SoundLib.ENTITY_WITHER_SPAWN.playSound(player);
						}
						SoundLib.ENTITY_WITHER_SPAWN.playSound(getPlayer());
						skill.start();
						cooldownTimer.start();
						return true;
					} else {
						getPlayer().sendMessage("§4마지막으로 때렸던 플레이어가 존재하지 않습니다.");
					}
				}
			}
		}

		return false;
	}

	@SubscribeEvent
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getEntity().equals(getPlayer())) {
				if (noFallDamage) {
					if (e.getCause().equals(DamageCause.FALL)) {
						e.setCancelled(true);
						noFallDamage = false;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getPlayer().equals(getPlayer())) {
			if (skillEnabled) {
				final Block block = getPlayer().getLocation().getBlock(), downBlock = getPlayer().getLocation().subtract(0, 1, 0).getBlock();
				if (!block.getType().equals(Material.AIR) || !downBlock.getType().equals(Material.AIR)) {
					skillEnabled = false;
					final double damage = DAMAGE_CONFIG.getValue();
					for (Damageable d : LocationUtil.getNearbyEntities(Damageable.class, getPlayer().getLocation(), 5, 5, predicate)) {
						if (d instanceof Player) SoundLib.ENTITY_GENERIC_EXPLODE.playSound((Player) d);
						d.damage(damage, getPlayer());
					}
					SoundLib.ENTITY_GENERIC_EXPLODE.playSound(getPlayer());
					follow.stop(false);
					fallBlockTimer.start();
				}
			}
		}
	}

	@SubscribeEvent
	private void onAttack(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		if ((damager.equals(getPlayer()) || (damager instanceof Projectile && getPlayer().equals(((Projectile) damager).getShooter()))) && e.getEntity() instanceof Player && !getPlayer().equals(e.getEntity())) {
			Player victim = (Player) e.getEntity();
			if (getGame().isParticipating(victim)) {
				if (victim.equals(lastVictim)) {
					stack++;
				} else {
					lastVictim = victim;
					stack = 1;
				}

				double ceil = Math.ceil(stack / 3.0);
				int soundNumber = (int) (ceil - ((Math.ceil(ceil / SOUND_RUNNABLES.size()) - 1) * SOUND_RUNNABLES.size())) - 1;
				SOUND_RUNNABLES.get(soundNumber).run();
				cooldownTimer.setCount(Math.max(0, cooldownTimer.getCount() - stack));
				PotionEffects.BLINDNESS.addPotionEffect(victim, 20, 0, true);
				e.setDamage(e.getDamage() + (stack * .1));
			}
		}
	}

}
