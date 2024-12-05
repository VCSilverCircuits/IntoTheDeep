package vcsc.teamcode.opmodes.tele;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.core.util.GamepadButton;
import vcsc.teamcode.DebugConstants;
import vcsc.teamcode.actions.BasketPose;
import vcsc.teamcode.actions.IntakePose;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.SetRotPose;
import vcsc.teamcode.actions.ToggleHooks;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "MainTele", group = "Main")
public class MainTele extends BaseOpMode {
    boolean rumbledEndGame = false, rumbledMatchEnd = false;

    BasketPose basketPose;
    IntakePose intakePose;
    NeutralAction neutralAction;
    ToggleHooks toggleHooks;
    SetRotPose preHangPose;
    SetRotPose hangPose;
    double wristRotateSpeed = 0.01;

    @Override
    public void init() {
        super.init();
        // ===== Actions =====
        basketPose = new BasketPose(rotState, extState, elbowState, wristState);
        intakePose = new IntakePose(rotState, extState, clawState);
        neutralAction = new NeutralAction(rotState, extState, elbowState, wristState);
        toggleHooks = new ToggleHooks(hookState);
        preHangPose = new SetRotPose(rotState, ArmRotPose.PRE_HANG);
        hangPose = new SetRotPose(rotState, ArmRotPose.INTAKE);

        /*  ===============
            Button Bindings
            ================ */

        // ----- Controller 1 -----
        // Basket pose
        gw1.bindButton(GamepadButton.LEFT_TRIGGER, basketPose);
        // Intake pose
        gw1.bindButton(GamepadButton.RIGHT_TRIGGER, intakePose);
        // Cancel button
        gw1.bindButton(GamepadButton.B, neutralAction);

        // ----- Controller 2 -----
        gw2.bindButton(GamepadButton.B, toggleHooks);
        gw2.bindButton(GamepadButton.Y, preHangPose);
        gw2.bindButton(GamepadButton.A, hangPose);
    }

    @Override
    public void loop() {
        /*  ===================
            General Maintenance
            =================== */
        super.loop();
        extState.setSpeed(DebugConstants.extSpeed);
        rotState.setSpeed(DebugConstants.rotSpeed);

        /*  ============
            Controller 1
            ============ */

        // Make basket and intake poses mutually exclusive
        if (!basketPose.isFinished()) {
            intakePose.cancel();
        } else if (!intakePose.isFinished()) {
            basketPose.cancel();
        }

        // ----- Drivetrain -----
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        /*  ============
            Controller 2
            ============ */
        // Rotation of wrist
        wristState.setRot(wristState.getRot() + gamepad2.right_stick_x * wristRotateSpeed);

        // Extension of slides
        extState.setPower(gamepad1.left_stick_y);


        /*  ================
            Both Controllers
            ================ */

        // ----- Hanging -----

        if (matchTimer.seconds() >= 90 && !rumbledEndGame) {
            gamepad1.rumble(500);
            gamepad2.rumble(500);
            rumbledEndGame = true;
        } else if (matchTimer.seconds() >= 120 && !rumbledMatchEnd) {
            gamepad1.rumbleBlips(3);
            gamepad2.rumbleBlips(3);
            rumbledMatchEnd = true;
        }
    }
}
