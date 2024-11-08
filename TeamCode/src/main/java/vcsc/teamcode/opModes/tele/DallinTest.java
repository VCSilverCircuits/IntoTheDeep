package vcsc.teamcode.opModes.tele;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import roadrunner.MecanumDrive;
import vcsc.teamcode.Arm;
import vcsc.teamcode.Claw;
import vcsc.teamcode.Wrist;

@Disabled
@TeleOp(name = "DallinTest")
public class DallinTest extends OpMode {
    Arm arm;
    Claw claw;
    Wrist wrist;

    MecanumDrive drive;

    ServoImplEx clawServo;
    double rot;
    double ext;

    @Override
    public void init() {
        claw = new Claw(hardwareMap);
        arm = new Arm(hardwareMap);
//        wrist = new Wrist(hardwareMap);
//        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        //wrist.setPose(WristPose.STOW);
        clawServo = hardwareMap.get(ServoImplEx.class, "wristY");
        rot = 0;
        ext = 50;
    }

    @Override
    public void loop() {

        rot += gamepad1.right_stick_x * 5;
        ext -= gamepad1.right_stick_y * 25;

        ext = Math.min(ext, 3300);
        ext = Math.max(ext, 0);

        rot = Math.min(rot, 0);
        rot = Math.max(rot, -870);

        telemetry.addData("Extending to", ext);
        telemetry.addData("Currently ext. to", arm.getExtension());
        telemetry.addData("Rotation", rot);

        arm.setPosition(ext, rot);
        arm.update(telemetry);

        /*pos = clawServo.getPosition();
        pos += gamepad1.right_stick_x / 300;
        if (pos > 1) {
            pos = 1;
        }
        if (pos < 0) {
            pos = 0;
        }
        clawServo.setPosition(pos);
        telemetry.addData("Wrist Pos:", pos);
        claw.setPosition(gamepad1.right_trigger);*/
       /* if (gamepad1.a) {
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

        drive.updatePoseEstimate();*/
    }
}
