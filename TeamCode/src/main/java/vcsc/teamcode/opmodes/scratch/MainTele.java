package vcsc.teamcode.opmodes.scratch;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "MainTele", group = "Main")
public class MainTele extends BaseOpMode {
    boolean rumbledEndGame = false, rumbledMatchEnd = false;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();

        // ===== Drivetrain =====
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        // ===== Hanging =====

        if (matchTimer.seconds() >= 90 && !rumbledEndGame) {
            gamepad1.rumble(500);
        } else if (matchTimer.seconds() >= 120 && !rumbledMatchEnd) {
            gamepad1.rumbleBlips(3);
        }
    }
}
