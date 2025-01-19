package vcsc.teamcode.opmodes.tele;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import vcsc.core.util.GamepadButton;
import vcsc.teamcode.DebugConstants;
import vcsc.teamcode.actions.Cancel;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.NeutralActionSpecimen;
import vcsc.teamcode.actions.ToggleBasket;
import vcsc.teamcode.actions.basket.BasketPose;
import vcsc.teamcode.actions.basket.DownFromBasket;
import vcsc.teamcode.actions.basket.LowerBasketPose;
import vcsc.teamcode.actions.hang.PreHang;
import vcsc.teamcode.actions.intake.Grab;
import vcsc.teamcode.actions.intake.GrabWall;
import vcsc.teamcode.actions.intake.IntakePose;
import vcsc.teamcode.actions.intake.IntakePoseWall;
import vcsc.teamcode.actions.intake.PreGrabPose;
import vcsc.teamcode.actions.intake.PreGrabPoseWall;
import vcsc.teamcode.actions.intake.WallActions;
import vcsc.teamcode.actions.specimen.ScoreSpecimen;
import vcsc.teamcode.actions.specimen.SpecimenActions;
import vcsc.teamcode.actions.specimen.SpecimenPose;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.actions.SetRotPose;
import vcsc.teamcode.component.hooks.actions.ToggleHooks;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Tele", group = "Main")
public class MainTele extends BaseOpMode {
    boolean rumbledEndGame = false, rumbledMatchEnd = false;
    BasketPose basketPose;
    LowerBasketPose lowerBasketPose;
    IntakePose intakePose;
    NeutralAction neutralAction;
    NeutralActionSpecimen neutralActionSpecimen;
    ToggleHooks toggleHooks;
    PreHang preHangPose;
    SetRotPose hangPose;
    PreGrabPose preGrabPose;
    ToggleBasket toggleBasket;
    DownFromBasket downFromBasket;
    SpecimenPose specimenPose;
    ScoreSpecimen scoreSpecimen;
    SpecimenActions specimenActions;
    IntakePoseWall intakePoseWall;
    GrabWall grabWall;
    WallActions wallActions;
    Cancel cancel;
    Grab grab;
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
        specimenPose = new SpecimenPose(rotState, extState, elbowState, wristState);
        neutralActionSpecimen = new NeutralActionSpecimen(rotState, extState, elbowState, wristState);
        scoreSpecimen = new ScoreSpecimen(extState, clawState, neutralActionSpecimen);
        specimenActions = new SpecimenActions(extState, clawState, specimenPose, scoreSpecimen);
        intakePoseWall = new IntakePoseWall(rotState, extState, clawState, new PreGrabPoseWall(elbowState, wristState, clawState));
        grabWall = new GrabWall(elbowState, wristState, clawState);
        wallActions = new WallActions(elbowState, clawState, intakePoseWall, grabWall);
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
        gw1.bindButton(GamepadButton.LEFT_TRIGGER, toggleBasket);
        gw1.bindRunnable(GamepadButton.LEFT_TRIGGER, () -> {
//            lowerBasketPose.cancel();
            intakePose.cancel();
            neutralAction.cancel();
            telemetry.addLine("Cancelling intake and neutral");
        });
        // Lower Basket Pose
        /*gw1.bindButton(GamepadButton.LEFT_BUMPER, lowerBasketPose);
        gw1.bindRunnable(GamepadButton.LEFT_BUMPER, () -> {
            basketPose.cancel();
            intakePose.cancel();
            neutralAction.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });*/
        // Intake pose
        gw1.bindButton(GamepadButton.RIGHT_TRIGGER, intakePose);
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
            specimenActions.cancel();
            wallActions.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });

        // Specimens
        /*gw1.bindButton(GamepadButton.LEFT_BUMPER, intakePoseWall);
        gw1.bindRunnable(GamepadButton.LEFT_BUMPER, () -> {
            basketPose.cancel();
            intakePose.cancel();
            neutralAction.cancel();
            grabWall.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });
        gw1.bindButton(GamepadButton.RIGHT_BUMPER, grabWall);
        gw1.bindRunnable(GamepadButton.RIGHT_BUMPER, () -> {
            basketPose.cancel();
            intakePose.cancel();
            neutralAction.cancel();
            intakePoseWall.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });*/

        gw1.bindButton(GamepadButton.RIGHT_BUMPER, wallActions);
        gw1.bindRunnable(GamepadButton.RIGHT_BUMPER, () -> {
            basketPose.cancel();
            intakePose.cancel();
            specimenActions.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });

        gw1.bindButton(GamepadButton.LEFT_BUMPER, specimenActions);
        gw1.bindRunnable(GamepadButton.LEFT_BUMPER, () -> {
            basketPose.cancel();
            intakePose.cancel();
            wallActions.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });
        /*gw1.bindButton(GamepadButton.RIGHT_BUMPER, scoreSpecimen);
        gw1.bindRunnable(GamepadButton.RIGHT_BUMPER, () -> {
            basketPose.cancel();
            intakePose.cancel();
            neutralActionSpecimen.cancel();
            specimenPose.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });*/


        // ----- Controller 2 -----
        gw2.bindButton(GamepadButton.B, toggleHooks);
        gw2.bindButton(GamepadButton.Y, preHangPose);
        gw2.bindButton(GamepadButton.A, hangPose);
        gw2.bindButton(GamepadButton.RIGHT_BUMPER, preGrabPose);
        gw2.bindButton(GamepadButton.LEFT_BUMPER, grab);
        gw2.bindButton(GamepadButton.RIGHT_TRIGGER, neutralAction);

        follower.setStartingPose(new Pose(0, 0, 0));
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
        if (extState.getTargetPosition() > 10 && extState.getPose() != ArmExtPose.SPECIMEN_PRE_SCORE || elbowState.getPose() == ElbowPose.WALL) {
            driveSpeed = 0.25;
        } else {
            driveSpeed = 1;
        }

//        elbowState.setPosition(DebugConstants.elbow);

        //follower.setTeleOpMovementVectors(-gamepad1.left_stick_y * driveSpeed, -gamepad1.left_stick_x * driveSpeed, -gamepad1.right_stick_x * driveSpeed);
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y * driveSpeed,
                        -gamepad1.left_stick_x * driveSpeed
                ),
                -gamepad1.right_stick_x * driveSpeed
        ));

        /*  ============
            Controller 2
            ============ */
        if (rotState.getPose() == ArmRotPose.INTAKE) {
            // Rotation of wrist
            double newPivot = wristState.getPivot() + gamepad2.right_stick_x * wristRotateSpeed;
            newPivot = Math.min(Math.max(newPivot, WristPivotPose.MIN.getPosition()), WristPivotPose.MAX.getPosition());
            wristState.setPivot(newPivot);

            // Extension of slides (with max and min limits)

            // WARNING: Don't make too big without checking min position.
            // This is also used for emergency reset of slides.
            double extPower = -gamepad2.left_stick_y / 4;
            // If slides exceed the max and you're trying to go further then prevent adjusting length
            if (extPower > 0 && extState.getRealPosition() >= ArmExtPose.INTAKE.getLength()) {
                extPower = 0;
                // TODO: Add optional override
            }
            // If slides exceed the min and you're trying to go further then prevent adjusting length
//            else if (extPower < 0 && extState.getRealPosition() <= 0) {
//                extPower = 0;
//            }
            extState.setPower(extPower);

            if (gamepad2.dpad_left) {
                rotState.setPower(-0.2);
            } else {
                rotState.setPower(0);
            }

            telem.addData("Current", extState.getCurrent());


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
}


