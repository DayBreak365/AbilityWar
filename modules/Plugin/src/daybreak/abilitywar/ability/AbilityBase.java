package daybreak.abilitywar.ability;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.ability.AbilityFactory.AbilityRegistration;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.event.AbilityDestroyEvent;
import daybreak.abilitywar.ability.event.AbilityEvent;
import daybreak.abilitywar.ability.event.AbilityPreRestrictionEvent;
import daybreak.abilitywar.ability.event.AbilityRestrictionEvent;
import daybreak.abilitywar.config.ability.AbilitySettings;
import daybreak.abilitywar.config.enums.CooldownDecrease;
import daybreak.abilitywar.game.AbstractGame;
import daybreak.abilitywar.game.AbstractGame.GameTimer;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.game.AbstractGame.Participant.ActionbarNotification.ActionbarChannel;
import daybreak.abilitywar.game.AbstractGame.RestrictionBehavior;
import daybreak.abilitywar.game.event.participant.ParticipantEvent;
import daybreak.abilitywar.game.list.changeability.ChangeAbilityWar;
import daybreak.abilitywar.game.list.standard.DefaultGame;
import daybreak.abilitywar.game.manager.AbilityList;
import daybreak.abilitywar.game.manager.object.EventManager;
import daybreak.abilitywar.game.manager.object.EventManager.EventObserver;
import daybreak.abilitywar.game.manager.object.WRECK;
import daybreak.abilitywar.utils.base.RegexReplacer;
import daybreak.abilitywar.utils.base.TimeUtil;
import daybreak.abilitywar.utils.base.collect.Pair;
import daybreak.abilitywar.utils.base.concurrent.SimpleTimer;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.base.io.FileUtil;
import daybreak.abilitywar.utils.base.logging.Logger;
import daybreak.abilitywar.utils.base.math.FastMath;
import daybreak.abilitywar.utils.base.reflect.ReflectionUtil;
import daybreak.abilitywar.utils.library.SoundLib;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.regex.MatchResult;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

/**
 * {@link AbilityWar} 플러그인에서 사용하는 <strong>모든 능력</strong>의 기반이 되는 클래스입니다.
 * <p>
 * 만들어진 <strong>모든 능력은 반드시 {@link AbilityFactory}에 등록되어야 합니다.</strong>
 * <p>
 * <ul>
 * {@link AbilityFactory#registerAbility}
 * </ul>
 * {@link DefaultGame}, {@link ChangeAbilityWar} 등에서 사용할 능력은 추가적으로
 * {@link AbilityList}에 등록해야 합니다.
 * <p>
 * <ul>
 * {@link AbilityList#registerAbility}
 * </ul>
 *
 * @author Daybreak 새벽
 */
public abstract class AbilityBase {

	private static final Logger logger = Logger.getLogger(AbilityBase.class);

	public static final AbilitySettings abilitySettings = new AbilitySettings(FileUtil.newFile("abilitysettings.yml"));
	private static final RegexReplacer SQUARE_BRACKET = new RegexReplacer("\\$\\[([^\\[\\]]+)\\]");
	private static final RegexReplacer ROUND_BRACKET = new RegexReplacer("\\$\\(([^\\(\\)]+)\\)");

