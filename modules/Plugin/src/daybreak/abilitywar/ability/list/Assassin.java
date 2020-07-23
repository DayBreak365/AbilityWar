package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.decorator.ActiveHandler;
import daybreak.abilitywar.config.ability.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.game.interfaces.TeamGame;
import daybreak.abilitywar.game.manager.effect.Bleed;
import daybreak.abilitywar.game.manager.object.DeathManager;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.base.math.LocationUtil;
import daybreak.abilitywar.utils.library.SoundLib;
import java.util.LinkedList;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@AbilityManifest(name = "암살자", rank = Rank.A, species = Species.HUMAN, explain = {
		"철괴를 우클릭하면 $[DistanceConfig]칸 이내에 있는 생명체 $[TeleportCountConfig]명(마리)에게 이동하며",
		"각각 $[DamageConfig]의 대미지를 줍니다. $[COOLDOWN_CONFIG]",
		"대미지를 받은 생명체는 3초간 추가로 출혈 피해를 입습니다."
})
public class Assassin extends AbilityBase implements ActiveHandler {

	public static final SettingObject<Integer> DistanceConfig = abilitySettings.new SettingObject<Integer>(Assassin.class, "Distance", 8,
			"# 스킬 대미지") {

		@Override
		public boolean condition(Integer value) {
			return value > 0;
		}

	};

	public static final SettingObject<Integer> DamageConfig = abilitySettings.new SettingObject<Integer>(Assassin.class, "Damage", 8,
			"# 스킬 대미지") {

		@Override
		public boolean condition(Integer value) {
			return value >= 0;
		}

	};

	public static final SettingObject<Integer> COOLDOWN_CONFIG = abilitySettings.new SettingObject<Integer>(Assassin.class, "Cooldown", 35,
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

	public static final SettingObject<Integer> TeleportCountConfig = abilitySettings.new SettingObject<Integer>(Assassin.class, "TeleportCount", 4,
			"# 능력 사용 시 텔레포트 횟수") {

		@Override
		public boolean condition(Integer value) {
			return value >= 1;
		}

	};

	public Assassin(Participant participant) {
		super(participant);
	}

	private final Cooldown cooldownTimer = new Cooldown(COOLDOWN_CONFIG.getValue());

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

	private final int damage = DamageConfig.getValue();
	private final int distance = DistanceConfig.getValue();
	private LinkedList<LivingEntity> entities = null;
	private final AbilityTimer skill = new AbilityTimer(TeleportCountConfig.getValue()) {

		@Override
		public void run(int count) {
			if (entities != null) {
				if (!entities.isEmpty()) {
					LivingEntity e = entities.remove();
					getPlayer().teleport(e);
					e.damage(damage, getPlayer());
					SoundLib.ENTITY_PLAYER_ATTACK_SWEEP.playSound(getPlayer());
					SoundLib.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(getPlayer());
					Bleed.apply(getGame(), e, TimeUnit.SECONDS, 3);
				} else {
					stop(false);
				}
			}
		}

	}.setPeriod(TimeUnit.TICKS, 3).register();

	@Override
	public boolean ActiveSkill(Material material, ClickType clickType) {
		if (material == Material.IRON_INGOT && clickType.equals(ClickType.RIGHT_CLICK) && !cooldownTimer.isCooldown()) {
			this.entities = new LinkedList<>(LocationUtil.getNearbyEntities(LivingEntity.class, getPlayer().getLocation(), distance, distance, predicate));
			if (entities.size() > 0) {
				skill.start();
				cooldownTimer.start();
				return true;
			} else {
				getPlayer().sendMessage("§f" + distance + "칸 이내에 §a엔티티§f가 존재하지 않습니다.");
			}
		}

		return false;
	}

}
