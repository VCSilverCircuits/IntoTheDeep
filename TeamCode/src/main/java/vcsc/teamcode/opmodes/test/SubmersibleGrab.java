package vcsc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;
import java.util.OptionalDouble;

import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.abstracts.Block;
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
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.actions.SetRotPose;
import vcsc.teamcode.component.hooks.actions.ToggleHooks;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Submersible Grab", group = "Testing")
public class SubmersibleGrab extends BaseOpMode {
    MultipleTelemetry telem;
    ArrayList<Double> angleList;
    double lastAngle = 0;
    int MAX_ANGLE_LIST_SIZE = 50;
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
    double x_offset, y_offset;

    PIDController xController, yController;

    @Override
    public void init() {
        super.init();
        GlobalTelemetry.init(telemetry);
        telem = GlobalTelemetry.getInstance();
        angleList = new ArrayList<>();
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
        xController = new PIDController(0.01, 0, 0);
        yController = new PIDController(0.01, 0, 0);
        follower.setStartingPose(new Pose(0, 0, 0));
    }

    @Override
    public void start() {
        super.start();
        intakePose.start();
        xController.setSetPoint(0);
        yController.setSetPoint(0);
        xController.setTolerance(10);
        yController.setTolerance(10);
    }

    @Override
    public void loop() {
        super.loop();
        Block block = camera.getBlock();
        if (block != null) {
            x_offset = block.getX() - 160;
            y_offset = 80 - block.getY();

            telem.addData("Block", block.getColor());
            telem.addData("X", block.getX());
            telem.addData("Y", block.getY());
            telem.addData("X Offset", x_offset);
            telem.addData("Y Offset", y_offset);
            telem.addData("Angle", block.getAngle());

            OptionalDouble angleAverage = angleList.stream().mapToDouble(a -> a).average();

            follower.setTeleOpMovementVectors(yController.calculate(-y_offset), 0, xController.calculate(x_offset));

            double angle = block.getAngle();
            if (Math.abs(angle) > 85) {
                angle = Math.abs(angle);
            }

            if (angleAverage.isPresent() && Math.abs(angleAverage.getAsDouble() - lastAngle) > 10 || Math.abs(angle - lastAngle) > 20) {

                double poseSpan = WristPivotPose.MAX.getPosition() - WristPivotPose.MIN.getPosition();
                double midPose = (WristPivotPose.MIN.getPosition() + WristPivotPose.MAX.getPosition()) / 2;
                double blockPose = midPose + angle / 90 * poseSpan / 2;

                wristState.setPivot(blockPose);
                lastAngle = angle;
                angleList.clear();
                for (int i = 0; i < MAX_ANGLE_LIST_SIZE; ++i) {
                    angleList.add(angle);
                }
            }
            angleList.add(angle);
            while (angleList.size() > MAX_ANGLE_LIST_SIZE) {
                angleList.remove(0);
            }
        } else {
            telem.addLine("No blocks detected.");
            follower.setTeleOpMovementVectors(0, 0, 0.15);
            angleList.clear();
        }

        intakePose.loop();
    }
}
