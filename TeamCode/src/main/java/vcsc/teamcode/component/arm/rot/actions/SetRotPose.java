package vcsc.teamcode.component.arm.rot.actions;


import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;

public class SetRotPose implements Action {
    ArmRotState rotState;
    ArmRotPose targetPose;

    boolean finished = true;

    DIRECTION direction;

    public SetRotPose(ArmRotState rotState, ArmRotPose targetPose) {
        super();
        this.rotState = rotState;
        this.targetPose = targetPose;
        finished = true;
    }

    @Override
    public void start() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
//        telemetry.addData("Rotating slides to position", targetPose.getAngle());
        if (targetPose.getAngle() > rotState.getRealPosition()) {
            direction = DIRECTION.UP;
        } else {
            direction = DIRECTION.DOWN;
        }
        finished = false;
        rotState.setPose(targetPose);
    }

    @Override
    public void loop() {
        if (direction == DIRECTION.UP && rotState.getRealPosition() > targetPose.getAngle()) {
            finished = true;
        }
        if (direction == DIRECTION.DOWN && rotState.getRealPosition() < targetPose.getAngle()) {
            finished = true;
        }
        if (!rotState.actuatorsInAction()) {
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
