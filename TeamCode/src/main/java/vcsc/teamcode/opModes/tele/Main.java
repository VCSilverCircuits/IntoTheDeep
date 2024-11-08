package vcsc.teamcode.opModes.tele;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

import roadrunner.MecanumDrive;
import vcsc.teamcode.Arm;
import vcsc.teamcode.Claw;
import vcsc.teamcode.Hooks;
import vcsc.teamcode.Wrist;

@Config
@TeleOp(name = "Main", group = "Competition")
public class Main extends OpMode {
    public static double bottom = -60;
    public static double top = -780;
    Arm arm;
    Wrist wrist;
    Claw claw;
    Hooks hooks;
    MecanumDrive drive;
    ArrayList<Double> wristPosList = new ArrayList<Double>() {
        {
            add(0.3);
            add(0.87);
        }
    };
    int wristPosNum = 0;
    double ext = 0, rot = -350;
    boolean rightDebounce = false, leftDebounce = false, bumperDebounce = false, xDebounce = false;
    boolean up = false;

    @Override
    public void init() {
        arm = new Arm((HardwareMap) hardwareMap);
        wrist = new Wrist(hardwareMap);
        claw = new Claw(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        hooks = new Hooks(hardwareMap);
        hooks.raise();
        arm.setPosition(ext, rot);
    }

    @Override
    public void init_loop() {
        arm.update(telemetry);
    }

    @Override
    public void loop() {
        claw.setPosition(gamepad1.right_trigger);


        if (gamepad1.dpad_down) { // INTAKE
            wrist.setAngleY(0.6);
            wrist.setAngleX(1);
        }
        if (gamepad1.dpad_right) { // PULL_OUT
            wrist.setAngleX(0.65);
        }
        if (gamepad1.dpad_up) { // SCORE_BASKET
            wrist.setAngleX(0.5);
            arm.setPosition(0.5,0.25);
        }
        if (gamepad1.dpad_left) { // SCORE_HOOK
            wrist.setAngleX(0.45);
        }
        if (gamepad1.right_stick_button) {
            if (!rightDebounce) {
                wristPosNum = (wristPosNum + 1) % wristPosList.size();
            }
            rightDebounce = true;
        } else {
            rightDebounce = false;
        }

        /*if (gamepad1.left_stick_button) {
            if (!leftDebounce) {
                if (wristPosNum > 0) {
                    wristPosNum--;
                }
            }
            leftDebounce = true;
        } else {
            leftDebounce = false;
        }*/

        if (gamepad1.left_bumper) {
            if (!bumperDebounce) {
                up = !up;
            }
            bumperDebounce = true;
        } else {
            bumperDebounce = false;
        }

        if (up) {
            rot = top;
        } else {
            rot = bottom;
        }

        wrist.setAngleY(wristPosList.get(wristPosNum));

        //rot += gamepad1.right_stick_x * 5;
        ext -= gamepad1.right_stick_y * 50;

        if (up) {
            ext = Math.min(ext, 3300);
        } else {
            ext = Math.min(ext, 1600);
        }
        ext = Math.max(ext, 0);
        rot = Math.min(rot, 0);
        rot = Math.max(rot, -870);

//        arm.setPosition(ext, rot);
        arm.setRotation(rot);
        arm.update(telemetry);
        arm.setExtensionPower(-gamepad1.right_stick_y);

        if (gamepad1.a) {
            if (!xDebounce) {
                hooks.toggle();
            }
            xDebounce = true;
        } else {
            xDebounce = false;
        }

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        drive.updatePoseEstimate();
    }
}
