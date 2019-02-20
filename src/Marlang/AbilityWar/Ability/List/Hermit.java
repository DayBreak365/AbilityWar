package Marlang.AbilityWar.Ability.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import Marlang.AbilityWar.Ability.AbilityBase;
import Marlang.AbilityWar.Config.AbilitySettings.SettingObject;
import Marlang.AbilityWar.Utils.Library.Packet.TitlePacket;
import Marlang.AbilityWar.Utils.Math.LocationUtil;
import Marlang.AbilityWar.Utils.Thread.AbilityWarThread;

public class Hermit extends AbilityBase {

	public static SettingObject<Integer> DistanceConfig = new SettingObject<Integer>("�츣��", "Distance", 15, 
			"# ��ĭ �̳��� �÷��̾ ������ �� �˸��� ����� �����մϴ�.") {
		
		@Override
		public boolean Condition(Integer value) {
			return value >= 1;
		}
		
	};

	public Hermit(Player player) {
		super(player, "�츣��", Rank.C,
				ChatColor.translateAlternateColorCodes('&', "&f�ڽ��� �ֺ� " + DistanceConfig.getValue() + "ĭ ���� �÷��̾ ���� ��� �˷��ݴϴ�."));
	}

	@Override
	public boolean ActiveSkill(ActiveMaterialType mt, ActiveClickType ct) {
		return false;
	}

	Integer Distance = DistanceConfig.getValue();
	
	@Override
	public void PassiveSkill(Event event) {
		if(event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			Player p = e.getPlayer();
			if(p.getWorld().equals(getPlayer().getWorld())) {
				if(!LocationUtil.isInCircle(e.getFrom(), getPlayer().getLocation(), Double.valueOf(Distance)) && 
						LocationUtil.isInCircle(e.getTo(), getPlayer().getLocation(), Double.valueOf(Distance))) {
					if(AbilityWarThread.isGameTaskRunning() && AbilityWarThread.getGame().isParticipating(p)) {
						TitlePacket title = new TitlePacket(ChatColor.translateAlternateColorCodes('&', "&8�츣��"),
								ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + " &f������"), 5, 30, 5);
						title.Send(getPlayer());
					}
				}
			}
		}
	}

	@Override
	public void onRestrictClear() {}

}