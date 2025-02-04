package vcsc.teamcode.opmodes.tele;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.core.util.GamepadButton;
import vcsc.teamcode.DebugConstants;
import vcsc.teamcode.actions.Cancel;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.ToggleBasket;
import vcsc.teamcode.actions.basket.BasketPose;
import vcsc.teamcode.actions.basket.DownFromBasket;
import vcsc.teamcode.actions.basket.LowerBasketPose;
import vcsc.teamcode.actions.basket.WristSpecimenPose;
import vcsc.teamcode.actions.hang.PreHang;
import vcsc.teamcode.actions.intake.Grab;
import vcsc.teamcode.actions.intake.IntakePose;
import vcsc.teamcode.actions.intake.IntakePoseWall;
import vcsc.teamcode.actions.intake.PreGrabPose;
import vcsc.teamcode.actions.intake.PreGrabPoseWall;
import vcsc.teamcode.actions.specimen.SpecimenPose;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.actions.SetRotPose;
import vcsc.teamcode.component.hooks.actions.ToggleHooks;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@Disabled
@TeleOp(name = "Tele", group = "Test")
public class TestTele extends BaseOpMode {
    boolean rumbledEndGame = false, rumbledMatchEnd = false;
    BasketPose basketPose;
    LowerBasketPose lowerBasketPose;
    IntakePose intakePose;
    NeutralAction neutralAction;
    ToggleHooks toggleHooks;
    PreHang preHangPose;
    SetRotPose hangPose;
    PreGrabPose preGrabPose;
    ToggleBasket toggleBasket;
    DownFromBasket downFromBasket;
    Cancel cancel;
    Grab grab;
    PreGrabPoseWall preGrabPoseWall;
    WristSpecimenPose wristSpecimenPose;
    SpecimenPose specimenPose;
    IntakePoseWall intakePoseWall;
    double wristRotateSpeed = 0.03;
    double driveSpeed = 1;
//    private Limelight3A limelight;

    @Override
    public void init() {
        super.init();
        // ===== Actions =====
        preGrabPose = new PreGrabPose(elbowState, wristState, clawState);
        basketPose = new BasketPose(rotState, extState, elbowState, wristState);
//        lowerBasketPose = new LowerBasketPose(rotState, elbowState, wristState);
        downFromBasket = new DownFromBasket(rotState, extState, elbowState, wristState);
        toggleBasket = new ToggleBasket(extState, clawState, basketPose, downFromBasket);
        intakePose = new IntakePose(rotState, extState, clawState, preGrabPose);
        neutralAction = new NeutralAction(rotState, extState, elbowState, wristState);
        toggleHooks = new ToggleHooks(hookState);
        preHangPose = new PreHang(extState, rotState, hookState, elbowState); //new SetRotPose(rotState, ArmRotPose.PRE_HANG);
        hangPose = new SetRotPose(rotState, ArmRotPose.HANG);
        grab = new Grab(elbowState, wristState, clawState);
        cancel = new Cancel(rotState, neutralAction, downFromBasket);
        preGrabPoseWall = new PreGrabPoseWall(elbowState, wristState, clawState);
        wristSpecimenPose = new WristSpecimenPose(elbowState, wristState);
        specimenPose = new SpecimenPose(rotState, extState, elbowState, wristState);
        intakePoseWall = new IntakePoseWall(rotState, extState, clawState, preGrabPoseWall);
        //limelight initialization
//        limelight = hardwareMap.get(Limelight3A.class, "limelight");
//        telemetry.setMsTransmissionInterval(11);
//        limelight.pipelineSwitch(0);
//        limelight.start();

        /*  ===============
            Button Bindings
            ================ */

        // ----- Controller 1 -----
        // Basket pose
        gw1.bindButton(GamepadButton.LEFT_TRIGGER, preGrabPoseWall);
        gw1.bindRunnable(GamepadButton.LEFT_TRIGGER, () -> {
//            lowerBasketPose.cancel();
            telemetry.addLine("Cancelling intake and neutral");
        });
        gw1.bindButton(GamepadButton.RIGHT_BUMPER, specimenPose);
        gw1.bindRunnable(GamepadButton.LEFT_TRIGGER, () -> {
//            lowerBasketPose.cancel();
            telemetry.addLine("Cancelling intake and neutral");
        });
        gw1.bindButton(GamepadButton.LEFT_BUMPER, intakePoseWall);
        gw1.bindRunnable(GamepadButton.LEFT_TRIGGER, () -> {
//            lowerBasketPose.cancel();
            telemetry.addLine("Cancelling intake and neutral");
        });
        gw1.bindButton(GamepadButton.RIGHT_TRIGGER, wristSpecimenPose);
        gw1.bindRunnable(GamepadButton.RIGHT_TRIGGER, () -> {
            if (extState.getPose() == ArmExtPose.BASKET) {
                return;
            }
            basketPose.cancel();
//            lowerBasketPose.cancel();
            neutralAction.cancel();
            telemetry.addLine("Cancelling basket and neutral");
        });
        // Cancel button
        gw1.bindButton(GamepadButton.B, cancel);
        gw1.bindRunnable(GamepadButton.B, () -> {
            basketPose.cancel();
//            lowerBasketPose.cancel();
            intakePose.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });
    }

    @Override
    public void start() {
        super.start();

    }

    @Override
    public void loop() {
        /*  ===================
            General Maintenance
            =================== */
        super.loop();
        extState.setSpeed(DebugConstants.extSpeed);
        rotState.setSpeed(DebugConstants.rotSpeed);
        telem.addData("ElbowRot", elbowState.getPosition());
        telem.addData("WristRot", wristState.getRot());
        telem.addData("WristPivot", wristState.getPivot());
        telem.addData("LeftHook", hookState.getPositionLeft());
        telem.addData("RightHook", hookState.getPositionRight());

        /*  ============
            Controller 1
            ============ */


        // ----- Drivetrain -----

        // Slow down driving if the slides are extended
        if (extState.getTargetPosition() > 10) {
            driveSpeed = 0.25;
        } else {
            driveSpeed = 1;
        }

//        elbowState.setPosition(DebugConstants.elbow);

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y * driveSpeed,
                        -gamepad1.left_stick_x * driveSpeed
                ),
                -gamepad1.right_stick_x * driveSpeed
        ));






        /*  ================
            Both Controllers
            ================ */

        // ----- Rumbling -----
        if (matchTimer.seconds() >= 90 && !rumbledEndGame) { // End game rumble
            gamepad1.rumble(500);
            gamepad2.rumble(500);
            rumbledEndGame = true;
        } else if (matchTimer.seconds() >= 120 && !rumbledMatchEnd) { // Match end 3 rumbles
            gamepad1.rumbleBlips(3);
            gamepad2.rumbleBlips(3);
            rumbledMatchEnd = true;
        }
    }
}


