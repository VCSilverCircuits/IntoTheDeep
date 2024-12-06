package vcsc.teamcode.opModes.tele;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

import roadrunner.MecanumDrive;
import vcsc.teamcode.Arm;
import vcsc.teamcode.Claw;
import vcsc.teamcode.Hooks;
import vcsc.teamcode.Wrist;

//Aiden better main bc aiden's better than dallin

@TeleOp(name = "AidenBetterMain",group = "Competition")
public class AidenBetterMain extends OpMode {
    public static double bottom = -60;
    public static double top = -780;
    Arm arm;
    Wrist wrist;
    Claw claw;
    Hooks hooks;
    MecanumDrive drive;
    ArrayList<Double> wristPosList = new ArrayList<Double>() {
        {
            add(0.6);
            add(0.05);
        }
    };
    int wristPosNum = 0;
    double ext = 0, rot = -350;
    boolean rightDebounce = false, leftDebounce = false, bumperDebounce = false, xDebounce = false;
    @Override
    public void init() {
        arm = new Arm((HardwareMap) hardwareMap);
        wrist = new Wrist(hardwareMap);
        claw = new Claw(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        hooks = new Hooks(hardwareMap);
        hooks.raise();
        arm.setPosition(0, 45);
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_down) { // INTAKE
            wrist.setAngleY(0.6);
            wrist.setAngleX(1);
        }
        if (gamepad1.dpad_right) { // PULL_OUT
            wrist.setAngleX(0.65);
        }
        if (gamepad1.dpad_up) { // SCORE_BASKET
            wrist.setAngleX(0.5);
            arm.setPosition(21, 90);
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
        if (gamepad1.a) {
            if (!xDebounce) {
                hooks.toggle();
            }
            bumperDebounce = true;
        } else {
            bumperDebounce = false;
        }
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        drive.updatePoseEstimate();
        wrist.setAngleY(wristPosList.get(wristPosNum));
        arm.setOutputPower(-gamepad1.right_stick_y);
        arm.update(telemetry);
    }


}
