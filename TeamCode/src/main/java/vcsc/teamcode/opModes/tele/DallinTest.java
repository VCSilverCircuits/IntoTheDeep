package vcsc.teamcode.opModes.tele;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import roadrunner.MecanumDrive;
import vcsc.teamcode.Claw;
import vcsc.teamcode.Wrist;
import vcsc.teamcode.WristPose;

@TeleOp(name = "DallinTest")
public class DallinTest extends OpMode {
    Claw claw;
    Wrist wrist;

    MecanumDrive drive;

    @Override
    public void init() {
        claw = new Claw(hardwareMap);
        wrist = new Wrist(hardwareMap);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        wrist.setPose(WristPose.STOW);

    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            wrist.setPose(WristPose.INTAKE);
        } else if (gamepad1.b) {
            wrist.setPose(WristPose.SCORING);
        } else {
            wrist.setPose(WristPose.STOW);
        }
        // Drive
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
