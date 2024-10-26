package vcsc.teamcode.opModes.tele;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.teamcode.Arm;
import vcsc.teamcode.Claw;

@Disabled
@TeleOp(name = "Test")
public class Test extends OpMode {
    Claw claw;
    Arm arm;

    @Override
    public void init() {
        claw = new Claw(hardwareMap);
        arm = new Arm(hardwareMap);

    }

    @Override
    public void loop() {
        claw.setPosition(gamepad1.right_trigger);
//        arm.extension.setPower(-gamepad1.right_stick_y);
//        arm.setExtensionLength(42);
    }
}
