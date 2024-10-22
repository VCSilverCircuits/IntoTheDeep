package vcsc.teamcode.opModes.tele;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.teamcode.Arm;
import vcsc.teamcode.Claw;

public class Test extends OpMode {
    Claw claw;
    Arm arm;

    @Override
    public void init() {
        claw =  new Claw(hardwareMap.get(ServoImplEx.class, "claw"));
        arm = new Arm(hardwareMap.get(DcMotorEx.class, "rotation"));
        // Creates a PIDController with gains kP, kI, and kD
    }

    @Override
    public void loop() {
        if (gamepad1.left_bumper) {
            claw.open();
        } else {
            claw.close();
        }
    }
}
