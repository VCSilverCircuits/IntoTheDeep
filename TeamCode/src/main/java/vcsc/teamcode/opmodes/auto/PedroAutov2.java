package vcsc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.teamcode.actions.BasketPose;
import vcsc.teamcode.actions.DownFromBasket;
import vcsc.teamcode.actions.Grab;
import vcsc.teamcode.actions.IntakePose;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.PreGrabPoseAuto;
import vcsc.teamcode.actions.ToggleBasket;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.opmodes.base.BaseOpModeAuto;

@Disabled
@Autonomous(name = "PedroAutoV2", group = "Testing")
public class PedroAutov2 extends BaseOpModeAuto {
    private final Pose startPose = new Pose(6, 113, Math.toRadians(270));  // Starting position
    private final Pose scorePose = new Pose(14, 129, Math.toRadians(315)); // Scoring position

    private final Pose pickup1Pose = new Pose(24, 122, Math.toRadians(0)); // First sample pickup
    private final Pose pickup2Pose = new Pose(24, 131, Math.toRadians(0)); // Second sample pickup
    private final Pose pickup3Pose = new Pose(25, 132.5, Math.toRadians(22)); // Third sample pickup

    private final Pose parkPose = new Pose(60, 98, Math.toRadians(90));    // Parking position
    private final Pose parkControlPose = new Pose(65, 125, Math.toRadians(90)); // Control point for curved path
    BasketPose basketPose;
    DownFromBasket downFromBasket;
    ToggleBasket toggleBasket;
    PreGrabPoseAuto preGrabPose;
    IntakePose intakePose;
    Grab grab;
    NeutralAction neutralAction;

