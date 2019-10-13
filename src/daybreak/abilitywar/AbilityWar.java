package daybreak.abilitywar;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import daybreak.abilitywar.addon.AddonLoader;
import daybreak.abilitywar.config.AbilitySettings;
import daybreak.abilitywar.config.AbilityWarSettings;
import daybreak.abilitywar.game.MainCommand;
import daybreak.abilitywar.game.manager.AbilityList;
import daybreak.abilitywar.game.script.Script;
import daybreak.abilitywar.game.script.Script.RequiredData;
import daybreak.abilitywar.game.script.types.ChangeAbilityScript;
import daybreak.abilitywar.game.script.types.LocationNoticeScript;
import daybreak.abilitywar.game.script.types.TeleportScript;
import daybreak.abilitywar.game.script.types.ChangeAbilityScript.ChangeTarget;
import daybreak.abilitywar.utils.Messager;
import daybreak.abilitywar.utils.autoupdate.AutoUpdate;
import daybreak.abilitywar.utils.autoupdate.AutoUpdate.Branch;
import daybreak.abilitywar.utils.thread.AbilityWarThread;
import daybreak.abilitywar.utils.versioncompat.ServerVersion;

/**
 * Ability War 능력자 전쟁 플러그인
 * @author DayBreak 새벽
 */
public class AbilityWar extends JavaPlugin {
	
	private static final Logger logger = Logger.getLogger(AbilityWar.class.getName());
	private static final Messager messager = new Messager();
	private static AbilityWar plugin;
	
	public static AbilityWar getPlugin() {
		if (plugin != null) {
			return plugin;
		}
		throw new IllegalStateException("플러그인이 아직 초기화되지 않았습니다.");
	}
	
	private final AutoUpdate au = new AutoUpdate("DayBreak365", "AbilityWar", this, Branch.Master);

	public AutoUpdate getAutoUpdate() {
		return au;
	}

	public AbilityWar() {
		plugin = this;
	}
	
	@Override
	public void onEnable() {
		ServerVersion.VersionCompat(this);

		au.Check();
		
		messager.sendConsoleMessage("Server Version: " + Bukkit.getServer().getBukkitVersion());

		
		Bukkit.getPluginCommand("AbilityWar").setExecutor(new MainCommand());

		Script.registerScript(TeleportScript.class, new RequiredData<Location>("텔레포트 위치", Location.class));
		Script.registerScript(ChangeAbilityScript.class, new RequiredData<ChangeTarget>("능력 변경 대상", ChangeTarget.class));
		Script.registerScript(LocationNoticeScript.class);
		
		AbilityList.nameValues();

		AddonLoader.loadAddons();
		AddonLoader.enableAll();
		
		/*
		 * 서버 부팅이 끝나면 실행
		 */
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				try {
					AbilityWarSettings.load();
					AbilitySettings.load();
				} catch (IOException | InvalidConfigurationException e) {
					logger.log(Level.SEVERE, "콘피그를 불러오는 도중 오류가 발생하였습니다.");
					Bukkit.getPluginManager().disablePlugin(plugin);
				}
				Script.LoadAll();
			}
		});
		messager.sendConsoleMessage("플러그인이 활성화되었습니다.");
	}
	
	@Override
	public void onDisable() {
		AbilityWarThread.StopGame();
		try {
			AbilityWarSettings.update();
		} catch (IOException | InvalidConfigurationException e1) {
			logger.log(Level.SEVERE, "콘피그를 업데이트하는 도중 오류가 발생하였습니다.");
		}
		AbilitySettings.Update();
		AddonLoader.disableAll();
		messager.sendConsoleMessage("플러그인이 비활성화되었습니다.");
	}
	
}