package vcsc.teamcode.component.arm.elbow;

import vcsc.core.abstracts.Action;
import vcsc.teamcode.component.claw.ClawState;

public abstract class ElbowAction implements Action {
    protected ElbowState elbowState;

    public ElbowAction(ElbowState elbowState) {
        this.elbowState = elbowState;
    }
}
