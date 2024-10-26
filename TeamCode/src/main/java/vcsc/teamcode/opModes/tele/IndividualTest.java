package vcsc.teamcode.opModes.tele;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@TeleOp(name = "IndividualTest")
public class IndividualTest extends OpMode {
    DcMotorEx extension1, extension2, rotation1, rotation2;

    @Override
    public void init() {
        extension1 = hardwareMap.get(DcMotorEx.class, "extension1");
        extension1.setTargetPosition(0);
        extension1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extension1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension1.setDirection(DcMotorSimple.Direction.REVERSE);

        extension2 = hardwareMap.get(DcMotorEx.class, "extension2");
        extension2.setTargetPosition(0);
        extension2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extension2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rotation1 = hardwareMap.get(DcMotorEx.class, "rotation1");
        rotation1.setTargetPosition(0);
        rotation1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotation1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rotation1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rotation2 = hardwareMap.get(DcMotorEx.class, "rotation2");
        rotation2.setTargetPosition(0);
        rotation2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotation2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rotation2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void loop() {
        extension1.setPower(-gamepad1.right_stick_y);
        extension2.setPower(-gamepad1.right_stick_y);
        telemetry.addData("Extension 1", extension1.getCurrentPosition());
        telemetry.addData("Extension 2", extension2.getCurrentPosition());
        telemetry.addData("Rotation 1", rotation1.getCurrentPosition());
        telemetry.addData("Rotation 2", rotation2.getCurrentPosition());
    }
}
