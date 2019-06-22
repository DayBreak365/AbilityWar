package DayBreak.AbilityWar.Ability.List;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import DayBreak.AbilityWar.Ability.AbilityBase;
import DayBreak.AbilityWar.Ability.AbilityManifest;
import DayBreak.AbilityWar.Ability.AbilityManifest.Rank;
import DayBreak.AbilityWar.Ability.Timer.CooldownTimer;
import DayBreak.AbilityWar.Ability.Timer.DurationTimer;
import DayBreak.AbilityWar.Config.AbilitySettings.SettingObject;
import DayBreak.AbilityWar.Game.Games.AbstractGame.Participant;
import DayBreak.AbilityWar.Utils.Messager;
import DayBreak.AbilityWar.Utils.Library.SoundLib;
import DayBreak.AbilityWar.Utils.Library.Item.EnchantLib;
import DayBreak.AbilityWar.Utils.Math.LocationUtil;
import DayBreak.AbilityWar.Utils.Thread.TimerBase;
import DayBreak.AbilityWar.Utils.VersionCompat.ServerVersion;

@AbilityManifest(Name = "ȣ��", Rank = Rank.C)
public class Pumpkin extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Pumpkin.class, "Cooldown", 80, 
			"# ��Ÿ��") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public static SettingObject<Integer> DurationConfig = new SettingObject<Integer>(Pumpkin.class, "Duration", 15, 
			"# ���� �ð�") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};
	
	public Pumpkin(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&fö���� ��Ŭ���ϸ� �ֺ� 30���� ���� �־��� �÷��̾�鿡�� " + DurationConfig.getValue() + "�ʰ�"),
				ChatColor.translateAlternateColorCodes('&', "&f�ͼ� ���ְ� �ɸ� ȣ���� ����ϴ�. " + Messager.formatCooldown(CooldownConfig.getValue())),
				ChatColor.translateAlternateColorCodes('&', "&f�� ȣ�� ���� �� �� ��"));
	}
	
	CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	TimerBase Song = new TimerBase(13) {
		
		ArrayList<Player> Players;
		
		Integer Count;
		
		@Override
		public void onStart() {
			this.Players = new ArrayList<Player>(Pumpkin.this.Players.keySet());
			
			Count = 1;
		}
		
		@Override
		public void TimerProcess(Integer Seconds) {
			if(Count.equals(1)) {
				SoundLib.BELL.playInstrument(Players, Note.natural(0, Tone.D));
				SoundLib.BELL.playInstrument(getPlayer(), Note.natural(0, Tone.D));
			} else if(Count.equals(3)) {
				SoundLib.BELL.playInstrument(Players, Note.natural(0, Tone.E));
				SoundLib.BELL.playInstrument(getPlayer(), Note.natural(0, Tone.E));
			} else if(Count.equals(4)) {
				SoundLib.BELL.playInstrument(Players, Note.sharp(1, Tone.F));
				SoundLib.BELL.playInstrument(getPlayer(), Note.sharp(1, Tone.F));
			} else if(Count.equals(7)) {
				SoundLib.BELL.playInstrument(Players, Note.sharp(1, Tone.F));
				SoundLib.BELL.playInstrument(getPlayer(), Note.sharp(1, Tone.F));
			} else if(Count.equals(10)) {
				SoundLib.BELL.playInstrument(Players, Note.natural(1, Tone.G));
				SoundLib.BELL.playInstrument(getPlayer(), Note.natural(1, Tone.G));
			} else if(Count.equals(12)) {
				SoundLib.BELL.playInstrument(Players, Note.sharp(1, Tone.F));
				SoundLib.BELL.playInstrument(getPlayer(), Note.sharp(1, Tone.F));
			} else if(Count.equals(13)) {
				SoundLib.BELL.playInstrument(Players, Note.natural(0, Tone.E));
				SoundLib.BELL.playInstrument(getPlayer(), Note.natural(0, Tone.E));
			}
			
			Count++;
		}
		
		@Override
		public void onEnd() {}
		
	}.setPeriod(3);

	HashMap<Player, ItemStack> Players;
	
	boolean Binding = false;
	
	DurationTimer Duration = new DurationTimer(this, DurationConfig.getValue(), Cool) {
		
		@Override
		public void onDurationStart() {
			Binding = true;
			Players = new HashMap<Player, ItemStack>();
			LocationUtil.getNearbyPlayers(getPlayer(), 30, 30).stream().forEach(p -> Players.put(p, p.getInventory().getHelmet()));
			Song.StartTimer();
		}
		
		@Override
		public void DurationProcess(Integer Seconds) {
			ItemStack Pumpkin = getPumpkin(Seconds);
			Players.keySet().stream().forEach(p -> p.getInventory().setHelmet(Pumpkin));
		}
		
		@Override
		public void onDurationEnd() {
			Players.keySet().stream().forEach(p -> p.getInventory().setHelmet(Players.get(p)));
			Binding = false;
		}
		
		private ItemStack getPumpkin(Integer Time) {
			ItemStack Pumpkin = new ItemStack(Material.PUMPKIN);
			ItemMeta PumpkinMeta = Pumpkin.getItemMeta();
			PumpkinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6ȣ��"));
			PumpkinMeta.setLore(Messager.getStringList(
					ChatColor.translateAlternateColorCodes('&', "&f�� ȣ�� ���� �� �� ��"),
					ChatColor.translateAlternateColorCodes('&', "&f���� �ð�&7: &a" + Time + "��")
					));
			Pumpkin.setItemMeta(PumpkinMeta);
			EnchantLib.BINDING_CURSE.addEnchantment(Pumpkin, 1);
			return Pumpkin;
		}
		
	};

	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Duration.isDuration() && !Cool.isCooldown()) {
					Duration.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void PassiveSkill(Event event) {
		if(ServerVersion.getVersion() < 11) {
			if(event instanceof InventoryClickEvent) {
				if(Binding) {
					InventoryClickEvent e = (InventoryClickEvent) event;
					if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
						if(e.getCurrentItem().getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', "&f�� ȣ�� ���� �� �� ��"))) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, Entity entity) {}
	
}