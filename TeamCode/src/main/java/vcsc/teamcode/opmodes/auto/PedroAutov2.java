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

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.teamcode.actions.LockOn;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.ToggleBasketAuto;
import vcsc.teamcode.actions.basket.BasketPoseAuto;
import vcsc.teamcode.actions.basket.DownFromBasket;
import vcsc.teamcode.actions.intake.GrabAuto;
import vcsc.teamcode.actions.intake.GrabAutoWall;
import vcsc.teamcode.actions.intake.GrabSubmersible;
import vcsc.teamcode.actions.intake.IntakePoseAuto;
import vcsc.teamcode.actions.intake.PreGrabPose;
import vcsc.teamcode.actions.intake.PreGrabPoseAuto;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.opmodes.base.BaseOpModeAuto;

@Autonomous(name = "SAMPLE (Basket) Auto", group = "Testing", preselectTeleOp = "Tele")
public class PedroAutov2 extends BaseOpModeAuto {
    private final Pose startPose = new Pose(6, 113, Math.toRadians(270));  // Starting position
    private final Pose scorePose = new Pose(14, 129, Math.toRadians(315)); // Scoring position

    private final Pose pickup1Pose = new Pose(24.5, 122.8, Math.toRadians(0)); // First sample pickup
    private final Pose pickup2Pose = new Pose(24.5, 131.5, Math.toRadians(0)); // Second sample pickup
    private final Pose pickup3Pose = new Pose(26, 132.3, Math.toRadians(26)); // Third sample pickup

    private final Pose parkPose = new Pose(60, 102, Math.toRadians(260));    // Parking position
    private final Pose parkControlPose = new Pose(65, 130, Math.toRadians(90)); // Control point for curved path
    BasketPoseAuto basketPose;
    DownFromBasket downFromBasket;
    ToggleBasketAuto toggleBasket;
    PreGrabPose preGrabPose;
    IntakePoseAuto intakePoseAuto;
    GrabAuto grab;
    GrabSubmersible grabSub;
    GrabAutoWall grabWall;
    NeutralAction neutralAction;
    LockOn lockOn;

    private Path scorePreload, park;
    private PathChain grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3, scoreSubmersible;
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    public void buildPaths() {
        // Path for scoring preload
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());
        scorePreload.setPathEndVelocityConstraint(0.3);

        // Path chains for picking up and scoring samples
        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        // Curved path for parking
        park = new Path(new BezierCurve(new Point(scorePose), new Point(parkControlPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());
    }

    public void autonomousPathUpdate() {
        double grabDelay = 1500;
        double neutralDelay = 250;
        double scoreDelay = 300;
        double basketDelayUp = 0;//2800;
        double basketDelayDown = 800;
        double lockOnFailsafe = 4000;
        switch (pathState) {
            case 0: // Move from start to scoring position
                elbowState.setPose(ElbowPose.STOW);
                follower.followPath(scorePreload, true);
                pathTimer.resetTimer();
                setPathState(1);
                break;
            case 1: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() > (scorePose.getX() - 8) && follower.getPose().getY() > (scorePose.getY() - 8)) {
                    toggleBasket.start();
                    pathTimer.resetTimer();
                    setPathState(2);
                }
                break;
            case 2: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1) && !follower.isBusy()) {
                    pathTimer.resetTimer();
                    setPathState(3);
                }
                break;
            case 3:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > basketDelayDown + 500) {
                    toggleBasket.start();
                    follower.followPath(grabPickup1, true);
                    setPathState(4);
                }
                break;
