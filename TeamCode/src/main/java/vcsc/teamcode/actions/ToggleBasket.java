package vcsc.teamcode.actions;


import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.claw.ClawState;

public class ToggleBasket implements Action {
    ArmExtState armExtState;
    BasketPose basketPose;
    DownFromBasket downFromBasket;
    ClawState clawState;

    Action currentAction;
    ElapsedTime clawTimer;

    boolean cancelled = false;
    double clawDelay = 150;

    public ToggleBasket(ArmExtState armExtState, ClawState clawState, BasketPose basketPose, DownFromBasket downFromBasket) {
        super();
        this.armExtState = armExtState;
        this.clawState = clawState;
        this.basketPose = basketPose;
        this.downFromBasket = downFromBasket;
        clawTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    }

    @Override
    public void start() {
        cancelled = false;
        if (armExtState.getPose() == ArmExtPose.BASKET) {
            clawState.open();
            clawTimer.reset();
        } else {
            basketPose.start();
            downFromBasket.cancel();
            currentAction = basketPose;
        }
    }

    @Override
    public void loop() {
        if (armExtState.getPose() == ArmExtPose.BASKET && !cancelled) {
            if (clawTimer.time() > clawDelay && currentAction == null) {
                downFromBasket.start();
                basketPose.cancel();
                currentAction = downFromBasket;
            }
        }
        if (currentAction != null) {
            currentAction.loop();
            if (currentAction.isFinished()) {
                currentAction = null;
            }
        }
    }

    @Override
    public boolean isFinished() {
        if (currentAction != null) {
            return currentAction.isFinished();
        }
        return (clawTimer.time() > clawDelay);
    }

    @Override
    public void cancel() {
        cancelled = true;
        if (currentAction != null) {
            currentAction.cancel();
            currentAction = null;
        }
    }
}
