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
import daybreak.abilitywar.utils.base.math.LocationUtil;
import daybreak.abilitywar.utils.library.ParticleLib;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

@AbilityManifest(name = "보이드", rank = Rank.A, species = Species.OTHERS, explain = {
		"공허의 존재 보이드. 철괴를 우클릭하면 보이드가 공허를 통하여",
		"가장 가까이 있는 플레이어에게 순간이동합니다. $[COOLDOWN_CONFIG]",
		"순간이동을 하고 난 후 $[DurationConfig]초간 무적 상태에 돌입합니다."
})
public class Void extends AbilityBase implements ActiveHandler {

	public static final SettingObject<Integer> COOLDOWN_CONFIG = abilitySettings.new SettingObject<Integer>(Void.class, "Cooldown", 100,
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

	public static final SettingObject<Integer> DurationConfig = abilitySettings.new SettingObject<Integer>(Void.class, "Duration", 4,
			"# 순간이동 후 무적 지속시간") {

		@Override
		public boolean condition(Integer value) {
			return value >= 1;
		}

	};

	public Void(Participant participant) {
		super(participant);
	}

	private final Cooldown cooldownTimer = new Cooldown(COOLDOWN_CONFIG.getValue());
	private final AbilityTimer skill = new AbilityTimer(DurationConfig.getValue()) {
		@Override
		public void run(int count) {
		}
	}.register();

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

	@Override
	public boolean ActiveSkill(Material material, ClickType clickType) {
		if (material == Material.IRON_INGOT) {
			if (clickType.equals(ClickType.RIGHT_CLICK)) {
				if (!cooldownTimer.isCooldown()) {
					Player target = LocationUtil.getNearestEntity(Player.class, getPlayer().getLocation(), predicate);

					if (target != null) {
						getPlayer().sendMessage("§e" + target.getName() + "§f에게 텔레포트합니다.");
						getPlayer().teleport(target);
						ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 1, 1, 1, 20);
						skill.start();
						cooldownTimer.start();
					} else {
						getPlayer().sendMessage("§a가장 가까운 플레이어§f가 존재하지 않습니다.");
					}
				}
			}
		}

		return false;
	}

	@SubscribeEvent(onlyRelevant = true)
	private void onEntityDamage(EntityDamageEvent e) {
		if (skill.isRunning()) {
			e.setCancelled(true);
			ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 1, 1, 1, 20);
		}
	}

	@SubscribeEvent(onlyRelevant = true)
	private void onEntityDamage(EntityDamageByEntityEvent e) {
		if (skill.isRunning()) {
			e.setCancelled(true);
			ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 1, 1, 1, 20);
		}
	}

	@SubscribeEvent(onlyRelevant = true)
	private void onEntityDamage(EntityDamageByBlockEvent e) {
		if (skill.isRunning()) {
			e.setCancelled(true);
			ParticleLib.DRAGON_BREATH.spawnParticle(getPlayer().getLocation(), 1, 1, 1, 20);
		}
	}

}
