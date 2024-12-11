package vcsc.teamcode.opmodes.tele;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import vcsc.core.util.GamepadButton;
import vcsc.teamcode.DebugConstants;
import vcsc.teamcode.actions.BasketPose;
import vcsc.teamcode.actions.Cancel;
import vcsc.teamcode.actions.DownFromBasket;
import vcsc.teamcode.actions.Grab;
import vcsc.teamcode.actions.IntakePose;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.PreGrabPose;
import vcsc.teamcode.actions.SetRotPose;
import vcsc.teamcode.actions.ToggleBasket;
import vcsc.teamcode.actions.ToggleHooks;
import vcsc.teamcode.actions.hang.PreHang;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "MainTele", group = "Main")
public class MainTele extends BaseOpMode {
    boolean rumbledEndGame = false, rumbledMatchEnd = false;
    private Limelight3A limelight;

    BasketPose basketPose;
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
    double wristRotateSpeed = 0.03;

    double driveSpeed = 1;

    @Override
    public void init() {
        super.init();
        // ===== Actions =====
        preGrabPose = new PreGrabPose(elbowState, wristState, clawState);
        basketPose = new BasketPose(rotState, extState, elbowState, wristState);
        intakePose = new IntakePose(rotState, extState, clawState, preGrabPose);
        neutralAction = new NeutralAction(rotState, extState, elbowState, wristState);
        downFromBasket = new DownFromBasket(rotState, extState, elbowState, wristState);
        toggleHooks = new ToggleHooks(hookState);
        toggleBasket = new ToggleBasket(extState, clawState, basketPose, downFromBasket);
        preHangPose = new PreHang(extState, rotState, hookState, elbowState); //new SetRotPose(rotState, ArmRotPose.PRE_HANG);
        hangPose = new SetRotPose(rotState, ArmRotPose.HANG);
        grab = new Grab(elbowState, wristState, clawState);
        cancel = new Cancel(rotState, neutralAction, downFromBasket);
        //limelight initialization
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();

        /*  ===============
            Button Bindings
            ================ */

        // ----- Controller 1 -----
        // Basket pose
        gw1.bindButton(GamepadButton.LEFT_TRIGGER, toggleBasket);
        gw1.bindRunnable(GamepadButton.LEFT_TRIGGER, () -> {
            intakePose.cancel();
            telemetry.addLine("Cancelling intake and neutral");
        });
        // Intake pose
        gw1.bindButton(GamepadButton.RIGHT_TRIGGER, intakePose);
        gw1.bindRunnable(GamepadButton.RIGHT_TRIGGER, () -> {
            basketPose.cancel();
            neutralAction.cancel();
            telemetry.addLine("Cancelling basket and neutral");
        });
        // Cancel button
        gw1.bindButton(GamepadButton.B, cancel);
        gw1.bindRunnable(GamepadButton.B, () -> {
            basketPose.cancel();
            intakePose.cancel();
            telemetry.addLine("Cancelling basket and intake");
        });


        // ----- Controller 2 -----
        gw2.bindButton(GamepadButton.B, toggleHooks);
        gw2.bindButton(GamepadButton.Y, preHangPose);
        gw2.bindButton(GamepadButton.A, hangPose);
        gw2.bindButton(GamepadButton.RIGHT_BUMPER, preGrabPose);
        gw2.bindButton(GamepadButton.LEFT_BUMPER, grab);
        gw2.bindButton(GamepadButton.RIGHT_TRIGGER, neutralAction);
    }

    @Override
    public void start() {
        super.start();
        wristState.setPose(WristPose.STOW);
        elbowState.setPose(ElbowPose.STOW);

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

        /*  ============
            Controller 2
            ============ */
        if (rotState.getPose() == ArmRotPose.INTAKE) {
            // Rotation of wrist
            double newPivot = wristState.getPivot() + gamepad2.right_stick_x * wristRotateSpeed;
            newPivot = Math.min(Math.max(newPivot, WristPivotPose.FORWARD.getPosition()), WristPivotPose.REVERSE.getPosition());
            wristState.setPivot(newPivot);

            // Extension of slides (with max and min limits)

            double extPower = -gamepad2.left_stick_y;
            // If slides exceed the max and you're trying to go further then prevent adjusting length
            if (extPower > 0 && extState.getRealPosition() >= ArmExtPose.INTAKE.getLength()) {
                extPower = 0;

            }
            // If slides exceed the min and you're trying to go further then prevent adjusting length
            else if (extPower < 0 && extState.getRealPosition() <= 0) {
                extPower = 0;
            }
            extState.setPower(extPower);


            /* --- Alternative Solution */
            // If slides exceed the max or min and you're trying to go further then prevent adjusting length
            /*if (!(-gamepad2.left_stick_y > 0 && extState.getRealPosition() >= ArmExtPose.INTAKE.getLength())
                    && !(-gamepad2.left_stick_y < 0 && extState.getRealPosition() <= 0)) {
                extState.setPower(-gamepad2.left_stick_y);*/
            // TODO: fix opmodeIsActive so it can pull results
           /* while (opModeIsActive()) {
                LLResult result = limelight.getLatestResult();
                if (result != null) {
                    if (result.isValid()) {
                        Pose3D botpose = result.getBotpose();
                        telemetry.addData("tx", result.getTx());
                        telemetry.addData("ty", result.getTy());
                        telemetry.addData("Botpose", botpose.toString());


                    }*\

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


