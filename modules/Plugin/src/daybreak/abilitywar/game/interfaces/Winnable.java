package daybreak.abilitywar.game.interfaces;

import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.utils.base.Messager;
import daybreak.abilitywar.utils.base.concurrent.SimpleTimer;
import daybreak.abilitywar.utils.base.concurrent.SimpleTimer.TaskType;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.base.minecraft.FireworkUtil;
import daybreak.abilitywar.utils.library.SoundLib;
import java.util.StringJoiner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public interface Winnable extends IGame {

	default void Win(Participant... winners) {
		if (!isRunning()) return;
		Messager.clearChat();
		StringJoiner joiner = new StringJoiner(ChatColor.WHITE + ", " + ChatColor.LIGHT_PURPLE, ChatColor.LIGHT_PURPLE.toString(), ChatColor.WHITE + ".");
		for (Participant participant : winners) {
			final Player player = participant.getPlayer();
			joiner.add(player.getName());
			SoundLib.UI_TOAST_CHALLENGE_COMPLETE.playSound(player);
			new SimpleTimer(TaskType.REVERSE, 8) {
				@Override
				protected void run(int seconds) {
					FireworkUtil.spawnWinnerFirework(player.getEyeLocation());
				}
			}.setPeriod(TimeUnit.TICKS, 4).start();
		}
		Bukkit.broadcastMessage("§5§l우승자§f: " + joiner.toString());
		stop();
	}

	default void Win(String... winners) {
		if (!isRunning()) return;
		Messager.clearChat();
		StringJoiner joiner = new StringJoiner(ChatColor.WHITE + ", " + ChatColor.LIGHT_PURPLE, ChatColor.LIGHT_PURPLE.toString(), ChatColor.WHITE + ".");
		for (String winner : winners) {
			joiner.add(winner);
		}
		Bukkit.broadcastMessage("§5§l우승자§f: " + joiner.toString());
		stop();
	}

}
