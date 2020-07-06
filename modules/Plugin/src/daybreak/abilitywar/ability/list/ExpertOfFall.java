package daybreak.abilitywar.ability.list;

import daybreak.abilitywar.ability.AbilityBase;
import daybreak.abilitywar.ability.AbilityManifest;
import daybreak.abilitywar.ability.AbilityManifest.Rank;
import daybreak.abilitywar.ability.AbilityManifest.Species;
import daybreak.abilitywar.ability.SubscribeEvent;
import daybreak.abilitywar.game.AbstractGame.Participant;
import daybreak.abilitywar.utils.base.concurrent.TimeUnit;
import daybreak.abilitywar.utils.base.math.LocationUtil;
import daybreak.abilitywar.utils.base.minecraft.FallingBlocks;
import daybreak.abilitywar.utils.base.minecraft.FallingBlocks.Behavior;
import daybreak.abilitywar.utils.library.SoundLib;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Damageable;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

@AbilityManifest(name = "낙법의 달인", rank = Rank.B, species = Species.HUMAN, explain = {
		"수십년간의 고된 수련으로 낙법과 하나가 된 낙법의 달인.",
		"낙하해 땅에 닿았을 때 자동으로 물낙법을 하며, 낙하 거리에 비례해",
		"주변 4칸 내의 생명체들에게 대미지를 줍니다."
})
public class ExpertOfFall extends AbilityBase {

	public ExpertOfFall(Participant participant) {
		super(participant);
	}

	@SubscribeEvent
	private void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity().equals(getPlayer())) {
			if (e.getCause().equals(DamageCause.FALL)) {
				e.setCancelled(true);
				Block block = getPlayer().getLocation().getBlock();
				Material blockType = block.getType();
				new Timer(1) {
					@Override
					protected void onStart() {
						block.setType(Material.WATER);
					}

					@Override
					protected void run(int count) {
					}

					@Override
					protected void onEnd() {
						block.setType(blockType);
					}
				}.setPeriod(TimeUnit.TICKS, 10).start();
				SoundLib.ENTITY_PLAYER_SPLASH.playSound(getPlayer());

				Block belowBlock = block.getRelative(BlockFace.DOWN);
				for (int i = 0; i < 3; i++) {
					FallingBlocks.spawnFallingBlock(belowBlock.getLocation().add(0, 1, 0), belowBlock.getType(), false, getPlayer().getLocation().toVector().subtract(belowBlock.getLocation().toVector()).multiply(-0.1).setY(Math.random()), Behavior.FALSE);
				}
				for (Damageable damageable : LocationUtil.getNearbyDamageableEntities(getPlayer(), 4, 4)) {
					damageable.damage(Math.min(getPlayer().getFallDistance() / 0.85, 25) * (1 - (getPlayer().getLocation().distanceSquared(damageable.getLocation()) / 16)), getPlayer());
				}
			}
		}
	}

}
