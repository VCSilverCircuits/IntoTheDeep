package vcsc.teamcode.actions;


import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.actions.basket.BasketPoseAuto;
import vcsc.teamcode.actions.basket.DownFromBasket;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.claw.ClawState;

public class ToggleBasketAuto implements Action {
    ArmExtState armExtState;
    BasketPoseAuto basketPose;
    DownFromBasket downFromBasket;
    ClawState clawState;

    Action currentAction;
    ElapsedTime clawTimer;

    boolean cancelled = false;
    boolean started = false;
    double clawDelay = 500;

    public ToggleBasketAuto(ArmExtState armExtState, ClawState clawState, BasketPoseAuto basketPose, DownFromBasket downFromBasket) {
        super();
        this.armExtState = armExtState;
        this.clawState = clawState;
        this.basketPose = basketPose;
        this.downFromBasket = downFromBasket;
        clawTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    }

    @Override
    public void start() {
        started = true;
        cancelled = false;
        if (armExtState.getPose() == ArmExtPose.BASKET_AUTO) {
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
        if (!started) {
            return;
        }
        if (armExtState.getPose() == ArmExtPose.BASKET_AUTO && !cancelled) {
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
                started = false;
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
        started = false;
        if (currentAction != null) {
            currentAction.cancel();
            currentAction = null;
        }
    }
}
