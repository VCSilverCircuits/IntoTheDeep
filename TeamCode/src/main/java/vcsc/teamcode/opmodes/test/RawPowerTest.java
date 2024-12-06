package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import vcsc.core.util.DcMotorGroup;

@TeleOp(group = "Test", name = "RawPower")
public class RawPowerTest extends OpMode {
    DcMotorGroup motors;

    @Override
    public void init() {
        DcMotorEx extension1 = hardwareMap.get(DcMotorEx.class, "extension1");
        DcMotorEx extension2 = hardwareMap.get(DcMotorEx.class, "extension2");
        extension2.setDirection(DcMotorSimple.Direction.REVERSE);
        motors = new DcMotorGroup(extension1, extension2);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        motors.setPower(-gamepad1.left_stick_y);
    }
}
