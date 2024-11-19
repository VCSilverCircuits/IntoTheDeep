package vcsc.teamcode.armOld.bx;

import vcsc.core.bx.Behavior;
import vcsc.teamcode.armOld.ArmState;

public interface ArmBehavior extends Behavior {
    public ArmState getStartingState();
}
