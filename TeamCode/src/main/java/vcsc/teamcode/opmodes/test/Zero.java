package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Disabled
@TeleOp(name = "Zero", group = "Testing")
public class Zero extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        ServoImplEx claw = hardwareMap.get(ServoImplEx.class, "claw");
        ServoImplEx wristRot = hardwareMap.get(ServoImplEx.class, "wristRot");
        ServoImplEx wristPivot = hardwareMap.get(ServoImplEx.class, "wristPivot");
        ServoImplEx elbow = hardwareMap.get(ServoImplEx.class, "elbow");
        waitForStart();
        claw.setPosition(0);
        wristPivot.setPosition(0);
        wristRot.setPosition(0);
        elbow.setPosition(0);
        while (!isStopRequested()) {
            // Wait
        }
    }
}
