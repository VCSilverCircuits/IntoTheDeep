package vcsc.teamcode.actions;


import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;

public class Cancel implements Action {
    ArmRotState rotState;
    NeutralAction neutralAction;
    DownFromBasket downFromBasket;
    Action currentAction;

    public Cancel(ArmRotState rotState, NeutralAction neutralAction, DownFromBasket downFromBasket) {
        super();
        this.rotState = rotState;
        this.neutralAction = neutralAction;
        this.downFromBasket = downFromBasket;
    }

    @Override
    public void start() {
        if (rotState.getPose() == ArmRotPose.BASKET) {
            downFromBasket.start();
            currentAction = downFromBasket;
        } else {
            neutralAction.start();
            currentAction = neutralAction;
        }
    }

    @Override
    public void loop() {
        if (currentAction != null) {
            currentAction.loop();
        } else {
            return;
        }
        if (currentAction != null && currentAction.isFinished()) {
            currentAction = null;
        }
    }

    @Override
    public boolean isFinished() {
        if (currentAction != null) {
            return currentAction.isFinished();
        }
        return true;
    }

    @Override
    public void cancel() {
        if (currentAction != null) {
            currentAction.cancel();
        }
    }
}
