package vcsc.teamcode.opmodes.tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

@Config
@TeleOp(name = "OutreachTele", group = "Outreach")
public class Outreach extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    public static double driveSpeed = 0.50; // we can change this via dash

    @Override
    public void init() {
        telemetry.addLine("Hello user, thank you for taking the time to try our robot!");
        telemetry.addLine("Use the left stick to move the bot (forward/backward/move sideways).");
        telemetry.addLine("Use the right stick to rotate the bot.");
        telemetry.addLine("Robot is initializing...");
        telemetry.addLine("");
        telemetry.update();

        // Init those REV motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Set motor directions for now
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addLine("✅ Initialization complete! Click start to begin driving.");
        telemetry.update();

    }

    @Override
    public void loop() {
        double y = gamepad1.left_stick_y * driveSpeed;     // forward/backward
        double x = -gamepad1.left_stick_x * driveSpeed;    // strafe
        double rx = -gamepad1.right_stick_x * driveSpeed;  // rotate

        // Calculate Mecanum Drive values for existance
        double fl = y + x + rx;
        double bl = y - x + rx;
        double fr = y - x - rx;
        double br = y + x - rx;

        double max = Math.max(Math.abs(fl), Math.max(Math.abs(bl), Math.max(Math.abs(fr), Math.abs(br))));
        if (max > 1.0) {
            fl /= max;
            bl /= max;
            fr /= max;
            br /= max;
        }

        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);

        telemetry.addLine("Hello user, thank you for taking the time to try our robot!");
        telemetry.addLine("Use the left stick to move the bot (forward/backward/move sideways).");
        telemetry.addLine("Use the right stick to rotate the bot.");
        telemetry.addData("Your speed for safety is limited to", "%.0f%%", driveSpeed * 100);
        telemetry.update();
    }
}