//
            case 4: // Wait until the robot is near the first sample pickup position
                if (follower.getPose().getX() > (pickup1Pose.getX() - 1) && follower.getPose().getY() < (pickup1Pose.getY() + 1)) {
                    intakePoseAuto.start();
                    setPathState(5);
                }
                break;
            case 5: // Grab
                if (intakePoseAuto.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
                    grab.start();
                    setPathState(6);
                }
                break;
            case 6: // Neutral
                if (grab.isFinished() && pathTimer.getElapsedTime() > neutralDelay) {
                    neutralAction.start();
                    setPathState(7);
                }
                break;
            case 7: // Score
                if (neutralAction.isFinished() && pathTimer.getElapsedTime() > scoreDelay) {
                    toggleBasket.start();
                    follower.followPath(scorePickup1, true);
                    setPathState(8);
                }
                break;
            case 8: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() < (scorePose.getX() + 1) && follower.getPose().getY() > (scorePose.getY() - 1) && pathTimer.getElapsedTime() > basketDelayUp && !follower.isBusy()) {
                    setPathState(9);
                }
                break;
            case 9:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    toggleBasket.start();
                    follower.followPath(grabPickup2, true);
                    setPathState(10);
                }
                break;
            case 10: // Wait until the robot is near the second sample pickup position
                if (follower.getPose().getX() > (pickup2Pose.getX() - 1) && follower.getPose().getY() > (pickup2Pose.getY() - 1)) {
                    intakePoseAuto.start();
                    setPathState(11);
                }
                break;
            case 11:
                if (intakePoseAuto.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
                    grab.start();
                    setPathState(12);
                }
                break;
            case 12:
                if (grab.isFinished() && pathTimer.getElapsedTime() > neutralDelay) {
                    neutralAction.start();
                    setPathState(13);
                }
                break;
            case 13:
                if (neutralAction.isFinished() && pathTimer.getElapsedTime() > scoreDelay) {
                    toggleBasket.start();
                    follower.followPath(scorePickup2, true);
                    setPathState(14);
                }
                break;
            case 14: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() < (scorePose.getX() + 1) && follower.getPose().getY() < (scorePose.getY() + 1) && pathTimer.getElapsedTime() > basketDelayUp && !follower.isBusy()) {
                    setPathState(15);
                }
                break;
            case 15:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    toggleBasket.start();
                    follower.followPath(grabPickup3, true);
                    setPathState(16);
                }
                break;
            case 16: // Wait until the robot is near the third sample pickup position
                if (follower.getPose().getX() > (pickup3Pose.getX() - 1) && follower.getPose().getY() > (pickup3Pose.getY() - 1)) {
                    intakePoseAuto.start();
                    setPathState(17);
                }
                break;
            case 17:
                if (intakePoseAuto.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
                    grabWall.start();
                    setPathState(18);
                }
                break;
            case 18:
                if (grabWall.isFinished() && pathTimer.getElapsedTime() > neutralDelay) {
                    neutralAction.start();
                    setPathState(19);
                }
                break;
            case 19:
                if (neutralAction.isFinished() && pathTimer.getElapsedTime() > scoreDelay) {
                    toggleBasket.start();
                    follower.followPath(scorePickup3, true);
                    setPathState(20);
                }
                break;
            case 20: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() < (scorePose.getX() + 1) && follower.getPose().getY() < (scorePose.getY() + 1) && pathTimer.getElapsedTime() > basketDelayUp && !follower.isBusy()) {
                    setPathState(21);
                }
                break;
            case 21:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    toggleBasket.start();
                    follower.followPath(park, true);
                    setPathState(22);
                }
                break;
            case 22:
                if (follower.getPose().getX() > (parkPose.getX() - 5) && follower.getPose().getY() < (parkPose.getY() + 5)) {
                    toggleBasket.cancel();
                    intakePoseAuto.start();
                    pathTimer.resetTimer();
                    setPathState(23);
                }
                break;
            case 23:
                if (intakePoseAuto.isFinished() && pathTimer.getElapsedTime() > 500) {
                    lockOn.start();
                    setPathState(24);
                }
                break;
            case 24:
                if (lockOn.isFinished()) {
                    if (lockOn.failed()) {
                        pathTimer.resetTimer();
                        follower.followPath(park, true);
                        setPathState(30);
                    } else {
                        pathTimer.resetTimer();
                        setPathState(25);
                    }
                } else if (pathTimer.getElapsedTime() > lockOnFailsafe) {
                    pathTimer.resetTimer();
                    follower.followPath(park, true);
                    setPathState(30);
                }
                break;
            case 25:
                if (pathTimer.getElapsedTime() > 500) {
                    camera.takeSnapshot("submersible");
                    grabSub.start();
                    setPathState(26);

                    scoreSubmersible = follower.pathBuilder()
                            .addPath(new BezierLine(new Point(follower.getPose()), new Point(scorePose)))
                            .setLinearHeadingInterpolation(follower.getPose().getHeading(), scorePose.getHeading())
                            .setPathEndVelocityConstraint(0.3)
                            .build();
                }
                break;
            case 26:
                if (grabSub.isFinished() && pathTimer.getElapsedTime() > neutralDelay) {
                    neutralAction.start();
                    setPathState(27);
                }
                break;
            case 27:
                if (neutralAction.isFinished() && pathTimer.getElapsedTime() > scoreDelay) {
                    toggleBasket.start();
                    follower.followPath(scoreSubmersible, true);
                    setPathState(28);
                }
                break;
            case 28: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() < (scorePose.getX() + 1) && follower.getPose().getY() > (scorePose.getY() - 1) && pathTimer.getElapsedTime() > basketDelayUp && !follower.isBusy()) {
                    setPathState(29);
                }
                break;
            case 29:
                if (toggleBasket.isFinished() && pathTimer.getElapsedTime() > 100) {
                    toggleBasket.start();
                    follower.followPath(park, true);
                    setPathState(30);
                }
                break;
