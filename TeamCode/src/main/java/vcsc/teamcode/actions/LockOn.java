package vcsc.teamcode.actions;

import com.arcrobotics.ftclib.controller.PIDController;
import com.pedropathing.follower.Follower;

import java.util.ArrayList;
import java.util.OptionalDouble;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.abstracts.Block;
import vcsc.teamcode.component.camera.Camera;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristState;

public class LockOn implements Action {
    double x_offset, y_offset;

    PIDController xController, yController;
    Camera camera;
    Follower follower;
    WristState wristState;
    ArrayList<Double> angleList;
    double lastAngle = 0;
    int MAX_ANGLE_LIST_SIZE = 50;
    boolean active = false;
    boolean failure = false;
    double start_heading = 0;
    NeutralAction neutralAction;


    public LockOn(Camera camera, Follower follower, WristState wristState, NeutralAction neutralAction) {
        this.camera = camera;
        this.follower = follower;
        this.wristState = wristState;
        xController = new PIDController(0.002, 0, 0);
        yController = new PIDController(0.002, 0, 0);
        angleList = new ArrayList<>();
        this.neutralAction = neutralAction;
    }

    @Override
    public void start() {
        active = true;
        failure = false;
        start_heading = follower.getPose().getHeading();
        xController.setSetPoint(0);
        yController.setSetPoint(0);
        xController.setTolerance(15);
        yController.setTolerance(15);
        follower.startTeleopDrive();
        angleList.clear();
    }

    public boolean failed() {
        return failure;
    }

    @Override
    public void loop() {
        if (!active) {
            return;
        }

        if (xController.atSetPoint() && yController.atSetPoint()) {
            active = false;
        }

        if (Math.abs(follower.getPose().getHeading() - start_heading) > Math.toRadians(100)) {
            active = false;
            neutralAction.start();
            failure = true;
        }

        Block block = camera.getBlock();
        if (block != null) {
            x_offset = block.getX() - 160;
            y_offset = 80 - block.getY();

//            telem.addData("Block", block.getColor());
//            telem.addData("X", block.getX());
//            telem.addData("Y", block.getY());
//            telem.addData("X Offset", x_offset);
//            telem.addData("Y Offset", y_offset);
//            telem.addData("Angle", block.getAngle());

            OptionalDouble angleAverage = angleList.stream().mapToDouble(a -> a).average();

            follower.setTeleOpMovementVectors(yController.calculate(-y_offset), 0, xController.calculate(x_offset));

            double angle = block.getAngle();
            if (Math.abs(angle) > 85) {
                angle = Math.abs(angle);
            }

            if (angleAverage.isPresent() && Math.abs(angleAverage.getAsDouble() - lastAngle) > 10 || Math.abs(angle - lastAngle) > 20) {

                double poseSpan = WristPivotPose.MAX.getPosition() - WristPivotPose.MIN.getPosition();
                double midPose = (WristPivotPose.MIN.getPosition() + WristPivotPose.MAX.getPosition()) / 2;
                double blockPose = midPose + angle / 90 * poseSpan / 2;

                wristState.setPivot(blockPose);
                lastAngle = angle;
                angleList.clear();
                for (int i = 0; i < MAX_ANGLE_LIST_SIZE; ++i) {
                    angleList.add(angle);
                }
            }
            angleList.add(angle);
            while (angleList.size() > MAX_ANGLE_LIST_SIZE) {
                angleList.remove(0);
            }
        } else {
//            telem.addLine("No blocks detected.");
            follower.setTeleOpMovementVectors(0, 0, 0.15);
            angleList.clear();
        }
    }

    @Override
    public boolean isFinished() {
        if (active) {
            if (xController.atSetPoint() && yController.atSetPoint()) {
                active = false;
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public void cancel() {

    }
}
