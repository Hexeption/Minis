package uk.co.hexeption.minis.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.entity.MiniMode;

/**
 * MiniFollowOwnerGoal
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniFollowOwnerGoal extends Goal {

	private static final double TELEPORT_DISTANCE = 12.0D;

	private final MiniEntity mini;
	private final double speed;
	private final double startDistance;
	private final double stopDistance;
	private Player owner;
	private int timeToRecalcPath;

	public MiniFollowOwnerGoal(MiniEntity mini, double speed, double startDistance, double stopDistance) {
		this.mini = mini;
		this.speed = speed;
		this.startDistance = startDistance;
		this.stopDistance = stopDistance;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (mini.getMode() != MiniMode.FOLLOW) {
			return false;
		}

		Player owner = mini.getOwner();
		if (owner == null || owner.isSpectator() || mini.distanceToSqr(owner) < startDistance * startDistance) {
			return false;
		}

		this.owner = owner;
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		return mini.getMode() == MiniMode.FOLLOW && owner.isAlive() && mini.distanceToSqr(owner) > stopDistance * stopDistance;
	}

	@Override
	public void start() {
		timeToRecalcPath = 0;
	}

	@Override
	public void stop() {
		owner = null;
		mini.getNavigation().stop();
	}

	@Override
	public void tick() {
		mini.getLookControl().setLookAt(owner, 10.0F, mini.getMaxHeadXRot());
		if (--timeToRecalcPath > 0) {
			return;
		}

		timeToRecalcPath = adjustedTickDelay(10);
		if (mini.distanceToSqr(owner) >= TELEPORT_DISTANCE * TELEPORT_DISTANCE) {
			mini.getNavigation().stop();
			mini.teleportTo(owner.getX(), owner.getY(), owner.getZ());
		} else {
			mini.getNavigation().moveTo(owner, speed);
		}
	}
}
