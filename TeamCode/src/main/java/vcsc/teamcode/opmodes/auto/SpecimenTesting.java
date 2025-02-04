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
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.basket.WristSpecimenPose;
import vcsc.teamcode.actions.intake.Grab;
import vcsc.teamcode.actions.intake.IntakePoseWall;
import vcsc.teamcode.actions.intake.PreGrabPoseAuto;
import vcsc.teamcode.actions.specimen.SpecimenPose;
import vcsc.teamcode.opmodes.base.BaseOpModeAuto;

@Disabled
@Autonomous(name = "SpecimenTest", group = "Main")
public class SpecimenTesting extends BaseOpModeAuto {
    private final Pose startPose = new Pose(10.855, 62.03, Math.toRadians(180));  // Starting position
    private final Pose scorePose = new Pose(36.1107, 71.335, Math.toRadians(180)); // Observation zone position
    private final Pose pickupPose = new Pose(13.514, 17.723);
    private final Pose specimen1 = new Pose(24, 122, Math.toRadians(0)); // First sample pickup
    private final Pose specimen2 = new Pose(24, 131, Math.toRadians(0)); // Second sample pickup
    private final Pose specimen3 = new Pose(25, 131, Math.toRadians(22)); // Third sample pickup

    private final Pose parkPose = new Pose(60, 98, Math.toRadians(270));    // Parking position
    private final Pose parkControlPose = new Pose(65, 125, Math.toRadians(90)); // Control point for curved path

    PreGrabPoseAuto preGrabPoseAuto;
    WristSpecimenPose wristSpecimenPose;
    Grab grab;
    SpecimenPose specimenPose;
    NeutralAction neutralAction;
    IntakePoseWall intakePoseWall;
    private Path scorePreload, park;
    private PathChain grabPickup, scorePickup1, scorePickup2, scorePickup3;
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    public void BuildPaths() {
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());
        scorePreload.setPathEndVelocityConstraint(0.3);

        // Path chains for picking up and scoring samples
        grabPickup = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickupPose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickupPose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickupPose.getHeading(), scorePose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();


        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickupPose.getHeading(), scorePose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickupPose.getHeading(), scorePose.getHeading())
                .setPathEndVelocityConstraint(0.3)
                .build();

        // Curved path for parking
        park = new Path(new BezierCurve(new Point(scorePose), new Point(parkControlPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());
    }

    public void autonomousPathUpdate() {
        double grabDelay = 2000;
        double neutralDelay = 250;
        double scoreDelay = 800;
        double basketDelayUp = 2800;
        double basketDelayDown = 3000;
        switch (pathState) {
            case 0: // Move from start to scoring position
                follower.followPath(scorePreload);
                break;
            case 1: // Wait until the robot is near the scoring position
                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1) && pathTimer.getElapsedTime() < basketDelayUp) {
                    specimenPose.start();
                    setPathState(2);
                }
                break;
            case 2:
                if (specimenPose.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    specimenPose.start();
                    follower.followPath(grabPickup, true);
                    setPathState(3);
                }
                break;
//
            case 3: // Wait until the robot is near the first sample pickup position
                if (follower.getPose().getX() > (pickupPose.getX() - 1) && follower.getPose().getY() > (pickupPose.getY() - 1)) {
                    intakePoseWall.start();
                    setPathState(4);
                }
                break;
            case 4: // Grab
                if (intakePoseWall.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
                    grab.start();
                    setPathState(5);
                }
                break;
            case 5: // Neutral
                if (intakePoseWall.isFinished() && pathTimer.getElapsedTime() > neutralDelay) {
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
                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1) && pathTimer.getElapsedTime() < basketDelayUp) {
                    specimenPose.start();
                    setPathState(8);
                }
                break;
            case 8:
                if (specimenPose.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    specimenPose.start();
                    follower.followPath(grabPickup, true);
                    setPathState(9);
                }
                break;
            case 9: // Wait until the robot is near the first sample pickup position
                if (follower.getPose().getX() > (pickupPose.getX() - 1) && follower.getPose().getY() > (pickupPose.getY() - 1)) {
                    intakePoseWall.start();
                    setPathState(10);
                }
                break;
            case 10:
                if (intakePoseWall.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
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
                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1) && pathTimer.getElapsedTime() < basketDelayUp) {
                    specimenPose.start();
                    setPathState(14);
                }
                break;
            case 14:
                if (specimenPose.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    specimenPose.start();
                    follower.followPath(grabPickup, true);
                    setPathState(15);
                }
                break;
            case 15: // Wait until the robot is near the first sample pickup position
                if (follower.getPose().getX() > (pickupPose.getX() - 1) && follower.getPose().getY() > (pickupPose.getY() - 1)) {
                    intakePoseWall.start();
                    setPathState(16);
                }
                break;
            case 16:
                if (intakePoseWall.isFinished() && pathTimer.getElapsedTime() > grabDelay) {
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
                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1) && pathTimer.getElapsedTime() < basketDelayUp) {
                    specimenPose.start();
                    setPathState(20);
                }
                break;
            case 20:
                if (specimenPose.isFinished() && pathTimer.getElapsedTime() > basketDelayDown) {
                    specimenPose.start();
                    follower.followPath(park);
                    setPathState(21);
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {
        super.loop();

//        basketPose.loop();
//        downFromBasket.loop();

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

    @Override
    public void init() {
        super.init();
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        neutralAction = new NeutralAction(rotState, extState, elbowState, wristState);

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        BuildPaths();
    }

    @Override
    public void init_loop() {
        super.init_loop();
    }

    @Override
    public void start() {
        super.start();
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
        super.stop();
    }
}



