package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import vcsc.core.util.DcMotorGroup;

@TeleOp(name = "SlideTest", group = "Testing")
public class SlideTest extends OpMode {
    DcMotorEx extension1;
    DcMotorEx extension2;

    @Override
    public void init() {
        extension1 = hardwareMap.get(DcMotorEx.class, "extension1"); // Power forward, encoder back
        extension2 = hardwareMap.get(DcMotorEx.class, "extension2"); // Power reverse, encoder back
        extension2.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotorGroup motors = new DcMotorGroup(extension1, extension2);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        extension1.setPower(-gamepad1.left_stick_y);
        extension2.setPower(-gamepad1.right_stick_y);

        telemetry.addData("Motor1", extension1.getCurrentPosition());
        telemetry.addData("Motor2", extension2.getCurrentPosition());
    }
}
