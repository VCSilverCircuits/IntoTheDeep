package vcsc.teamcode.component.claw;

import vcsc.core.abstracts.action.Action;

public abstract class ClawAction implements Action {
    protected ClawState clawState;

    public ClawAction(ClawState clawState) {
        this.clawState = clawState;
    }
}
