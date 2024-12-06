package vcsc.teamcode.opModes.tele;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="Test Individual")
public class Test2 extends LinearOpMode {
    public void testMotor(String name, DcMotorEx motor) {
        while (gamepad1.a) {
            // do nothing
        }
        while (!gamepad1.a && opModeIsActive()) {
            motor.setPower(-gamepad1.right_stick_y);
            telemetry.addLine(name);
            telemetry.addData("Position:", motor.getCurrentPosition());
            telemetry.update();
        }
    }
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx rotation1 = hardwareMap.get(DcMotorEx.class, "rotation1");
        DcMotorEx rotation2 = hardwareMap.get(DcMotorEx.class, "rotation2");
        rotation1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rotation2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DcMotorEx extension1 = hardwareMap.get(DcMotorEx.class, "extension1");
        DcMotorEx extension2 = hardwareMap.get(DcMotorEx.class, "extension2");
        extension1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extension2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        testMotor("Extension1", extension1);
        testMotor("Extension2", extension2);
        testMotor("Rotation1", rotation1);
        testMotor("Rotation2", rotation2);


    }
}
