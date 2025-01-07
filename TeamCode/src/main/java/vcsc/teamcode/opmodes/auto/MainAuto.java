package vcsc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.teamcode.actions.BasketPose;
import vcsc.teamcode.actions.DownFromBasket;
import vcsc.teamcode.actions.ToggleBasket;
import vcsc.teamcode.opmodes.base.BaseLinearOpMode;

@Autonomous(group = "Main", name = "MainAuto", preselectTeleOp = "MainTele")
public class MainAuto extends BaseLinearOpMode {
    @Override
    public void runOpMode() {
        super.runOpMode();
        BasketPose basketPose = new BasketPose(rotState, extState, elbowState, wristState);
        DownFromBasket downFromBasket = new DownFromBasket(rotState, extState, elbowState, wristState);
        ToggleBasket toggleBasket = new ToggleBasket(extState, clawState, basketPose, downFromBasket);
        //Actions
        //TODO: Need to change to a spline movement for more consistent scoring and better transition to other parts of the auto
        Action reverse = drive.actionBuilder(new Pose2d(0, 0, 0))
                .lineToX(-7.9).build();
        Action sample1 = drive.actionBuilder(new Pose2d(-7.9,0,0))
                .splineTo(new Vector2d(-7.9,7), Math.PI / 4).build();
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
        while (!toggleBasket.isFinished() || timer.time() < 10000 && opModeIsActive()) {
            toggleBasket.loop();
            updateComponents();
        }
        Actions.runBlocking(sample1);

    }
}
