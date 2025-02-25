package vcsc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.actions.GolfingSample1;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.NeutralActionSpecimen;
import vcsc.teamcode.actions.intake.Grab;
import vcsc.teamcode.actions.intake.GrabGround;
import vcsc.teamcode.actions.intake.IntakePoseSpecimenGround;
import vcsc.teamcode.actions.intake.PreGrabPoseGolf;
import vcsc.teamcode.actions.intake.PreGrabPoseSpecimenGround;
import vcsc.teamcode.actions.specimen.ScoreSpecimen;
import vcsc.teamcode.actions.specimen.SpecimenPose;
import vcsc.teamcode.opmodes.base.BaseOpModeAuto;

@Disabled
@Autonomous(name = "SPECIMENV3 (Hook) Auto", group = "Testing", preselectTeleOp = "Tele")
public class SpecimenPathingV3 extends BaseOpModeAuto {
    // ===== CONSTANTS =====
    // >>> Positions
    private final double SCORE_X = 38; // X position for scoring specimens
    private final double INITIAL_SCORE_Y = 65; // Y position for scoring first specimen
    private final double SCORE_SPACING = 2.5; // Y spacing between specimens
    private final double INTAKE_FORWARD_DISTANCE = 10; // inches forward between intake start and finish
    // ===== POSES =====
    // >>> Start
    private final Pose P_START = new Pose(9, 65, Math.toRadians(180));
    // >>> Scoring
    private final Pose P_SCORE_1 = getSpecimenPose(0);
    private final Pose P_SCORE_2 = getSpecimenPose(1);
    private final Pose P_SCORE_3 = getSpecimenPose(2);
    private final Pose P_SCORE_4 = getSpecimenPose(3);
    private final Pose P_SCORE_5 = getSpecimenPose(4);
    private final Pose P_SCORE_6 = getSpecimenPose(5);
    // >>> Intake
    private final Pose P_INTAKE_START = new Pose(14, 42, Math.toRadians(245));
    private final Pose P_INTAKE_FINISH = new Pose(
            P_INTAKE_START.getX() + INTAKE_FORWARD_DISTANCE * Math.cos(P_INTAKE_START.getHeading()),
            P_INTAKE_START.getY() + INTAKE_FORWARD_DISTANCE * Math.sin(P_INTAKE_START.getHeading()),
            Math.toRadians(245)
    );
    // >>> Sample Grabbing
    private final Pose P_SAMPLE_GRAB1 = new Pose(30, 35, Math.toRadians(-40));
    private final Pose P_SAMPLE_GRAB2 = new Pose(30, 25, Math.toRadians(-40));
    private final Pose P_SAMPLE_GRAB3 = new Pose(30, 15, Math.toRadians(-40));
    private final Pose P_SAMPLE_DROP = new Pose(16, 20, Math.toRadians(245));
    private final Pose P_SAMPLE_GRAB = new Pose(30, 35, Math.toRadians(0));
    private final double SAMPLE_GRAB_HEADING_1 = Math.toRadians(-40);
    private final double SAMPLE_GRAB_HEADING_2 = Math.toRadians(0);
    private final double SAMPLE_GRAB_HEADING_3 = Math.toRadians(-25);
    // >>> Park
    private final Pose P_PARK = new Pose(14, 21, Math.toRadians(-113));
    private final double SCORE_EMERGENCY_DELAY = 800;
    private final double PRE_LIFT_DELAY = 300; // Delay before the arm starts lifting to prevent smashing wall
    private final double INTAKE_POSE_DELAY = 100; // Delay after intake before grab
    private final double GRAB_DELAY = 250; // Delay between grab and neutral
    // ===== PATHS =====
    PathChain intakeStartToFinish, startToScore1,
            score1ToSampleGrab1, sampleGrab1ToSampleDrop,
            sampleDropToSampleGrab2, sampleGrab2ToSampleDrop,
            sampleDropToSampleGrab3, sampleGrab3ToIntake,
            intakeToScore2, score2ToIntake,
            intakeToScore3, score3ToIntake,
            intakeToScore4, score4ToIntake,
            intakeToScore5, score5ToIntake,
            intakeToScore6, score6ToIntake,
            score4ToPark, score5ToPark, score6ToPark;
    // ===== ACTIONS & COMPONENTS =====
    Follower follower;
    ActionBuilder r;
    SpecimenPose specimenPose;
    ScoreSpecimen scoreSpecimen;
    NeutralActionSpecimen neutralActionSpecimen;
    IntakePoseSpecimenGround intakePoseSpecimenGround;
    PreGrabPoseSpecimenGround preGrabPoseSpecimenGround;
    GrabGround grabGround;
    NeutralAction neutralAction;
    PreGrabPoseGolf preGrabPoseGolf;
    GolfingSample1 golfingSample1;
    Grab grab;
    // Timers
    private ElapsedTime pathTimer;
    private int pathSegment = -1;