	/**
	 * {@link AbilityBase}의 기본 생성자입니다.
	 *
	 * @param participant 능력을 소유하는 참가자
	 * @throws IllegalStateException 능력이 {@link AbilityFactory}에 등록되지 않았을 경우 예외가 발생합니다.
	 */
	protected AbilityBase(Participant participant) throws IllegalStateException {
		this.participant = participant;
		this.game = participant.getGame();
		if (!AbilityFactory.isRegistered(getClass())) {
			throw new IllegalStateException("AbilityFactory에 등록되지 않은 능력입니다.");
		}
		this.registration = AbilityFactory.getRegistration(getClass());
		this.manifest = registration.getManifest();
		EventManager eventManager = game.getEventManager();
		eventhandlers = new HashMap<>();
		for (Entry<Class<? extends Event>, Pair<Method, SubscribeEvent>> entry : registration.getEventhandlers().entrySet()) {
			final Pair<Method, SubscribeEvent> pair = entry.getValue();
			final EventObserver observer = new EventObserver(entry.getKey(), pair.getRight(), pair.getLeft()) {
				@Override
				protected void onEvent(Event event) {
					if (restricted) return;
					if (subscriber.onlyRelevant()
							&& ((event instanceof AbilityEvent && !AbilityBase.this.equals(((AbilityEvent) event).getAbility()))
							|| (event instanceof ParticipantEvent && !getParticipant().equals(((ParticipantEvent) event).getParticipant()))
							|| (event instanceof PlayerEvent && !getPlayer().equals(((PlayerEvent) event).getPlayer()))
							|| (event instanceof EntityEvent && !getPlayer().equals(((EntityEvent) event).getEntity())))) {
						return;
					}
					if (subscriber.ignoreCancelled() && event instanceof Cancellable && ((Cancellable) event).isCancelled())
						return;
					try {
						ReflectionUtil.setAccessible(method).invoke(AbilityBase.this, event);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						logger.log(Level.SEVERE, method.getDeclaringClass().getName() + ":" + method.getName() + "를 호출하는 도중 오류가 발생하였습니다.", ex);
					}
				}
			};
			eventhandlers.put(entry.getKey(), observer);
		}

		for (EventObserver value : eventhandlers.values()) {
			eventManager.register(value);
		}

		this.restricted = game.isRestricted() || !game.isGameStarted();
	}

	private final Function<MatchResult, String> fieldValueProvider = new Function<MatchResult, String>() {
		@Override
		public String apply(MatchResult matchResult) {
			try {
				final Field field = AbilityBase.this.getClass().getDeclaredField(matchResult.group(1));
				if (Modifier.isStatic(field.getModifiers())) {
					try {
						return String.valueOf(ReflectionUtil.setAccessible(field).get(null));
					} catch (IllegalAccessException ignored) {
					}
				} else {
					try {
						return String.valueOf(ReflectionUtil.setAccessible(field).get(AbilityBase.this));
					} catch (IllegalAccessException ignored) {
					}
				}
			} catch (NoSuchFieldException ignored) {
			}
			return "?";
		}
	};

	private final Participant participant;
	private final AbilityRegistration registration;
	private final AbilityManifest manifest;
	private String[] explanation = null;
	private final AbstractGame game;
	private final Map<Class<? extends Event>, EventObserver> eventhandlers;
	private final Set<AbilityTimer> timers = new HashSet<>();
	private final Set<AbilityTimer> runningTimers = new HashSet<>();
	private final List<ActionbarChannel> actionbarChannels = new LinkedList<>();

	private boolean restricted, destroyed;

