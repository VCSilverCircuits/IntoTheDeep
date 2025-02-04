package vcsc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.teamcode.actions.NeutralActionSpecimen;
import vcsc.teamcode.actions.intake.GrabWall;
import vcsc.teamcode.actions.intake.IntakePoseWall;
import vcsc.teamcode.actions.intake.PreGrabPoseWall;
import vcsc.teamcode.actions.specimen.ScoreSpecimen;
import vcsc.teamcode.actions.specimen.SpecimenPose;
import vcsc.teamcode.opmodes.base.BaseOpModeAuto;

@Autonomous(name = "SPECIMEN (Hook) Auto", group = "Testing", preselectTeleOp = "Tele")
public class SpecimenPathing extends BaseOpModeAuto {
    // X position for specimen scoring location
    final double SCORE_X = 38.0;
    // Buffer before starting to score
    final double SCORE_X_BUFFER = 1;
    final double SCORE_DELAY = 800; // Delay in milliseconds after scoring before driving
    final double GRAB_DELAY = 80; // Delay in milliseconds after grabbing off wall before driving
    Follower follower; // Pedropath follower
    int pathSegment = 0; // Current path segment
    SpecimenPose specimenPose;
    NeutralActionSpecimen neutralActionSpecimen;
    ScoreSpecimen scoreSpecimen;
    IntakePoseWall intakePoseWall;
    GrabWall grabWall;
    ElapsedTime pathTimer;
    ElapsedTime overallTimer;
    private Path scorePreload;
    private PathChain prep1, push1, prep2, push2, prep3, push3, score2, grab2, score3, grab3, score4, grab4;

