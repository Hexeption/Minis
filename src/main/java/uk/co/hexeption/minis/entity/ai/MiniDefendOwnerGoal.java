package uk.co.hexeption.minis.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import uk.co.hexeption.minis.entity.MiniEntity;

/**
 * MiniDefendOwnerGoal
 *
 * With the combat upgrade, a Mini goes after whatever hurt its owner, or whatever its owner attacked.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniDefendOwnerGoal extends TargetGoal {

	private final MiniEntity mini;
	// True to go after whoever hurt the owner, false for whoever the owner hurt
	private final boolean ownersAttacker;
	private LivingEntity candidate;
	private int lastSeen;

	public MiniDefendOwnerGoal(MiniEntity mini, boolean ownersAttacker) {
		super(mini, false);
		this.mini = mini;
		this.ownersAttacker = ownersAttacker;
		this.setFlags(EnumSet.of(Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		Player owner = this.mini.getOwner();
		if (owner == null || !this.mini.canFight()) {
			return false;
		}

		this.candidate = this.ownersAttacker ? owner.getLastHurtByMob() : owner.getLastHurtMob();
		int when = this.ownersAttacker ? owner.getLastHurtByMobTimestamp() : owner.getLastHurtMobTimestamp();
		if (when == this.lastSeen || this.candidate == null || this.candidate == owner) {
			return false;
		}
		if (this.candidate instanceof MiniEntity other && other.isOwnedBy(owner)) {
			return false;
		}
		return this.canAttack(this.candidate, TargetingConditions.DEFAULT);
	}

	@Override
	public void start() {
		this.mob.setTarget(this.candidate);
		Player owner = this.mini.getOwner();
		if (owner != null) {
			this.lastSeen = this.ownersAttacker ? owner.getLastHurtByMobTimestamp() : owner.getLastHurtMobTimestamp();
		}
		super.start();
	}

	@Override
	public boolean canContinueToUse() {
		return this.mini.canFight() && super.canContinueToUse();
	}
}