    private void buildPaths() {
        intakeStartToFinish = linearInterpolateLine(P_INTAKE_START, P_INTAKE_FINISH);

        score1ToSampleGrab1 = linearInterpolateLine(P_SCORE_1, P_SAMPLE_GRAB1);

        sampleGrab1ToSampleDrop = linearInterpolateLine(P_SAMPLE_GRAB1, P_SAMPLE_DROP);

        sampleDropToSampleGrab2 = linearInterpolateLine(P_SAMPLE_DROP, P_SAMPLE_GRAB2);

        sampleGrab2ToSampleDrop = linearInterpolateLine(P_SAMPLE_GRAB2, P_SAMPLE_DROP);

        sampleDropToSampleGrab3 = linearInterpolateLine(P_SAMPLE_DROP, P_SAMPLE_GRAB3);

        sampleGrab3ToIntake = linearInterpolateLine(P_SAMPLE_GRAB3, P_INTAKE_START);

        // Start to Score 1
        startToScore1 = linearInterpolateLine(P_START, P_SCORE_1);

        // Intake to Score 2
        intakeToScore2 = linearInterpolateLine(P_INTAKE_FINISH, P_SCORE_2);

        // Score 2 to Intake
        score2ToIntake = linearInterpolateLine(P_SCORE_2, P_INTAKE_START);

        // Intake to Score 3
        intakeToScore3 = linearInterpolateLine(P_INTAKE_FINISH, P_SCORE_3);

        // Score 3 to Intake
        score3ToIntake = linearInterpolateLine(P_SCORE_3, P_INTAKE_START);

        // Intake to Score 4
        intakeToScore4 = linearInterpolateLine(P_INTAKE_FINISH, P_SCORE_4);

        // Score 4 to Intake
        score4ToIntake = linearInterpolateLine(P_SCORE_4, P_INTAKE_START);

        // Intake to Score 5
        intakeToScore5 = linearInterpolateLine(P_INTAKE_FINISH, P_SCORE_5);

        // Score 5 to Intake
        score5ToIntake = linearInterpolateLine(P_SCORE_5, P_INTAKE_START);

        // Intake to Score 6
        intakeToScore6 = linearInterpolateLine(P_INTAKE_FINISH, P_SCORE_6);

        // Score 6 to Intake
        score6ToIntake = linearInterpolateLine(P_SCORE_6, P_INTAKE_START);

        // Score 4 to Park
        score4ToPark = linearInterpolateLine(P_SCORE_4, P_PARK);

        // Score 5 to Park
        score5ToPark = linearInterpolateLine(P_SCORE_5, P_PARK);

        // Score 6 to Park
        score6ToPark = linearInterpolateLine(P_SCORE_6, P_PARK);
    }

    private PathChain linearInterpolateLine(Pose start, Pose end) {
        return follower.pathBuilder().addBezierLine(
                new Point(start),
                new Point(end)
        ).setLinearHeadingInterpolation(start.getHeading(), end.getHeading()).build();
    }

    private Pose getSpecimenPose(int specimenNumber) {
        return new Pose(SCORE_X, INITIAL_SCORE_Y + SCORE_SPACING * specimenNumber, Math.toRadians(180));
    }

