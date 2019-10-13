package daybreak.abilitywar.ability.list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.timer.CooldownTimer;
import daybreak.abilitywar.config.AbilitySettings.SettingObject;
import daybreak.abilitywar.game.games.mode.AbstractGame.Participant;
import daybreak.abilitywar.utils.Messager;
import daybreak.abilitywar.utils.thread.AbilityWarThread;

@AbilityManifest(Name = "유명 인사", Rank = Rank.D, Species = Species.HUMAN)
public class Celebrity extends AbilityBase {

	public static SettingObject<Integer> CooldownConfig = new SettingObject<Integer>(Celebrity.class, "Cooldown", 25,
			"# 쿨타임") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 0;
		}
		
	};

	public Celebrity(Participant participant) {
		super(participant,
				ChatColor.translateAlternateColorCodes('&', "&f철괴를 우클릭하면 모든 플레이어가 자신의 방향을 바라봅니다. " + Messager.formatCooldown(CooldownConfig.getValue())));
	}
	
	private CooldownTimer Cool = new CooldownTimer(this, CooldownConfig.getValue());
	
	@Override
	public boolean ActiveSkill(MaterialType mt, ClickType ct) {
		if(mt.equals(MaterialType.Iron_Ingot)) {
			if(ct.equals(ClickType.RightClick)) {
				if(!Cool.isCooldown()) {
					
					if(AbilityWarThread.isGameTaskRunning()) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f안녕하세요, 여러분! 전 세계적으로 &c선풍적인 &f인기를 끌고있는 &e" + getPlayer().getName() + "&f입니다! @==(^o^)@"));
						for(Participant participant : AbilityWarThread.getGame().getParticipants()) {
							Player p = participant.getPlayer();
							if(!p.equals(getPlayer())) {
								Vector vector = getPlayer().getLocation().toVector().subtract(p.getLocation().toVector());
								p.teleport(p.getLocation().setDirection(vector));
							}
						}
					}
					
					Cool.StartTimer();
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void onRestrictClear() {}

	@Override
	public void TargetSkill(MaterialType mt, LivingEntity entity) {}
	
}