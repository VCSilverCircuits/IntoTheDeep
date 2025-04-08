package vcsc.teamcode.opmodes.tele;

//Drive-only opmode for outreach events, Undertow V1

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "OutreachTele", group = "Main")
public class Outreach extends BaseOpMode {
    final double DEFAULT_TURN_SPEED = 0.5;
    final double DEFAULT_DRIVE_SPEED = 1;
    double driveSpeed = DEFAULT_DRIVE_SPEED;
    double turnSpeed = DEFAULT_TURN_SPEED;

    @Override
    public void init() {
        super.init();
        follower.setStartingPose(new Pose(0, 0, 0));
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {
        super.loop();

        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y * driveSpeed, -gamepad1.left_stick_x * driveSpeed, -gamepad1.right_stick_x * turnSpeed, true);
//        drive.setDrivePowers(new PoseVelocity2d(
//                new Vector2d(
//                        -gamepad1.left_stick_y * driveSpeed,
//                        -gamepad1.left_stick_x * driveSpeed
//                ),
//                -gamepad1.right_stick_x * turnSpeed
//        ));
        }
    }
