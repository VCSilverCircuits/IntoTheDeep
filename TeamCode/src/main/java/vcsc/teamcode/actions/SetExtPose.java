package vcsc.teamcode.actions;


import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;

public class SetExtPose implements Action {
    ArmExtState extState;
    ArmExtPose targetPose;

    boolean finished = false;

    DIRECTION direction;

    public SetExtPose(ArmExtState extState, ArmExtPose targetPose) {
        super();
        this.extState = extState;
        this.targetPose = targetPose;
        if (targetPose.getLength() > extState.getRealPosition()) {
            direction = DIRECTION.UP;
        } else {
            direction = DIRECTION.DOWN;
        }
    }

    @Override
    public void start() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addData("Extending slides to position", targetPose.getLength());
        extState.setPose(targetPose);
    }

    @Override
    public void loop() {
        if (direction == DIRECTION.UP && extState.getRealPosition() >= targetPose.getLength()) {
            finished = true;
        }
        if (direction == DIRECTION.DOWN && extState.getRealPosition() <= targetPose.getLength()) {
            finished = true;
        }
        if (!extState.actuatorsInAction()) {
            finished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void cancel() {
        finished = true;
    }

    private enum DIRECTION {
        UP,
        DOWN
    }
}
