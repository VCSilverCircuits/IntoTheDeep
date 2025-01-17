package vcsc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;
import java.util.OptionalDouble;

import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.abstracts.Block;
import vcsc.teamcode.component.camera.Camera;
import vcsc.teamcode.component.wrist.WristActuator;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristState;

@TeleOp(group = "Test", name = "CameraTest")
public class CameraTest extends OpMode {
    Camera camera;
    WristState wristState;
    MultipleTelemetry telem;
    WristActuator wristActuator;
    ArrayList<Double> angleList;
    double lastAngle = 0;
    int MAX_ANGLE_LIST_SIZE = 50;

    @Override
    public void init() {
        camera = new Camera(hardwareMap);
        wristState = new WristState();
        wristActuator = new WristActuator(hardwareMap);
        wristState.registerActuator(wristActuator);
        GlobalTelemetry.init(telemetry);
        telem = GlobalTelemetry.getInstance();
        angleList = new ArrayList<>();
    }

    @Override
    public void loop() {
//        super.loop();

        camera.loop();
        wristActuator.loop();
        Block block = camera.getBlock();
        if (block != null) {
            telem.addData("Block", block.getColor());
            telem.addData("X", block.getX());
            telem.addData("Y", block.getY());
            telem.addData("Angle", block.getAngle());
            OptionalDouble angleAverage = angleList.stream().mapToDouble(a -> a).average();

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
            telem.addLine("No blocks detected.");
            angleList.clear();
        }
        telem.update();
    }
}
