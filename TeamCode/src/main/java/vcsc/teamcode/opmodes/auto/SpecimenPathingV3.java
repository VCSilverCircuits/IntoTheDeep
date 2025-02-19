package vcsc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.actions.NeutralAction;
import vcsc.teamcode.actions.NeutralActionSpecimen;
import vcsc.teamcode.actions.intake.GrabGround;
import vcsc.teamcode.actions.intake.IntakePoseSpecimenGround;
import vcsc.teamcode.actions.intake.PreGrabPoseSpecimenGround;
import vcsc.teamcode.actions.specimen.ScoreSpecimen;
import vcsc.teamcode.actions.specimen.SpecimenPose;
import vcsc.teamcode.opmodes.base.BaseOpModeAuto;

@Autonomous(name = "SPECIMENV3 (Hook) Auto", group = "Testing", preselectTeleOp = "Tele")
public class SpecimenPathingV3 extends BaseOpModeAuto {
    // ===== CONSTANTS =====
    // >>> Positions
    private final double SCORE_X = 38; // X position for scoring specimens
    private final double INITIAL_SCORE_Y = 65; // Y position for scoring first specimen
    private final double SCORE_SPACING = 3; // Y spacing between specimens
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
    private final Pose P_INTAKE = new Pose(20, 30, Math.toRadians(235));
    // >>> Sample Grabbing
    private final Pose P_SAMPLE_GRAB = new Pose(25, 15, Math.toRadians(0));
    private final double SAMPLE_GRAB_HEADING_1 = Math.toRadians(25);
    private final double SAMPLE_GRAB_HEADING_2 = Math.toRadians(0);
    private final double SAMPLE_GRAB_HEADING_3 = Math.toRadians(-25);
    // >>> Park
    private final Pose P_PARK = new Pose(14, 21, Math.toRadians(-113));
    private final double SCORE_EMERGENCY_DELAY = 800;
    private final double PRE_LIFT_DELAY = 100; // Delay before the arm starts lifting to prevent smashing wall
    // ===== PATHS =====
    PathChain startToScore1,
            score1ToSampleGrab, sampleGrabToIntake,
            intakeToScore2, score2ToIntake,
            intakeToScore3, score3ToIntake,
            intakeToScore4, score4ToIntake,
            intakeToScore5, score5ToIntake,
            intakeToScore6, score6ToIntake,
            score5ToPark, score6ToPark;
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

    // Timers
    private ElapsedTime pathTimer;
    private int pathSegment = -1;

    private void buildPaths() {
        // Start to Score 1
        startToScore1 = linearInterpolateLine(P_START, P_SCORE_1);

        // Score 1 to Sample Grab
        score1ToSampleGrab = linearInterpolateLine(P_SCORE_1, P_SAMPLE_GRAB);

        // Sample Grab to Intake
        sampleGrabToIntake = linearInterpolateLine(P_SAMPLE_GRAB, P_INTAKE);

        // Intake to Score 2
        intakeToScore2 = linearInterpolateLine(P_INTAKE, P_SCORE_2);

        // Score 2 to Intake
        score2ToIntake = linearInterpolateLine(P_SCORE_2, P_INTAKE);

        // Intake to Score 3
        intakeToScore3 = linearInterpolateLine(P_INTAKE, P_SCORE_3);

        // Score 3 to Intake
        score3ToIntake = linearInterpolateLine(P_SCORE_3, P_INTAKE);

        // Intake to Score 4
        intakeToScore4 = linearInterpolateLine(P_INTAKE, P_SCORE_4);

        // Score 4 to Intake
        score4ToIntake = linearInterpolateLine(P_SCORE_4, P_INTAKE);

        // Intake to Score 5
        intakeToScore5 = linearInterpolateLine(P_INTAKE, P_SCORE_5);

        // Score 5 to Intake
        score5ToIntake = linearInterpolateLine(P_SCORE_5, P_INTAKE);

        // Intake to Score 6
        intakeToScore6 = linearInterpolateLine(P_INTAKE, P_SCORE_6);

        // Score 6 to Intake
        score6ToIntake = linearInterpolateLine(P_SCORE_6, P_INTAKE);

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

        pathTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public void pathLoop() {
        switch (pathSegment) {
            case 0: // Start driving to bar
                follower.setMaxPower(0.75);
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
            case 3: // Drive to grab position
                if (scoreSpecimen.isFinished() || pathTimer.time() > SCORE_EMERGENCY_DELAY) {
                    cleanupScore();
                    follower.setMaxPower(1);
                    follower.followPath(score1ToSampleGrab, true);
                }
                break;
            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(sampleGrabToIntake);
                    pathSegment++;
                }
                break;
            case 5:
                if (!follower.isBusy()) {
                    intakePoseSpecimenGround.start();
                    pathSegment++;
                }
                break;
            case 6:
                if (intakePoseSpecimenGround.isFinished()) {
                    grabGround.start();
                    pathSegment++;
                }
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



