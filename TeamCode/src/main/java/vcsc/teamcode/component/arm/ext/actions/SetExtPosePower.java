package vcsc.teamcode.component.arm.ext.actions;


import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;

public class SetExtPosePower implements Action {
    ArmExtState extState;
    ArmExtPose targetPose;

    boolean finished = true;
    double power = 1;

    DIRECTION direction;

    public SetExtPosePower(ArmExtState extState, ArmExtPose targetPose, double power) {
        super();
        this.extState = extState;
        this.targetPose = targetPose;
        this.power = power;
    }

    @Override
    public void start() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
//        telemetry.addData("Extending slides to position", targetPose.getLength());
        extState.setPose(targetPose);
        if (targetPose.getLength() > extState.getRealExtensionLength()) {
            direction = DIRECTION.UP;
            extState.setPower(power);
        } else {
            direction = DIRECTION.DOWN;
            extState.setPower(-power);
        }
        finished = false;
        telemetry.addLine("Started slide extension with power");
    }

    @Override
    public void loop() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("[ACTION] SetExtPosePower is running.");
        telemetry.addData("current ext power", extState.getPower());
        telemetry.addData("Extension length", extState.getRealExtensionLength());
        telemetry.addData("Target Extension length", targetPose.getLength());
        if (direction == DIRECTION.UP && extState.getRealExtensionLength() >= targetPose.getLength()) {
            finished = true;
            extState.setPower(0);
            extState.setPose(targetPose);
            telemetry.addLine("Above setpoint, stopping");
        }
        if (direction == DIRECTION.DOWN && extState.getRealExtensionLength() <= targetPose.getLength()) {
            finished = true;
            extState.setPower(0);
            extState.setPose(targetPose);
            telemetry.addLine("Below setpoint, stopping");
        }
        if (!extState.actuatorsInAction()) {
            telemetry.addLine("Actuators not in action, stopping");
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
