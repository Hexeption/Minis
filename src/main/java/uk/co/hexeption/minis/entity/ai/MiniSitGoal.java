package uk.co.hexeption.minis.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.entity.MiniMode;

/**
 * MiniSitGoal
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniSitGoal extends Goal {

	private final MiniEntity mini;

	public MiniSitGoal(MiniEntity mini) {
		this.mini = mini;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return mini.getMode() == MiniMode.SIT;
	}

	@Override
	public void start() {
		mini.getNavigation().stop();
	}
}
