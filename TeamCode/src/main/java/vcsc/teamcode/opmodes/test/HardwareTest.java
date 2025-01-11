package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.util.DcMotorGroup;

@Disabled
@Autonomous(group = "Testing", name = "HardwareTest")
public class HardwareTest extends LinearOpMode {
    public void testServo(String name, ServoImplEx servo, double min, double max) {
        while (gamepad1.a || gamepad1.b) {
            // Do nothing
        }
        ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.reset();
        servo.setPosition(min);
        while (time.time() < 1500) {
            telemetry.addLine("Testing servo '" + name + "...");
            telemetry.addLine("Running to min position.");
            telemetry.addData("Current Time", time.time());
            telemetry.update();
            if (gamepad1.a || isStopRequested()) {
                break;
            }
        }
        servo.setPosition(max);
        time.reset();
        while (time.time() < 1500) {
            telemetry.addLine("Testing servo '" + name + "...");
            telemetry.addLine("Running to max position.");
            telemetry.addData("Current Time", time.time());
            telemetry.update();
            if (gamepad1.a || isStopRequested()) {
                break;
            }
        }
        telemetry.addLine("Testing complete. Press 'A' to continue or 'B' to retry.");
        telemetry.update();

        while (!gamepad1.a && !gamepad1.b) {
            if (gamepad1.a || isStopRequested()) {
                return;
            }

            if (gamepad1.b || isStopRequested()) {
                testServo(name, servo, min, max);
                return;
            }
        }
    }

    public void testMotorWithPosition(String name, DcMotorEx motor, int min, int max) {
        ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        motor.setTargetPosition(min);
        time.reset();
        while (time.time() < 1000) {
            telemetry.addLine("Testing motor '" + name + "...");
            telemetry.addLine("Running to min position.");
            telemetry.addData("Current Time", time.time());
            telemetry.update();
            if (gamepad1.a || isStopRequested()) {
                break;
            }
        }
        motor.setTargetPosition(max);
        time.reset();
        while (time.time() < 1000) {
            telemetry.addLine("Testing motor '" + name + "...");
            telemetry.addLine("Running to max position.");
            telemetry.addData("Current Time", time.time());
            telemetry.update();
            if (gamepad1.a || isStopRequested()) {
                break;
            }
        }
        telemetry.addLine("Testing complete. Press 'A' to continue or 'B' to retry.");
        telemetry.update();

        while (!gamepad1.a && !gamepad1.b) {
            if (gamepad1.a || isStopRequested()) {
                return;
            }

            if (gamepad1.b || isStopRequested()) {
                testMotorWithPosition(name, motor, min, max);
                return;
            }
        }
    }

    public void testMotorWithPower(String name, DcMotorEx motor, double runTime, double power) {
        ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.reset();
        while (time.time() < runTime) {
            telemetry.addLine("Testing motor '" + name + "...");
            telemetry.addLine("Running forward.");
            telemetry.addData("Current Time", time.time());
            telemetry.update();

            motor.setPower(power);
            if (gamepad1.a || isStopRequested()) {
                break;
            }
        }
        time.reset();
        while (time.time() < runTime) {
            telemetry.addLine("Testing motor '" + name + "...");
            telemetry.addLine("Running backward.");
            telemetry.addData("Current Time", time.time());
            telemetry.update();

            motor.setPower(-power);
            if (gamepad1.a || isStopRequested()) {
                break;
            }
        }
        telemetry.addLine("Testing complete. Press 'A' to continue or 'B' to retry.");
        telemetry.update();

        while (!gamepad1.a && !gamepad1.b) {
            if (gamepad1.a || isStopRequested()) {
                return;
            }

            if (gamepad1.b || isStopRequested()) {
                testMotorWithPower(name, motor, runTime, power);
                return;
            }
        }
    }

    @Override
    public void runOpMode() {
        /*=========
          Servos
        =========*/
        ServoImplEx hookLeft = hardwareMap.get(ServoImplEx.class, "hookLeft");
        ServoImplEx hookRight = hardwareMap.get(ServoImplEx.class, "hookRight");
        ServoImplEx elbow = hardwareMap.get(ServoImplEx.class, "elbow");
        ServoImplEx wristRot = hardwareMap.get(ServoImplEx.class, "wristRot");
        ServoImplEx wristPivot = hardwareMap.get(ServoImplEx.class, "wristPivot");
        ServoImplEx claw = hardwareMap.get(ServoImplEx.class, "claw");

        /*=========
          Motors
        =========*/
        // ----- Drivetrain -----
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // ----- Rotation -----

        DcMotorEx rotation1 = hardwareMap.get(DcMotorEx.class, "rotation1");
        DcMotorEx rotation2 = hardwareMap.get(DcMotorEx.class, "rotation2");
        DcMotorGroup rotation = new DcMotorGroup(rotation1, rotation2);

        // ----- Extension -----
        DcMotorEx extension1 = hardwareMap.get(DcMotorEx.class, "extension1");
        DcMotorEx extension2 = hardwareMap.get(DcMotorEx.class, "extension2");
        extension1.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotorGroup extension = new DcMotorGroup(extension1, extension2);

        // ----- All Motors -----
        DcMotorGroup allMotors = new DcMotorGroup(
                frontLeft, frontRight, backLeft, backRight,
                rotation,
                extension
        );
        allMotors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        allMotors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        allMotors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        /*=========
         Main Code
        =========*/

        telemetry.addLine("Welcome to HardwareTest!");
        telemetry.addLine("Press 'A' after each component to continue, or press 'B' to try again.");
        telemetry.addLine("Start the program to begin.");
        telemetry.update();

        waitForStart();

        testServo("Hook Left", hookLeft, 0, 1);
        testServo("Hook Right", hookRight, 0, 1);
        testServo("Elbow", elbow, 0, 1);
        testServo("WristRot", wristRot, 0, 1);
        testServo("WristPivot", wristPivot, 0, 1);
        testServo("Claw", claw, 0, 1);

//        testMotorWithPower("Extension1", extension1, 500, 0.5);
//        testMotorWithPower("Extension2", extension2, 500, 0.5);
//        testMotorWithPower("Extension (both)", extension, 500, 0.5);
//
//        testMotorWithPower("Rotation1", rotation1, 500, 0.5);
//        testMotorWithPower("Rotation2", rotation2, 500, 0.5);
//        testMotorWithPower("Rotation (both)", rotation, 500, 0.5);
//
//        testMotorWithPower("frontLeft", frontLeft, 500, 0.5);
//        testMotorWithPower("frontRight", frontRight, 500, 0.5);
//        testMotorWithPower("backLeft", backLeft, 500, 0.5);
//        testMotorWithPower("backRight", backRight, 500, 0.5);
    }
}
