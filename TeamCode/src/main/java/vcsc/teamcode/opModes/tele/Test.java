package vcsc.teamcode.opModes.tele;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import roadrunner.MecanumDrive;
import vcsc.core.GlobalTelemetry;
import vcsc.redesign.DcMotorGroup;

@TeleOp(name = "Test", group = "Test")
public class Test extends LinearOpMode {
    MecanumDrive drive;

    @Override
    public void runOpMode() {
        GlobalTelemetry.init(telemetry);

        MultipleTelemetry mt = GlobalTelemetry.getInstance();

        waitForStart();

        DcMotorEx rotation1 = hardwareMap.get(DcMotorEx.class, "rotation1");
        DcMotorEx rotation2 = hardwareMap.get(DcMotorEx.class, "rotation2");
        DcMotorGroup rotation = new DcMotorGroup(rotation1, rotation2);

        DcMotorEx extension1 = hardwareMap.get(DcMotorEx.class, "extension1");
        DcMotorEx extension2 = hardwareMap.get(DcMotorEx.class, "extension2");
        extension2.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotorGroup extension = new DcMotorGroup(extension1, extension2);

        rotation.setDirection(DcMotorSimple.Direction.REVERSE);

        DcMotorGroup allMotors = new DcMotorGroup(extension, rotation);
        allMotors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        allMotors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        allMotors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        while (opModeIsActive()) {
            double power = -gamepad1.right_stick_y;

            if (-extension.getCurrentPosition() < 50) {
                power = Math.max(0, power);
            } else if (-extension.getCurrentPosition() > 3000) {
                power = Math.min(0, power);
            }
            extension.setPower(power);
            rotation.setPower(-gamepad1.right_stick_x);
            mt.addData("Rotation1", rotation1.getCurrentPosition());
            mt.addData("Rotation2", rotation2.getCurrentPosition());
            mt.addData("Extension1", extension1.getCurrentPosition());
            mt.addData("Extension2", extension2.getCurrentPosition());
            mt.update();
        }

    }
}