    @Override
    public void init() {
        super.init();
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(P_START);
        buildPaths();

        // ===== Actions =====
        specimenPose = new SpecimenPose(rotState, extState, elbowState, wristState);
        neutralActionSpecimen = new NeutralActionSpecimen(rotState, extState, elbowState, wristState);
        scoreSpecimen = new ScoreSpecimen(extState, clawState, neutralActionSpecimen);
        neutralAction = new NeutralAction(rotState, extState, elbowState, wristState);
        preGrabPoseSpecimenGround = new PreGrabPoseSpecimenGround(elbowState, wristState, clawState);
        intakePoseSpecimenGround = new IntakePoseSpecimenGround(rotState, extState, clawState, preGrabPoseSpecimenGround);
        grabGround = new GrabGround(elbowState, wristState, clawState, neutralAction);
        preGrabPoseGolf = new PreGrabPoseGolf(elbowState, wristState, clawState);
        golfingSample1 = new GolfingSample1(rotState, extState, clawState, preGrabPoseGolf);
        grab = new Grab(elbowState, wristState, clawState);

        pathTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public void pathLoop() {
        switch (pathSegment) {
            case 0: // Start driving to bar
                follower.setMaxPower(0.6);
                follower.followPath(startToScore1, true);
                pathSegment++;
                pathTimer.reset();
                break;
            case 1: // Start lifting arm
                if (pathTimer.time() > PRE_LIFT_DELAY) {
                    specimenPose.start();
                    pathSegment++;
                }
                break;
            case 2: // Score
                if (!follower.isBusy() && specimenPose.isFinished()) {
                    scoreSpecimen();
                }
                break;
            case 3:
                if (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_EMERGENCY_DELAY) {
                    cleanupScore();
                    follower.setMaxPower(1);
                    follower.followPath(score1ToSampleGrab1, true);
                }
                break;
            case 4:
                if (!follower.isBusy()) {
                    // TODO: Grab sample 1
                    golfingSample1.start();
                    pathTimer.reset();
                    pathSegment++;
                }
                break;
            case 5:
                if (golfingSample1.isFinished() && pathTimer.time() > INTAKE_POSE_DELAY) {
                    grab.start();
                    pathTimer.reset();
                    pathSegment++;
                }
                break;
            case 6:
                if (grab.isFinished() && pathTimer.time() > GRAB_DELAY) {
                    neutralAction.start();
                    follower.followPath(sampleGrab1ToSampleDrop);
                    pathSegment++;
                }
                break;
            case 7:
                if (!follower.isBusy()) {
                    clawState.open();
                    follower.followPath(sampleDropToSampleGrab2);
                    pathSegment++;
                }
                break;
            case 8:
                if (!follower.isBusy()) {
                    // TODO: Grab sample 2
                    golfingSample1.start();
                    pathSegment++;
                }
                break;
            case 9:
                if (golfingSample1.isFinished() && pathTimer.time() > INTAKE_POSE_DELAY) {
                    grab.start();
                    pathTimer.reset();
                    pathSegment++;
                }
                break;
            case 10:
                if (grab.isFinished() && pathTimer.time() > GRAB_DELAY) {
                    neutralAction.start();
                    follower.followPath(sampleGrab2ToSampleDrop);
                    pathSegment++;
                }
                break;
            case 11:
                if (!follower.isBusy()) {
                    // TODO: Drop sample
                    clawState.open();
                    follower.followPath(sampleDropToSampleGrab3);
                    pathSegment++;
                }
                break;
            case 12:
                if (!follower.isBusy()) {
                    // TODO: Grab sample 3
                    golfingSample1.start();
                    pathSegment++;
                }
                break;
            case 13:
                if (golfingSample1.isFinished() && pathTimer.time() > INTAKE_POSE_DELAY) {
                    grab.start();
                    pathTimer.reset();
                    pathSegment++;
                }
            case 14:
                if (grab.isFinished() && pathTimer.time() > GRAB_DELAY) {
                    neutralAction.start();
                    follower.followPath(sampleGrab3ToIntake);
                    pathSegment++;
                }
            case 15:
                if (!follower.isBusy()) {
                    // TODO: Drop sample
                    clawState.open();
                    intakePoseSpecimenGround.start();
                    pathSegment++;
                }
            case 16:
                if (intakePoseSpecimenGround.isFinished()) {
                    follower.followPath(intakeStartToFinish);
                    pathSegment++;
                }
                break;
            case 17:
                if (!follower.isBusy()) {
                    grabGround.start();
                    pathSegment++;
                }
                break;
            case 18:
                if (grabGround.isFinished()) {
                    specimenPose.start();
                    follower.followPath(intakeToScore2);
                    pathSegment++;
                }
                break;
            case 19: // Score
                if (!follower.isBusy() && specimenPose.isFinished()) {
                    scoreSpecimen();
                }
                break;
            case 20: // Drive to grab position
                if (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_EMERGENCY_DELAY) {
                    cleanupScore();
                    follower.followPath(score2ToIntake, true);
                }
                break;
            case 21:
                if (!follower.isBusy()) {
                    intakePoseSpecimenGround.start();
                    pathSegment++;
                }
                break;
            case 22:
                if (intakePoseSpecimenGround.isFinished()) {
                    follower.followPath(intakeStartToFinish);
                    pathSegment++;
                }
                break;
            case 23:
                if (!follower.isBusy()) {
                    grabGround.start();
                    pathSegment++;
                }
                break;
            case 24:
                if (grabGround.isFinished()) {
                    specimenPose.start();
                    follower.followPath(intakeToScore3);
                    pathSegment++;
                }
                break;
            case 25: // Score
                if (!follower.isBusy() && specimenPose.isFinished()) {
                    scoreSpecimen();
                }
                break;
            case 26: // Drive to grab position
                if (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_EMERGENCY_DELAY) {
                    cleanupScore();
                    follower.followPath(score3ToIntake, true);
                }
                break;
            case 27:
                if (!follower.isBusy()) {
                    intakePoseSpecimenGround.start();
                    pathSegment++;
                }
                break;
            case 28:
                if (intakePoseSpecimenGround.isFinished()) {
                    follower.followPath(intakeStartToFinish);
                    pathSegment++;
                }
                break;
            case 29:
                if (!follower.isBusy()) {
                    grabGround.start();
                    pathSegment++;
                }
                break;
            case 30:
                if (grabGround.isFinished()) {
                    specimenPose.start();
                    follower.followPath(intakeToScore4);
                    pathSegment++;
                }
                break;
            case 31: // Score
                if (!follower.isBusy() && specimenPose.isFinished()) {
                    scoreSpecimen();
                }
                break;
            case 32: // Drive to grab position
                if (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_EMERGENCY_DELAY) {
                    cleanupScore();
                    follower.followPath(score4ToPark, true);
                }
                break;
        }
    }

    public void cleanupScore() {
        scoreSpecimen.cancel();
        clawState.open();
        if (!scoreSpecimen.isFinished()) {
            neutralActionSpecimen.start();
        }
        pathSegment++;
    }

    public void actionLoop() {
        specimenPose.loop();
        scoreSpecimen.loop();
        neutralActionSpecimen.loop();
        intakePoseSpecimenGround.loop();
        grabGround.loop();
        golfingSample1.loop();
        grab.loop();
    }

    public void scoreSpecimen() {
        specimenPose.cancel();
        scoreSpecimen.start();
        pathTimer.reset();
        pathSegment++;
    }

    @Override
    public void start() {
        super.start();
        pathSegment = 0;
    }

    @Override
    public void loop() {
        super.loop();
        pathLoop();
        actionLoop();
        follower.update();

        // ===== Feedback to Driver Hub =====
        telem.addData("x", follower.getPose().getX());
        telem.addData("y", follower.getPose().getY());
        telem.addData("heading", follower.getPose().getHeading());
        telem.addData("pathSegment", pathSegment);
    }
}



