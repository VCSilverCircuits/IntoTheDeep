package vcsc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.teamcode.actions.BasketPose;
import vcsc.teamcode.actions.DownFromBasket;
import vcsc.teamcode.actions.Grab;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.PreGrabPose;
import vcsc.teamcode.actions.ToggleBasket;
import vcsc.teamcode.opmodes.base.BaseLinearOpMode;

@Disabled
@Autonomous(group = "Main", name = "MainAuto", preselectTeleOp = "MainTele")
public class MainAuto extends BaseLinearOpMode {
    @Override
    public void runOpMode() {
        super.runOpMode();
        BasketPose basketPose = new BasketPose(rotState, extState, elbowState, wristState);
        DownFromBasket downFromBasket = new DownFromBasket(rotState, extState, elbowState, wristState);
        ToggleBasket toggleBasket = new ToggleBasket(extState, clawState, basketPose, downFromBasket);
        PreGrabPose preGrab = new PreGrabPose(elbowState, wristState, clawState);
        Grab grab = new Grab(elbowState, wristState, clawState);
        NeutralAction neutralAction = new NeutralAction(rotState, extState, elbowState, wristState);

        //Actions
        //TODO: Need to change to a spline movement for more consistent scoring and better transition to other parts of the auto
        Action reverse = drive.actionBuilder(new Pose2d(0, 0, 0))
                .lineToX(-7.9).build();
        Action strafe = drive.actionBuilder(new Pose2d(-7.9, 0, 0))
                .strafeToSplineHeading(new Vector2d(-11.5, 18), Math.toRadians(65)).build();
        Action reverse2 = drive.actionBuilder(new Pose2d(0, 0, 0)).lineToX(-8).build();
        Action tinyTurn = drive.actionBuilder(new Pose2d(0, 0, 0)).turn(Math.toRadians(-38)).build();
        //.strafeTo(new Vector2d(-7.9, 10)).turn(Math.PI / 2).build();
//        Action sample1 = drive.actionBuilder(new Pose2d(-7.9, 0, 0))
//                .splineTo(new Vector2d(-7.9, 7), Math.toRadians(70)).build();
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        waitForStart();
        matchTimer.reset();

        // Go to basket pose
        basketPose.start();
        while (!basketPose.isFinished() || timer.time() < 3000) {
            basketPose.loop();
            updateComponents();
        }
        timer.reset();

        while (timer.time() < 2000) {
            basketPose.loop();
            updateComponents();
        }
        timer.reset();
        // Drive backwards
        Actions.runBlocking(reverse);

        while (timer.time() < 1500) {
            // Stall
            updateComponents();
        }

        timer.reset();
        // Drop sample and retract
        toggleBasket.start();
        while (!toggleBasket.isFinished() || timer.time() < 3000 && opModeIsActive()) {
            toggleBasket.loop();
            updateComponents();
        }
        preGrab.start();

        timer.reset();
        while (!preGrab.isFinished() || timer.time() < 1000 && opModeIsActive()) {
            preGrab.loop();
            updateComponents();
        }

        Actions.runBlocking(strafe);


        grab.start();
        timer.reset();

        while (!grab.isFinished() || timer.time() < 3000 && opModeIsActive()) {
            grab.loop();
            updateComponents();
        }


        neutralAction.start();

        timer.reset();

        while (!neutralAction.isFinished() || timer.time() < 3000 && opModeIsActive()) {
            neutralAction.loop();
            updateComponents();
        }

        Actions.runBlocking(reverse2);

        basketPose.start();
        while (!basketPose.isFinished() || timer.time() < 3000) {
            basketPose.loop();
            updateComponents();
        }

        timer.reset();

        while (timer.time() < 2000) {
            basketPose.loop();
            updateComponents();
        }

        Actions.runBlocking(tinyTurn);

        timer.reset();

        // Drop sample and retract
        toggleBasket.start();
        while (!toggleBasket.isFinished() || timer.time() < 3000 && opModeIsActive()) {
            toggleBasket.loop();
            updateComponents();
        }


        //Actions.runBlocking(sample1);

    }
}
