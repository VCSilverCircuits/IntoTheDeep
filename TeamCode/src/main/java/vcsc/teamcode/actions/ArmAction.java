package vcsc.teamcode.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.wrist.WristState;

public abstract class ArmAction implements Action {
    protected ArmRotState rotState;
    protected ArmExtState extState;
    protected ElbowState elbowState;
    protected WristState wristState;

    public ArmAction(ArmRotState rotState, ArmExtState extState, ElbowState elbowState, WristState wristState) {
        this.rotState = rotState;
        this.extState = extState;
        this.elbowState = elbowState;
        this.wristState = wristState;
    }
}