    public void buildPaths() {
        scorePreload = new Path(
                // Line 1
                new BezierLine(
                        new Point(9.301, 65.971, Point.CARTESIAN),
                        new Point(SCORE_X, 65, Point.CARTESIAN)
                )
        );
        scorePreload.setPathEndVelocityConstraint(0.5);
        scorePreload.setConstantHeadingInterpolation(Math.toRadians(180));
        prep1 = follower.pathBuilder().addPath(
                        // Line 2
                        new BezierCurve(
                                new Point(SCORE_X, 65.971, Point.CARTESIAN),
                                new Point(20.670, 13.952, Point.CARTESIAN),
                                new Point(70.622, 55.292, Point.CARTESIAN),
                                new Point(45, 23.254, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180)).build();
        push1 = follower.pathBuilder().addPath(
                // Line 3
                new BezierLine(
                        new Point(62.354, 23.254, Point.CARTESIAN),
                        new Point(16, 23.081, Point.CARTESIAN)
                )
        ).setConstantHeadingInterpolation(Math.toRadians(180)).setPathEndVelocityConstraint(1).build();
        prep2 = follower.pathBuilder()
                .addPath(
                        // Line 4
                        new BezierCurve(
                                new Point(15.158, 23.081, Point.CARTESIAN),
                                new Point(59.081, 29.799, Point.CARTESIAN),
                                new Point(53, 12.402, Point.CARTESIAN)
                        )
                ).setPathEndVelocityConstraint(1)
                .setConstantHeadingInterpolation(Math.toRadians(180)).build();
        push2 = follower.pathBuilder().addPath(
                        // Line 5
                        new BezierLine(
                                new Point(61.837, 12.402, Point.CARTESIAN),
                                new Point(16, 13.091, Point.CARTESIAN)
                        )
                ).setPathEndVelocityConstraint(1)
                .setConstantHeadingInterpolation(Math.toRadians(180)).build();
        prep3 = follower.pathBuilder()
                .addPath(
                        // Line 6
                        new BezierCurve(
                                new Point(16.019, 13.091, Point.CARTESIAN),
                                new Point(59.426, 19.464, Point.CARTESIAN),
                                new Point(53, 6.890, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndVelocityConstraint(1).build();
        push3 = follower.pathBuilder().addPath(
                        // Line 7
                        new BezierLine(
                                new Point(59.943, 7, Point.CARTESIAN),
                                new Point(18, 9, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180)).setPathEndVelocityConstraint(1)
                .build();
        score2 = follower.pathBuilder().addPath(
                        // Line 8
                        new BezierCurve(
                                new Point(18.000, 9.000, Point.CARTESIAN),
                                new Point(20.842, 67.866, Point.CARTESIAN),
                                new Point(SCORE_X, 68, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180)).setPathEndVelocityConstraint(1).build();
        grab2 = follower.pathBuilder().addPath(
                        // Line 9
                        new BezierCurve(
                                new Point(SCORE_X, 68.000, Point.CARTESIAN),
                                new Point(7.923, 67.694, Point.CARTESIAN),
                                new Point(60.115, 27.560, Point.CARTESIAN),
                                new Point(18.000, 31.694, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180)).setPathEndVelocityConstraint(1).build();
        score3 = follower.pathBuilder().addPath(
                        // Line 8
                        new BezierCurve(
                                new Point(18.000, 31.694, Point.CARTESIAN),
                                new Point(20.842, 67.866, Point.CARTESIAN),
                                new Point(SCORE_X, 71, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180)).setPathEndVelocityConstraint(1).build();
        grab3 = follower.pathBuilder().addPath(
                        // Line 9
                        new BezierCurve(
                                new Point(SCORE_X, 71.000, Point.CARTESIAN),
                                new Point(22.737, 54.431, Point.CARTESIAN),
                                new Point(50.813, 29.282, Point.CARTESIAN),
                                new Point(18.000, 31.694, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180)).setPathEndVelocityConstraint(1).build();
        score4 = follower.pathBuilder().addPath(
                        // Line 8
                        new BezierCurve(
                                new Point(18.000, 31.694, Point.CARTESIAN),
                                new Point(20.842, 67.866, Point.CARTESIAN),
                                new Point(SCORE_X, 74, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180)).setPathEndVelocityConstraint(1).build();
        grab4 = follower.pathBuilder().addPath(
                        // Line 9
                        new BezierCurve(
                                new Point(SCORE_X, 74.000, Point.CARTESIAN),
                                new Point(27.38755980861244, 53.91387559808612, Point.CARTESIAN),
                                new Point(11.885167464114833, 67.17703349282297, Point.CARTESIAN),
                                new Point(12.91866028708134, 30.488038277511965, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(245)).setPathEndVelocityConstraint(1).build();
        //testPath = builder.build();
    }

    @Override
    public void init() {
        super.init();

        specimenPose = new SpecimenPose(rotState, extState, elbowState, wristState);
        neutralActionSpecimen = new NeutralActionSpecimen(rotState, extState, elbowState, wristState);
        scoreSpecimen = new ScoreSpecimen(extState, clawState, neutralActionSpecimen);
        intakePoseWall = new IntakePoseWall(rotState, extState, clawState, new PreGrabPoseWall(elbowState, wristState, clawState));
        grabWall = new GrabWall(elbowState, wristState, clawState);
        pathTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        overallTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(9.301435406698566, 65.97129186602871, Math.toRadians(180)));
        buildPaths();
    }

    @Override
    public void start() {
        super.start();
        pathSegment = 0;
        overallTimer.reset();

    }

    @Override
    public void loop() {
        super.loop();

        // ===== Score Pre-load =====
        if (pathSegment == 0) { // Raise slides
            specimenPose.start();
            pathSegment = 1;
            pathTimer.reset();
        } else if (pathSegment == 1 && pathTimer.time() > 0) { // Drive to bar
            follower.followPath(scorePreload, true);
            pathTimer.reset();
            pathSegment = 2;
        } else if (pathSegment == 2 && follower.getPose().getX() >= (SCORE_X - SCORE_X_BUFFER) && !follower.isBusy()) { // Score specimen
            specimenPose.cancel();
            scoreSpecimen.start();
            pathSegment = 3;
            pathTimer.reset();
        }

        // ===== Pushing Samples =====
        else if (pathSegment == 3 && (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_DELAY)) { // Prep 1
            scoreSpecimen.cancel();
            clawState.open();
            if (!scoreSpecimen.isFinished()) {
                neutralActionSpecimen.start();
            }
            follower.followPath(prep1, true);
            pathSegment = 4;
        } else if (pathSegment == 4 && follower.getPose().getX() > 44) { // Push 1
            follower.followPath(push1, true);
            pathSegment = 5;
        } else if (pathSegment == 5 && follower.getPose().getX() < 16) { // Prep 2
            follower.followPath(prep2, true);
            pathSegment = 6;
        } else if (pathSegment == 6 && follower.getPose().getX() > 52) { // Push 2
            follower.followPath(push2, true);
            pathSegment = 7;
        } else if (pathSegment == 7 && follower.getPose().getX() < 16) { // Prep 3
            follower.followPath(prep3, true);
            intakePoseWall.start();
            pathSegment = 8;
        } else if (pathSegment == 8 && follower.getPose().getX() > 52) { // Push 3
            follower.followPath(push3, true);
            intakePoseWall.start();
            pathSegment = 9;
        }

        // ===== Score 2 =====
        else if (pathSegment == 9 && !follower.isBusy()) { // Grab wall
            grabWall.start();
            pathSegment = 10;
            pathTimer.reset();
        } else if (pathSegment == 10 && grabWall.isFinished() && pathTimer.time() > GRAB_DELAY) { // Drive to bar
            follower.followPath(score2);
            specimenPose.start();
            pathSegment = 11;
        } else if (pathSegment == 11 && follower.getPose().getX() >= (SCORE_X - SCORE_X_BUFFER) && !follower.isBusy()) { // Score specimen
            specimenPose.cancel();
            scoreSpecimen.start();
            pathSegment = 12;
            pathTimer.reset();
        }

        // ===== Score 3 =====
        else if (pathSegment == 12 && (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_DELAY)) { // Drive to wall
            scoreSpecimen.cancel();
            clawState.open();
            if (!scoreSpecimen.isFinished()) {
                neutralActionSpecimen.start();
            }
            follower.followPath(grab2);
            intakePoseWall.start();
            pathSegment = 13;
        } else if (pathSegment == 13 && !follower.isBusy()) { // Grab wall
            grabWall.start();
            pathSegment = 14;
        } else if (pathSegment == 14 && grabWall.isFinished() && pathTimer.time() > GRAB_DELAY) { // Drive to bar
            follower.followPath(score3);
            specimenPose.start();
            pathSegment = 15;
        } else if (pathSegment == 15 && follower.getPose().getX() >= (SCORE_X - SCORE_X_BUFFER) && !follower.isBusy()) { // Score specimen
            specimenPose.cancel();
            scoreSpecimen.start();
            pathSegment = 16;
            pathTimer.reset();
        }

        // ===== Score 4 =====
        else if (pathSegment == 16 && (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_DELAY)) { // Drive to wall
            scoreSpecimen.cancel();
            clawState.open();
            if (!scoreSpecimen.isFinished()) {
                neutralActionSpecimen.start();
            }
            follower.followPath(grab3);
            intakePoseWall.start();
            pathSegment = 17;
        } else if (pathSegment == 17 && !follower.isBusy()) { // Grab wall
            grabWall.start();
            pathSegment = 18;
        } else if (pathSegment == 18 && grabWall.isFinished() && pathTimer.time() > GRAB_DELAY) { // Drive to bar
            follower.followPath(score4);
            specimenPose.start();
            pathSegment = 19;
        } else if (pathSegment == 19 && follower.getPose().getX() >= (SCORE_X - SCORE_X_BUFFER) && !follower.isBusy()) { // Score specimen
            specimenPose.cancel();
            scoreSpecimen.start();
            pathSegment = 20;
            pathTimer.reset();
        }

        // ===== Park =====
        else if (pathSegment == 20 && (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_DELAY)) {
            scoreSpecimen.cancel();
            clawState.open();
            if (!scoreSpecimen.isFinished()) {
                neutralActionSpecimen.start();
            }
            follower.followPath(grab4);
            intakePoseWall.start();
            pathSegment = 21;
        }

        // Emergency stop timer
        if (overallTimer.time() > 29500) {
            specimenPose.cancel();
            scoreSpecimen.cancel();
            intakePoseWall.cancel();
            grabWall.cancel();
            neutralActionSpecimen.start();
            pathSegment = 99;
        }

        // ===== Main Loops =====
        specimenPose.loop();
        scoreSpecimen.loop();
        intakePoseWall.loop();
        neutralActionSpecimen.loop();
        grabWall.loop();
        follower.update();
        if (pathSegment > 9999) // 1
            follower.telemetryDebug(telem);


        // ===== Feedback to Driver Hub =====
        telem.addData("x", follower.getPose().getX());
        telem.addData("y", follower.getPose().getY());
        telem.addData("heading", follower.getPose().getHeading());
        telem.addData("pathSegment", pathSegment);

    }
}
