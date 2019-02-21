package Marlang.AbilityWar.Ability;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Marlang.AbilityWar.Ability.Timer.CooldownTimer;
import Marlang.AbilityWar.Ability.Timer.DurationTimer;
import Marlang.AbilityWar.Utils.Messager;
import Marlang.AbilityWar.Utils.Validate;
import Marlang.AbilityWar.Utils.Thread.TimerBase;

abstract public class AbilityBase {
	
	private Player player;
	private String AbilityName;
	private Rank Rank;
	private String[] Explain;
	
	private boolean Restricted = true;
	
	public AbilityBase(Player player, String AbilityName, Rank Rank, String... Explain) {
		this.player = player;
		this.AbilityName = AbilityName;
		this.Rank = Rank;
		this.Explain = Explain;
	}
	
	abstract public boolean ActiveSkill(MaterialType mt, ClickType ct);
	
	abstract public void PassiveSkill(Event event);
	
	abstract public void TargetSkill(MaterialType mt, Entity entity);
	
	abstract protected void onRestrictClear();
	
	/**
	 * 플레이어 능력 삭제시 사용
	 */
	public void DeleteAbility() {
		for(TimerBase timer : getTimers()) {
			timer.StopTimer(true);
		}
		
		this.player = null;
	}
	
	private List<TimerBase> getTimers() {
		ArrayList<TimerBase> Timers = new ArrayList<TimerBase>();
		
		for(Field field : this.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				if(field.getType().isAssignableFrom(TimerBase.class)) {
					Timers.add((TimerBase) field.get(this));
				} else if(field.getType().isAssignableFrom(DurationTimer.class)) {
					Timers.add((DurationTimer) field.get(this));
				} else if(field.getType().isAssignableFrom(CooldownTimer.class)) {
					Timers.add((CooldownTimer) field.get(this));
				}
				field.setAccessible(false);
			} catch (IllegalArgumentException | IllegalAccessException | NullPointerException exception) {
				Messager.sendErrorMessage("Reflection Error");
			}
		}
		
		return Timers;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getAbilityName() {
		return AbilityName;
	}
	
	public Rank getRank() {
		return Rank;
	}
	
	public String[] getExplain() {
		return Explain;
	}
	
	protected void setExplain(String... Explain) {
		this.Explain = Explain;
	}
	
	public boolean isRestricted() {
		return Restricted;
	}
	
	public void setRestricted(boolean restricted) {
		Restricted = restricted;
		
		if(!restricted) {
			onRestrictClear();
		}
	}
	
	public void updatePlayer(Player player) throws IllegalArgumentException {
		Validate.NotNull(player);
		
		this.player = player;
	}
	
	public enum ClickType {
		/**
		 * 우클릭
		 */
		RightClick,
		/**
		 * 좌클릭
		 */
		LeftClick;
	}
	
	public enum MaterialType {
		
		/**
		 * 철괴
		 */
		Iron_Ingot(Material.IRON_INGOT),
		/**
		 * 금괴
		 */
		Gold_Ingot(Material.GOLD_INGOT);
		
		private Material material;
		
		private MaterialType(Material material) {
			this.material = material;
		}
		
		public Material getMaterial() {
			return material;
		}
		
	}
	
	public enum Rank {
		
		SPECIAL(ChatColor.translateAlternateColorCodes('&', "&5Special 등급")),
		GOD(ChatColor.translateAlternateColorCodes('&', "&c신 등급")),
		S(ChatColor.translateAlternateColorCodes('&', "&dS 등급")),
		A(ChatColor.translateAlternateColorCodes('&', "&aA 등급")),
		B(ChatColor.translateAlternateColorCodes('&', "&bB 등급")),
		C(ChatColor.translateAlternateColorCodes('&', "&eC 등급")),
		D(ChatColor.translateAlternateColorCodes('&', "&7D 등급"));
		
		private String RankName;
		
		private Rank(String RankName) {
			this.RankName = RankName;
		}
		
		public String getRankName() {
			return RankName;
		}
		
	}
	
}
