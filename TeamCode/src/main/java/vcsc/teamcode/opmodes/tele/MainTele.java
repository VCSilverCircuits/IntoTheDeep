package vcsc.teamcode.opmodes.tele;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.core.util.GamepadButton;
import vcsc.teamcode.actions.BasketPose;
import vcsc.teamcode.actions.IntakePose;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "MainTele", group = "Main")
public class MainTele extends BaseOpMode {
    boolean rumbledEndGame = false, rumbledMatchEnd = false;

    BasketPose basketPose;
    IntakePose intakePose;

    @Override
    public void init() {
        super.init();
        // ===== Actions =====
        basketPose = new BasketPose(rotState,extState,elbowState,wristState);
        intakePose = new IntakePose(rotState,extState,clawState);

        // ===== Button Bindings =====
        gw1.bindButton(GamepadButton.LEFT_TRIGGER,basketPose);
        gw1.bindButton(GamepadButton.RIGHT_TRIGGER,intakePose);
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