//
//            case 3: // Wait until the robot returns to the scoring position
//                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
//                    follower.followPath(grabPickup2, true);
//                    setPathState(4);
//                }
//                break;
//
//            case 4: // Wait until the robot is near the second sample pickup position
//                if (follower.getPose().getX() > (pickup2Pose.getX() - 1) && follower.getPose().getY() > (pickup2Pose.getY() - 1)) {
//                    follower.followPath(scorePickup2, true);
//                    setPathState(5);
//                }
//                break;
//
//            case 5: // Wait until the robot returns to the scoring position
//                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
//                    follower.followPath(grabPickup3, true);
//                    setPathState(6);
//                }
//                break;
//
//            case 6: // Wait until the robot is near the third sample pickup position
//                if (follower.getPose().getX() > (pickup3Pose.getX() - 1) && follower.getPose().getY() > (pickup3Pose.getY() - 1)) {
//                    follower.followPath(scorePickup3, true);
//                    setPathState(7);
//                }
//                break;
//
//            case 7: // Wait until the robot returns to the scoring position
//                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
//                    follower.followPath(park, true);
//                    setPathState(8);
//                }
//                break;
//
//            case 8: // Wait until the robot is near the parking position
//                if (follower.getPose().getX() > (parkPose.getX() - 1) && follower.getPose().getY() > (parkPose.getY() - 1)) {
//                    setPathState(-1); // End the autonomous routine
//                }
//                break;
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
        intakePoseAuto.loop();
        toggleBasket.loop();
        grab.loop();
        grabSub.loop();
        grabWall.loop();
        neutralAction.loop();
        lockOn.loop();

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

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();

        basketPose = new BasketPoseAuto(rotState, extState, elbowState, wristState);
        downFromBasket = new DownFromBasket(rotState, extState, elbowState, wristState);
        toggleBasket = new ToggleBasketAuto(extState, clawState, basketPose, downFromBasket);
        preGrabPose = new PreGrabPoseAuto(elbowState, wristState, clawState);
        intakePoseAuto = new IntakePoseAuto(rotState, extState, clawState, preGrabPose);
        grab = new GrabAuto(elbowState, wristState, clawState);
        grabWall = new GrabAutoWall(elbowState, wristState, clawState);
        grabSub = new GrabSubmersible(elbowState, wristState, clawState);
        neutralAction = new NeutralAction(rotState, extState, elbowState, wristState);
        lockOn = new LockOn(camera, follower, wristState, neutralAction);
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
        wristState.setPose(WristPose.STOW);
        elbowState.setPose(ElbowPose.STOW);
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
