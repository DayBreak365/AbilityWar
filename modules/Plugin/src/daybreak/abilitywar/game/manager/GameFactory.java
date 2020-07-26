package daybreak.abilitywar.game.manager;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import daybreak.abilitywar.config.Configuration.Settings.DeveloperSettings;
import daybreak.abilitywar.game.AbstractGame;
import daybreak.abilitywar.game.Category;
import daybreak.abilitywar.game.Category.GameCategory;
import daybreak.abilitywar.game.GameAliases;
import daybreak.abilitywar.game.GameManifest;
import daybreak.abilitywar.game.TeamSupport;
import daybreak.abilitywar.game.list.blind.BlindAbilityWar;
import daybreak.abilitywar.game.list.changeability.ChangeAbilityWar;
import daybreak.abilitywar.game.list.debug.DebugMode;
import daybreak.abilitywar.game.list.mix.MixGame;
import daybreak.abilitywar.game.list.mix.changemix.ChangeMix;
import daybreak.abilitywar.game.list.mix.debug.MixDebugMode;
import daybreak.abilitywar.game.list.murdermystery.MurderMystery;
import daybreak.abilitywar.game.list.oneability.OneAbility;
import daybreak.abilitywar.game.list.standard.StandardGame;
import daybreak.abilitywar.game.list.standard.WarGame;
import daybreak.abilitywar.game.list.summervacation.SummerVacation;
import daybreak.abilitywar.game.manager.GameFactory.GameRegistration.Flag;
import daybreak.abilitywar.game.team.interfaces.Teamable;
import daybreak.abilitywar.utils.annotations.Beta;
import daybreak.abilitywar.utils.annotations.Support;
import daybreak.abilitywar.utils.base.logging.Logger;
import daybreak.abilitywar.utils.base.minecraft.server.ServerType;
import daybreak.abilitywar.utils.base.minecraft.server.UnsupportedServerException;
import daybreak.abilitywar.utils.base.minecraft.version.ServerVersion;
import daybreak.abilitywar.utils.base.minecraft.version.UnsupportedVersionException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameFactory {

	private GameFactory() {
	}

	private static final Logger logger = Logger.getLogger(GameFactory.class);

	private static final Map<String, GameRegistration> usedNames = new LinkedHashMap<>();
	private static final Map<Class<? extends AbstractGame>, GameRegistration> registeredModes = new HashMap<>();
	private static final Multimap<GameCategory, GameRegistration> byCategory = LinkedHashMultimap.create();

	static {
		registerMode(StandardGame.class);
		registerMode(WarGame.class);
		registerMode(ChangeAbilityWar.class);
		registerMode(SummerVacation.class);
		registerMode(MixGame.class);
		registerMode(OneAbility.class);
		registerMode(ChangeMix.class);
		registerMode(MurderMystery.class);
		registerMode(BlindAbilityWar.class);
		if (DeveloperSettings.isEnabled()) {
			registerMode(DebugMode.class);
			registerMode(MixDebugMode.class);
		}
	}

	public static Collection<GameRegistration> getRegistrations() {
		return registeredModes.values();
	}

	public static GameRegistration getRegistration(Class<? extends AbstractGame> clazz) {
		return registeredModes.get(clazz);
	}

	public static boolean isRegistered(String name) {
		return usedNames.containsKey(name);
	}

	public static boolean isRegistered(Class<? extends AbstractGame> clazz) {
		return registeredModes.containsKey(clazz);
	}

	public static void registerMode(Class<? extends AbstractGame> gameClass) {
		if (!registeredModes.containsKey(gameClass)) {
			try {
				final GameRegistration registration = new GameRegistration(gameClass);
				final String name = registration.getManifest().name();
				if (!usedNames.containsKey(name)) {
					if (!registration.hasFlag(Flag.BETA) || DeveloperSettings.isEnabled()) {
						registeredModes.put(gameClass, registration);
						usedNames.put(name, registration);
						for (String alias : registration.aliases) {
							usedNames.putIfAbsent(alias, registration);
						}
						byCategory.put(registration.category, registration);
					}
				} else {
					logger.debug("§e" + gameClass.getName() + " §f게임모드는 겹치는 이름이 있어 등록되지 않았습니다.");
				}
			} catch (UnsupportedVersionException e) {
				logger.debug("§e" + gameClass.getName() + " §f게임 모드는 이 버전에서 지원되지 않습니다.");
			} catch (UnsupportedServerException e) {
				logger.debug("§e" + gameClass.getName() + " §f게임 모드는 이 서버에서 지원되지 않습니다. (이 서버: " + ServerType.getServerType().name() + ") (지원되는 서버: " + Arrays.toString(e.getSupported()) + ")");
			} catch (Exception e) {
				logger.error(e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : ("§e" + gameClass.getName() + " §f게임 모드 등록 중 오류가 발생하였습니다."));
			}
		}
	}

	public static List<String> nameValues() {
		return new ArrayList<>(usedNames.keySet());
	}

	public static GameRegistration getByName(String name) {
		return usedNames.get(name);
	}

	public static Collection<GameRegistration> getByCategory(final GameCategory category) {
		return byCategory.get(category);
	}

	public static class GameRegistration {

		private static final String[] EMPTY = new String[0];

		private final Class<? extends AbstractGame> clazz;
		private final Constructor<? extends AbstractGame> constructor;
		private final GameManifest manifest;
		private final GameCategory category;
		private final String[] aliases;
		private final TeamGameRegistration teamGame;
		private final int flag;

		private GameRegistration(Class<? extends AbstractGame> clazz) throws NullPointerException, NoSuchMethodException, SecurityException, UnsupportedVersionException, UnsupportedServerException {
			if (clazz.isAnnotationPresent(Support.Version.class)) {
				final Support.Version supportedVersion = clazz.getAnnotation(Support.Version.class);
				if (!(ServerVersion.isAboveOrEqual(supportedVersion.min()) && ServerVersion.isBelowOrEqual(supportedVersion.max()))) {
					throw new UnsupportedVersionException();
				}
			}
			if (clazz.isAnnotationPresent(Support.Server.class)) {
				final ServerType[] supportedServers = clazz.getAnnotation(Support.Server.class).value();
				if (!Arrays.asList(supportedServers).contains(ServerType.getServerType())) {
					throw new UnsupportedServerException(supportedServers);
				}
			}
			this.clazz = clazz;

			Constructor<? extends AbstractGame> constructor;
			try {
				constructor = clazz.getConstructor(String[].class);
			} catch (NoSuchMethodException ex) {
				constructor = clazz.getConstructor();
			}
			this.constructor = constructor;

			if (!clazz.isAnnotationPresent(GameManifest.class))
				throw new IllegalArgumentException("GameManifest가 없는 게임 모드입니다.");
			this.manifest = clazz.getAnnotation(GameManifest.class);
			Preconditions.checkNotNull(manifest.name());
			Preconditions.checkNotNull(manifest.description());
			this.category = clazz.isAnnotationPresent(Category.class) ? clazz.getAnnotation(Category.class).value() : GameCategory.GAME;
			this.aliases = clazz.isAnnotationPresent(GameAliases.class) ? clazz.getAnnotation(GameAliases.class).value() : EMPTY;
			if (clazz.isAnnotationPresent(TeamSupport.class)) {
				final TeamSupport teamSupport = clazz.getAnnotation(TeamSupport.class);
				TeamGameRegistration teamGame = null;
				try {
					teamGame = new TeamGameRegistration(teamSupport.value());
				} catch (UnsupportedVersionException e) {
					logger.debug("§e" + teamSupport.value().getName() + " §f게임 모드는 이 버전에서 지원되지 않습니다.");
				} catch (UnsupportedServerException e) {
					logger.debug("§e" + teamSupport.value().getName() + " §f게임 모드는 이 서버에서 지원되지 않습니다. (이 서버: " + ServerType.getServerType().name() + ") (지원되는 서버: " + Arrays.toString(e.getSupported()) + ")");
				} catch (Exception e) {
					logger.error(e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : ("§e" + teamSupport.value().getName() + " §f게임 모드 등록 중 오류가 발생하였습니다."));
				}
				this.teamGame = teamGame;
			} else this.teamGame = null;

			int flag = 0x0;
			if (clazz.isAnnotationPresent(Beta.class)) flag |= Flag.BETA;
			if (this.constructor.getParameterCount() == 1 && this.constructor.getParameterTypes()[0] == String[].class) flag |= Flag.CONSTRUCTOR_ARGS;
			if (this.teamGame != null) flag |= Flag.TEAM_GAME_SUPPORTED;
			this.flag = flag;
		}

		public Class<? extends AbstractGame> getGameClass() {
			return clazz;
		}

		public Constructor<? extends AbstractGame> getConstructor() {
			return constructor;
		}

		public GameCategory getCategory() {
			return category;
		}

		public String[] getAliases() {
			return aliases;
		}

		public GameManifest getManifest() {
			return manifest;
		}

		public TeamGameRegistration getTeamGame() {
			return teamGame;
		}

		public boolean hasFlag(int flag) {
			return (this.flag & flag) == flag;
		}

		public static class Flag {
			public static final int BETA = 0x1;
			public static final int CONSTRUCTOR_ARGS = 0x2;
			public static final int TEAM_GAME_SUPPORTED = 0x4;
		}

	}

	public static class TeamGameRegistration {

		private final Class<? extends Teamable> clazz;
		private final Constructor<? extends Teamable> constructor;
		private final int flag;

		private TeamGameRegistration(Class<? extends Teamable> clazz) throws NullPointerException, NoSuchMethodException, SecurityException, UnsupportedVersionException, UnsupportedServerException {
			if (clazz.isAnnotationPresent(Support.Version.class)) {
				final Support.Version supportedVersion = clazz.getAnnotation(Support.Version.class);
				if (!(ServerVersion.isAboveOrEqual(supportedVersion.min()) && ServerVersion.isBelowOrEqual(supportedVersion.max()))) {
					throw new UnsupportedVersionException();
				}
			}
			if (clazz.isAnnotationPresent(Support.Server.class)) {
				final ServerType[] supportedServers = clazz.getAnnotation(Support.Server.class).value();
				if (!Arrays.asList(supportedServers).contains(ServerType.getServerType())) {
					throw new UnsupportedServerException(supportedServers);
				}
			}
			this.clazz = clazz;

			Constructor<? extends Teamable> constructor;
			try {
				constructor = clazz.getConstructor(String[].class);
			} catch (NoSuchMethodException ex) {
				constructor = clazz.getConstructor();
			}
			this.constructor = constructor;

			int flag = 0x0;
			if (clazz.isAnnotationPresent(Beta.class)) flag |= Flag.BETA;
			if (this.constructor.getParameterCount() == 1 && this.constructor.getParameterTypes()[0] == String[].class) flag |= Flag.CONSTRUCTOR_ARGS;
			this.flag = flag;
		}

		public Class<? extends Teamable> getGameClass() {
			return clazz;
		}

		public Constructor<? extends Teamable> getConstructor() {
			return constructor;
		}

		public boolean hasFlag(int flag) {
			return (this.flag & flag) == flag;
		}

		public static class Flag {
			public static final int BETA = 0x1;
			public static final int CONSTRUCTOR_ARGS = 0x2;
		}

	}

}
