package daybreak.abilitywar.utils.base.minecraft.raytrace.v1_16_R2;

import daybreak.abilitywar.utils.base.minecraft.raytrace.IRayTrace;
import net.minecraft.server.v1_16_R2.MovingObjectPosition.EnumMovingObjectType;
import net.minecraft.server.v1_16_R2.MovingObjectPositionBlock;
import net.minecraft.server.v1_16_R2.RayTrace;
import net.minecraft.server.v1_16_R2.RayTrace.BlockCollisionOption;
import net.minecraft.server.v1_16_R2.RayTrace.FluidCollisionOption;
import net.minecraft.server.v1_16_R2.Vec3D;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;

public class RayTraceImpl implements IRayTrace {
	@Override
	public boolean hitsBlock(World world, double ax, double ay, double az, double bx, double by, double bz) {
		final MovingObjectPositionBlock rayTrace = ((CraftWorld) world).getHandle().rayTrace(new RayTrace(new Vec3D(ax, ay, az), new Vec3D(bx, by, bz), BlockCollisionOption.COLLIDER, FluidCollisionOption.NONE, null));
		return rayTrace != null && rayTrace.getType() != EnumMovingObjectType.MISS;
	}
}