	public static <T extends AbilityBase> T create(Class<T> abilityClass, Participant participant) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		Preconditions.checkNotNull(abilityClass);
		Preconditions.checkNotNull(participant);
		if (AbilityFactory.isRegistered(abilityClass)) {
			AbilityRegistration registration = AbilityFactory.getRegistration(abilityClass);
			AbilityBase abilityBase = registration.getConstructor().newInstance(participant);
			return abilityClass.cast(abilityBase);
		} else {
			throw new IllegalArgumentException(abilityClass.getSimpleName() + " 능력은 AbilityFactory에 등록되지 않은 능력입니다.");
		}
	}

	protected void onUpdate(Update update) {
	}

	public enum Update {
		RESTRICTION_SET,
		RESTRICTION_CLEAR,
		ABILITY_DESTROY
	}

	/**
	 * 더 이상 사용되지 않는 {@link AbilityBase}를 제거할 때 사용됩니다.
	 * <p>
	 * {@link Participant#removeAbility()}를 통해 {@link Participant}의 능력을 제거할 때 호출됩니다.
	 * 절대 임의로 호출하지 마십시오.
	 */
	public final void destroy() {
		this.destroyed = true;
		onUpdate(Update.ABILITY_DESTROY);
		Bukkit.getPluginManager().callEvent(new AbilityDestroyEvent(this));
		for (EventObserver eventObserver : eventhandlers.values()) {
			game.getEventManager().unregister(eventObserver);
		}
		for (AbilityTimer abilityTimer : new HashSet<>(runningTimers)) {
			abilityTimer.stop(true);
		}
		for (ActionbarChannel channel : actionbarChannels) {
			channel.unregister();
		}
	}

	public final AbilityRegistration getRegistration() {
		return registration;
	}

	/**
	 * 능력을 소유하는 플레이어를 반환합니다.
	 */
	public final Player getPlayer() {
		return participant.getPlayer();
	}

	/**
	 * 능력을 소유하는 참가자를 반환합니다.
	 */
	public final Participant getParticipant() {
		return participant;
	}

	/**
	 * 능력의 설명을 반환합니다.
	 */
	public final Iterator<String> getExplanation() {
		if (explanation == null) {
			this.explanation = new String[manifest.explain().length];
			System.arraycopy(manifest.explain(), 0, explanation, 0, explanation.length);
			for (int i = 0; i < explanation.length; i++) {
				explanation[i] = SQUARE_BRACKET.replaceAll(explanation[i], fieldValueProvider);
			}
		}

		return new Iterator<String>() {
			private int cursor = 0;

			@Override
			public boolean hasNext() {
				return cursor < explanation.length;
			}

			@Override
			public String next() {
				return ROUND_BRACKET.replaceAll(explanation[cursor++], fieldValueProvider);
			}
		};
	}

	/**
	 * 능력의 이름을 반환합니다.
	 */
	public final String getName() {
		return manifest.name();
	}

	/**
	 * 능력의 등급을 반환합니다.
	 */
	public final Rank getRank() {
		return manifest.rank();
	}

	/**
	 * 능력의 종족을 반환합니다.
	 */
	public final Species getSpecies() {
		return manifest.species();
	}

	/**
	 * 이 능력이 사용되는 게임을 반환합니다.
	 */
	protected final AbstractGame getGame() {
		return game;
	}

	/**
	 * 능력의 제한 여부를 반환합니다.
	 */
	public final boolean isRestricted() {
		return restricted;
	}

	public final Set<GameTimer> getTimers() {
		return Collections.unmodifiableSet(timers);
	}

	public final Set<GameTimer> getRunningTimers() {
		return Collections.unmodifiableSet(runningTimers);
	}

	public boolean usesMaterial(Material material) {
		return registration.getMaterials().contains(material);
	}

	private final Queue<GameTimer> pausedTimers = new LinkedList<>();

	/**
	 * 능력의 제한 여부를 설정합니다.
	 */
	public final void setRestricted(final boolean toSet) {
		final AbilityPreRestrictionEvent event = new AbilityPreRestrictionEvent(this, toSet);
		Bukkit.getPluginManager().callEvent(event);
		this.restricted = event.getNewStatus();
		if (event.getNewStatus()) {
			for (GameTimer timer : timers) {
				if (timer.getBehavior() == RestrictionBehavior.STOP_START) {
					timer.stop(true);
				} else {
					timer.pause();
					pausedTimers.add(timer);
				}
			}
			for (ActionbarChannel channel : actionbarChannels) {
				channel.update(null);
			}
			onUpdate(Update.RESTRICTION_SET);
			Bukkit.getPluginManager().callEvent(new AbilityRestrictionEvent(this, true));
		} else {
			while (!pausedTimers.isEmpty()) {
				pausedTimers.poll().resume();
			}
			onUpdate(Update.RESTRICTION_CLEAR);
			Bukkit.getPluginManager().callEvent(new AbilityRestrictionEvent(this, false));
		}
	}

	public final ActionbarChannel newActionbarChannel() {
		ActionbarChannel channel = participant.actionbar().newChannel();
		actionbarChannels.add(channel);
		return channel;
	}

	public enum ClickType {LEFT_CLICK, RIGHT_CLICK}

	/**
	 * 쿨타임 타이머
	 * 능력의 쿨타임을 관리하기 위해 만들어진 타이머입니다.
	 *
	 * @author Daybreak 새벽
	 */
	@Deprecated
	public class CooldownTimer extends Timer {

		private final ActionbarChannel actionbarChannel = newActionbarChannel();
		private final String name;

		public CooldownTimer(int cooldown, String name, CooldownDecrease maxDecrease) {
			super(TaskType.REVERSE, (int) (WRECK.isEnabled(getGame()) ? cooldown * WRECK.calculateDecreasedAmount(maxDecrease.getPercentage()) : cooldown));
			setBehavior(RestrictionBehavior.PAUSE_RESUME);
			this.name = name;
		}

		public CooldownTimer(int cooldown, String name) {
			this(cooldown, name, CooldownDecrease._100);
		}

		public CooldownTimer(int cooldown, CooldownDecrease maxDecrease) {
			this(cooldown, "", maxDecrease);
		}

		public CooldownTimer(int cooldown) {
			this(cooldown, "");
		}

		public boolean isCooldown() {
			if (isRunning()) {
				if (getPlayer() != null) {
					getPlayer().sendMessage(toString(ChatColor.WHITE));
				}
			}
			return isRunning();
		}

		@Override
		public void run(int count) {
			actionbarChannel.update(toString());
			if (count == (getMaximumCount() / 2) || (count <= 5 && count >= 1)) {
				SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(getPlayer());
				getPlayer().sendMessage(toString(ChatColor.WHITE));
			}
		}

		@Override
		public void onEnd() {
			Player player = getPlayer();
			if (player != null) {
				player.sendMessage("§a능력을 다시 사용할 수 있습니다.");
				actionbarChannel.update("§a능력을 다시 사용할 수 있습니다.", 2);
			}
		}

		@Override
		public void onSilentEnd() {
			actionbarChannel.update(null);
		}

		@Override
		public void onCountSet() {
			actionbarChannel.update(toString());
		}

		@Override
		public String toString() {
			return toString(ChatColor.GOLD);
		}

		public String toString(ChatColor timeColor) {
			if (name != null && !name.isEmpty()) {
				return ChatColor.RED.toString() + name + " 쿨타임 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(getCount());
			} else {
				return ChatColor.RED.toString() + "쿨타임 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(getCount());
			}
		}

		@Override
		public CooldownTimer setPeriod(TimeUnit timeUnit, int period) {
			return this;
		}

		@Override
		public CooldownTimer setInitialDelay(TimeUnit timeUnit, int initialDelay) {
			return this;
		}

	}

	/**
	 * Duration Timer (지속시간 타이머)
	 * 능력의 지속시간을 관리하고 능력을 발동시키기 위해 만들어진 타이머입니다.
	 *
	 * @author Daybreak 새벽
	 */
	@Deprecated
	public abstract class DurationTimer extends Timer {

		private final ActionbarChannel actionbarChannel = newActionbarChannel();
		private final int duration;
		private final String name;
		private final CooldownTimer cooldownTimer;
		private int period = 20;

		public DurationTimer(int duration, CooldownTimer cooldownTimer, String name) {
			super(TaskType.REVERSE, duration);
			this.duration = duration;
			this.name = name;
			this.cooldownTimer = cooldownTimer;
		}

		public DurationTimer(int duration, CooldownTimer cooldownTimer) {
			this(duration, cooldownTimer, "");
		}

		public DurationTimer(int duration) {
			this(duration, null);
		}

		protected void onDurationStart() {}
		protected abstract void onDurationProcess(int count);
		protected void onDurationEnd() {}
		protected void onDurationSilentEnd() {}

		public final boolean isDuration() {
			if (isRunning()) {
				if (getPlayer() != null) {
					getPlayer().sendMessage(toString(ChatColor.WHITE));
				}
			}
			return isRunning();
		}

		@Override
		public DurationTimer setPeriod(TimeUnit timeUnit, int period) {
			Preconditions.checkNotNull(timeUnit);
			this.period = timeUnit.toTicks(period);
			super.setPeriod(TimeUnit.TICKS, FastMath.gcd(this.period, 20));
			return this;
		}

		@Override
		public DurationTimer setInitialDelay(TimeUnit timeUnit, int initialDelay) {
			super.setInitialDelay(timeUnit, initialDelay);
			return this;
		}

		@Override
		protected final void onStart() {
			onDurationStart();
		}

		private int tick = 0;

		@Override
		protected final void run(int count) {
			tick += getPeriod();
			if (tick % 20 == 0) {
				actionbarChannel.update(toString());
				final int fixedCount = getFixedCount();
				if ((fixedCount == (duration / 2) || (fixedCount <= 5 && fixedCount >= 1))) {
					SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(getPlayer());
					getPlayer().sendMessage(toString(ChatColor.WHITE));
				}
			}
			if (tick % period == 0) {
				onDurationProcess(count);
				if (tick >= 20) {
					tick = 0;
				}
			}
		}

		@Override
		protected final void onEnd() {
			Player player = getPlayer();
			if (player != null) {
				onDurationEnd();

				player.sendMessage("§6지속 시간§f이 종료되었습니다.");
				actionbarChannel.update("§6지속 시간§f이 종료되었습니다.", 2);

				if (cooldownTimer != null) {
					cooldownTimer.start();
				}
			}
		}

		@Override
		protected final void onSilentEnd() {
			onDurationSilentEnd();
			actionbarChannel.update(null);
		}

		@Override
		public final String toString() {
			return toString(ChatColor.YELLOW);
		}

		public final String toString(ChatColor timeColor) {
			if (name != null && !name.isEmpty()) {
				return ChatColor.GOLD.toString() + name + " 지속 시간 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(getFixedCount());
			} else {
				return ChatColor.GOLD.toString() + "지속 시간 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(getFixedCount());
			}
		}

	}

	@Deprecated
	public abstract class Timer extends AbilityTimer {

		public Timer(TaskType taskType, int maximumCount) {
			super(taskType, maximumCount);
			register();
		}

		public Timer(int maximumCount) {
			this(TaskType.REVERSE, maximumCount);
		}

		public Timer() {
			this(TaskType.INFINITE, -1);
		}

		@Override
		public Timer setPeriod(TimeUnit timeUnit, int period) {
			super.setPeriod(timeUnit, period);
			return this;
		}

		@Override
		public Timer setInitialDelay(TimeUnit timeUnit, int initialDelay) {
			super.setInitialDelay(timeUnit, initialDelay);
			return this;
		}

		public Timer setBehavior(RestrictionBehavior behavior) {
			super.setBehavior(behavior);
			return this;
		}

	}

	public abstract class AbilityTimer extends GameTimer {

		public AbilityTimer(TaskType taskType, int maximumCount) {
			game.super(taskType, maximumCount);
			attachObserver(new SimpleTimer.Observer() {
				@Override
				public void onStart() {
					runningTimers.add(AbilityTimer.this);
				}
				@Override
				public void onEnd() {
					runningTimers.remove(AbilityTimer.this);
				}
				@Override
				public void onSilentEnd() {
					runningTimers.remove(AbilityTimer.this);
				}
				@Override
				public void onResume() {
					runningTimers.add(AbilityTimer.this);
				}
				@Override
				public void onPause() {
					runningTimers.remove(AbilityTimer.this);
				}
			});
		}

		public AbilityTimer(int maximumCount) {
			this(TaskType.REVERSE, maximumCount);
		}

		public AbilityTimer() {
			this(TaskType.INFINITE, -1);
		}

		@Override
		public AbilityTimer setPeriod(TimeUnit timeUnit, int period) {
			super.setPeriod(timeUnit, period);
			return this;
		}

		@Override
		public AbilityTimer setInitialDelay(TimeUnit timeUnit, int initialDelay) {
			super.setInitialDelay(timeUnit, initialDelay);
			return this;
		}

		public AbilityTimer setBehavior(RestrictionBehavior behavior) {
			super.setBehavior(behavior);
			return this;
		}

		public AbilityTimer register() {
			timers.add(this);
			return this;
		}

		public AbilityTimer unregister() {
			timers.remove(this);
			return this;
		}

		@Override
		public boolean start() {
			if (!destroyed) {
				return super.start();
			} else {
				return false;
			}
		}

	}

	/**
	 * 쿨타임 관리 클래스
	 * 타이머를 내부적으로 관리합니다.
	 *
	 * @author Daybreak 새벽
	 */
	public class Cooldown {

		private int cooldown;
		private final String name;
		private CooldownTimer timer;

		public Cooldown(int cooldown, String name, CooldownDecrease maxDecrease) {
			this.cooldown = (int) (WRECK.isEnabled(getGame()) ? cooldown * WRECK.calculateDecreasedAmount(maxDecrease.getPercentage()) : cooldown);
			this.timer = new CooldownTimer(this.cooldown);
			this.name = name;
		}

		public Cooldown(int cooldown, String name) {
			this(cooldown, name, CooldownDecrease._100);
		}

		public Cooldown(int cooldown, CooldownDecrease maxDecrease) {
			this(cooldown, "", maxDecrease);
		}

		public Cooldown(int cooldown) {
			this(cooldown, "");
		}

		public boolean start() {
			return timer.start();
		}

		public boolean stop(boolean silent) {
			return timer.stop(silent);
		}

		public boolean isRunning() {
			return timer.isRunning();
		}

		public int getCooldown() {
			return cooldown;
		}

		public boolean isCooldown() {
			if (timer.isRunning()) {
				if (getPlayer() != null) {
					getPlayer().sendMessage(toString(ChatColor.WHITE));
				}
			}
			return timer.isRunning();
		}

		public int getCount() {
			return timer.getCount();
		}

		public void setCount(int count) {
			timer.setCount(count);
		}

		public void setCooldown(int cooldown, CooldownDecrease maxDecrease) {
			timer.actionbarChannel.unregister();
			timer.stop(true);
			timer.unregister();
			this.cooldown = (int) (WRECK.isEnabled(getGame()) ? cooldown * WRECK.calculateDecreasedAmount(maxDecrease.getPercentage()) : cooldown);
			this.timer = new CooldownTimer(this.cooldown);
		}

		public void setCooldown(int cooldown) {
			this.setCooldown(cooldown, CooldownDecrease._100);
		}

		@Override
		public String toString() {
			return toString(ChatColor.GOLD);
		}

		public String toString(ChatColor timeColor) {
			if (name != null && !name.isEmpty()) {
				return ChatColor.RED.toString() + name + " 쿨타임 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(timer.getCount());
			} else {
				return ChatColor.RED.toString() + "쿨타임 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(timer.getCount());
			}
		}

		public class CooldownTimer extends AbilityTimer {

			private final ActionbarChannel actionbarChannel = newActionbarChannel();

			public CooldownTimer(int cooldown) {
				super(TaskType.REVERSE, cooldown);
				setBehavior(RestrictionBehavior.PAUSE_RESUME);
				register();
			}

			@Override
			public void run(int count) {
				actionbarChannel.update(toString());
				if (count == (getMaximumCount() / 2) || (count <= 5 && count >= 1)) {
					SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(getPlayer());
					getPlayer().sendMessage(toString(ChatColor.WHITE));
				}
			}

			@Override
			public void onEnd() {
				getPlayer().sendMessage("§a능력을 다시 사용할 수 있습니다.");
				actionbarChannel.update("§a능력을 다시 사용할 수 있습니다.", 2);
			}

			@Override
			public void onSilentEnd() {
				actionbarChannel.update(null);
			}

			@Override
			public void onCountSet() {
				actionbarChannel.update(toString());
			}

			@Override
			public String toString() {
				return toString(ChatColor.GOLD);
			}

			private String toString(ChatColor timeColor) {
				if (name != null && !name.isEmpty()) {
					return ChatColor.RED.toString() + name + " 쿨타임 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(getCount());
				} else {
					return ChatColor.RED.toString() + "쿨타임 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(getCount());
				}
			}

			@Override
			public CooldownTimer setPeriod(TimeUnit timeUnit, int period) {
				return this;
			}

			@Override
			public CooldownTimer setInitialDelay(TimeUnit timeUnit, int initialDelay) {
				return this;
			}

		}

	}

	/**
	 * 지속 타이머
	 *
	 * @author Daybreak 새벽
	 */
	public abstract class Duration extends AbilityTimer {

		private final ActionbarChannel actionbarChannel = newActionbarChannel();
		private final int duration;
		private final String name;
		private final Cooldown cooldown;
		private int period = 20;

		public Duration(int duration, Cooldown cooldown, String name) {
			super(TaskType.REVERSE, duration);
			this.duration = duration;
			this.name = Strings.nullToEmpty(name);
			this.cooldown = cooldown;
			register();
		}

		public Duration(int duration, Cooldown cooldown) {
			this(duration, cooldown, "");
		}

		public Duration(int duration) {
			this(duration, null);
		}

		protected void onDurationStart() {}
		protected abstract void onDurationProcess(int count);
		protected void onDurationEnd() {}
		protected void onDurationSilentEnd() {}

		public final boolean isDuration() {
			if (isRunning()) {
				if (getPlayer() != null) {
					getPlayer().sendMessage(toString(ChatColor.WHITE));
				}
			}
			return isRunning();
		}

		@Override
		public Duration setPeriod(TimeUnit timeUnit, int period) {
			Preconditions.checkNotNull(timeUnit);
			this.period = timeUnit.toTicks(period);
			super.setPeriod(TimeUnit.TICKS, FastMath.gcd(this.period, 20));
			return this;
		}

		@Override
		public Duration setInitialDelay(TimeUnit timeUnit, int initialDelay) {
			super.setInitialDelay(timeUnit, initialDelay);
			return this;
		}

		@Override
		public Duration setBehavior(RestrictionBehavior behavior) {
			super.setBehavior(behavior);
			return this;
		}

		@Override
		public Duration unregister() {
			super.unregister();
			return this;
		}

		@Override
		protected final void onStart() {
			onDurationStart();
		}

		private int tick = 0;

		@Override
		protected final void run(int count) {
			tick += getPeriod();
			if (tick % 20 == 0) {
				actionbarChannel.update(toString());
				final int fixedCount = getFixedCount();
				if ((fixedCount == (duration / 2) || (fixedCount <= 5 && fixedCount >= 1))) {
					SoundLib.BLOCK_NOTE_BLOCK_HAT.playSound(getPlayer());
					getPlayer().sendMessage(toString(ChatColor.WHITE));
				}
			}
			if (tick % period == 0) {
				onDurationProcess(count);
				if (tick >= 20) {
					tick = 0;
				}
			}
		}

		@Override
		protected final void onEnd() {
			Player player = getPlayer();
			if (player != null) {
				onDurationEnd();

				player.sendMessage("§6지속 시간§f이 종료되었습니다.");
				actionbarChannel.update("§6지속 시간§f이 종료되었습니다.", 2);

				if (cooldown != null) {
					cooldown.start();
				}
			}
		}

		@Override
		protected final void onSilentEnd() {
			onDurationSilentEnd();
			actionbarChannel.update(null);
		}

		@Override
		public final String toString() {
			return toString(ChatColor.YELLOW);
		}

		public final String toString(ChatColor timeColor) {
			if (name != null && !name.isEmpty()) {
				return ChatColor.GOLD.toString() + name + " 지속 시간 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(getFixedCount());
			} else {
				return ChatColor.GOLD.toString() + "지속 시간 " + ChatColor.WHITE.toString() + ": " + timeColor.toString() + TimeUtil.parseTimeAsString(getFixedCount());
			}
		}

	}

}