    private Path scorePreload, park;
    private PathChain grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    public void buildPaths() {
        // Path for scoring preload
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        // Path chains for picking up and scoring samples
        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .build();

        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();

        // Curved path for parking
        park = new Path(new BezierCurve(new Point(scorePose), new Point(parkControlPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());
    }

    public void autonomousPathUpdate() {
        double grabDelay = 2000; // 4
        double neutralDelay = 250; // 4
        double scoreDelay = 800; // 4
        double basketDelayUp = 2800; //4
        double basketDelayDown = 3000; // 4


        switch (pathState) {
            case 0: // Move from start to scoring position
                follower.followPath(scorePreload);
                setPathState(1);
                elbowState.setPose(ElbowPose.STOW);
                break;
            /*
                   X: 6   --> 14
                   Y: 113 --> 129
             */
            case 1: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1) && pathTimer.getElapsedTime() > basketDelayUp) {
                    toggleBasket.start();
                    setPathState(2);
                }
                break;
            case 2:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    toggleBasket.start();
                    follower.followPath(grabPickup1, true);
                    setPathState(3);
                }
                break;
//
            case 3: // Wait until the robot is near the first sample pickup position
                if (follower.getPose().getX() > (pickup1Pose.getX() - 1) && follower.getPose().getY() < (pickup1Pose.getY() + 1)) {
                    intakePose.start();
                    setPathState(4);
                }
                break;
            case 4: // Grab
                if (intakePose.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
                    grab.start();
                    setPathState(5);
                }
                break;
            case 5: // Neutral
                if (grab.isFinished() && pathTimer.getElapsedTime() > neutralDelay) {
                    neutralAction.start();
                    setPathState(6);
                }
                break;
            case 6: // Score
                if (neutralAction.isFinished() && pathTimer.getElapsedTime() > scoreDelay) {
                    follower.followPath(scorePickup1);
                    setPathState(7);
                }
                break;
            case 7: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() < (scorePose.getX() + 1) && follower.getPose().getY() > (scorePose.getY() - 1) && pathTimer.getElapsedTime() > basketDelayUp) {
                    toggleBasket.start();
                    setPathState(8);
                }
                break;
            case 8:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    toggleBasket.start();
                    follower.followPath(grabPickup2, true);
                    setPathState(9);
                }
                break;
            case 9: // Wait until the robot is near the first sample pickup position
                if (follower.getPose().getX() > (pickup2Pose.getX() - 1) && follower.getPose().getY() > (pickup2Pose.getY() - 1)) {
                    intakePose.start();
                    setPathState(10);
                }
                break;
            case 10:
                if (intakePose.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
                    grab.start();
                    setPathState(11);
                }
                break;
            case 11:
                if (grab.isFinished() && pathTimer.getElapsedTime() > neutralDelay) {
                    neutralAction.start();
                    setPathState(12);
                }
                break;
            case 12:
                if (neutralAction.isFinished() && pathTimer.getElapsedTime() > scoreDelay) {
                    follower.followPath(scorePickup2);
                    setPathState(13);
                }
                break;
            case 13: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() < (scorePose.getX() + 1) && follower.getPose().getY() < (scorePose.getY() + 1) && pathTimer.getElapsedTime() > basketDelayUp) {
                    toggleBasket.start();
                    setPathState(14);
                }
                break;
            case 14:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    toggleBasket.start();
                    follower.followPath(grabPickup3, true);
                    setPathState(15);
                }
                break;
            case 15: // Wait until the robot is near the first sample pickup position
                if (follower.getPose().getX() > (pickup3Pose.getX() - 1) && follower.getPose().getY() > (pickup3Pose.getY() - 1)) {
                    intakePose.start();
                    setPathState(16);
                }
                break;
            case 16:
                if (intakePose.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
                    grab.start();
                    setPathState(17);
                }
                break;
            case 17:
                if (grab.isFinished() && pathTimer.getElapsedTime() > neutralDelay) {
                    neutralAction.start();
                    setPathState(18);
                }
                break;
            case 18:
                if (neutralAction.isFinished() && pathTimer.getElapsedTime() > scoreDelay) {
                    follower.followPath(scorePickup3);
                    setPathState(19);
                }
                break;
            case 19: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() < (scorePose.getX() + 1) && follower.getPose().getY() < (scorePose.getY() + 1) && pathTimer.getElapsedTime() > basketDelayUp) {
                    toggleBasket.start();
                    setPathState(20);
                }
                break;
            case 20:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    toggleBasket.start();
                    follower.followPath(park);
                    setPathState(21);
                }
                break;
            case 21: // Wait until the robot is near the parking position
                if (follower.getPose().getX() > (parkPose.getX() - 1) && follower.getPose().getY() < (parkPose.getY() + 1)) {
                    setPathState(-1); // End the autonomous routine
                }
                break;
        }
    }

    /**
     * These change the states of the paths and actions
     * It will also reset the timers of the individual switches
     **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {
        super.loop();

//        basketPose.loop();
//        downFromBasket.loop();
        intakePose.loop();
        toggleBasket.loop();
        grab.loop();
        neutralAction.loop();

        // These loop the movements of the robot
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub
        telem.addData("path state", pathState);
        telem.addData("x", follower.getPose().getX());
        telem.addData("y", follower.getPose().getY());
        telem.addData("heading", follower.getPose().getHeading());
        telem.addData("timer", pathTimer.getElapsedTime());
    }

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        super.init();
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        basketPose = new BasketPose(rotState, extState, elbowState, wristState);
        downFromBasket = new DownFromBasket(rotState, extState, elbowState, wristState);
        toggleBasket = new ToggleBasket(extState, clawState, basketPose, downFromBasket);
        preGrabPose = new PreGrabPoseAuto(elbowState, wristState, clawState);
        intakePose = new IntakePose(rotState, extState, clawState, preGrabPose);
        grab = new Grab(elbowState, wristState, clawState);
        neutralAction = new NeutralAction(rotState, extState, elbowState, wristState);

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void init_loop() {
        super.init_loop();
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void start() {
        super.start();
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /**
     * We do not use this because everything should automatically disable
     **/
    @Override
    public void stop() {
        super.stop();
    }

}
