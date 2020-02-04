package daybreak.abilitywar.game.manager.object;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.ability.list.BlackCandle;
import daybreak.abilitywar.game.games.mode.AbstractGame;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.base.concurrent.SimpleTimer.TaskType;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;

import java.util.HashSet;
import java.util.Set;

public class EffectManager implements EventExecutor {

	private final AbstractGame game;

	public EffectManager(AbstractGame game) {
		this.game = game;
		Bukkit.getPluginManager().registerEvent(PlayerMoveEvent.class, game, EventPriority.HIGHEST, this,
				AbilityWar.getPlugin());

		registerCondition(new EffectCondition() {
			@Override
			protected boolean checkCondition(Participant p, EffectType type) {
				return !type.equals(EffectType.STUN) || !p.hasAbility()
						|| !p.getAbility().getClass().equals(BlackCandle.class);
			}
		});
	}

	private final Set<Participant> STUN = new HashSet<>();

	public void Stun(Player player, int tick) {
		if (game.isParticipating(player)) {
			Participant part = game.getParticipant(player);

			for (EffectCondition condition : conditions) {
				if (!condition.checkCondition(part, EffectType.STUN))
					return;
			}

			game.new GameTimer(TaskType.NORMAL, tick) {
				@Override
				protected void onStart() {
					STUN.add(part);
				}

				@Override
				protected void onEnd() {
					STUN.remove(part);
				}

				@Override
				protected void run(int count) {
				}
			}.setPeriod(TimeUnit.TICKS, 1).start();
		}
	}

	private final Set<EffectCondition> conditions = new HashSet<>();

	public void registerCondition(EffectCondition condition) {
		conditions.add(condition);
	}

	public abstract class EffectCondition {

		protected abstract boolean checkCondition(Participant p, EffectType type);

	}

	private enum EffectType {STUN}

	@Override
	public void execute(Listener listener, Event event) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			Player p = e.getPlayer();
			if (game.isParticipating(p)) {
				Participant part = game.getParticipant(p);
				if (STUN.contains(part)) {
					e.setCancelled(true);
				}
			}
		}
	}

	public interface Handler {
		EffectManager getEffectManager();
	}

}
