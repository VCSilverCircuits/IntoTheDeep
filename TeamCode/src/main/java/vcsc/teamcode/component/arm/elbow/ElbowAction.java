package vcsc.teamcode.component.arm.elbow;

import vcsc.core.abstracts.action.Action;

public abstract class ElbowAction implements Action {
    protected ElbowState elbowState;

    public ElbowAction(ElbowState elbowState) {
        this.elbowState = elbowState;
    }
}
