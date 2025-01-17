package vcsc.teamcode.opmodes.test;

import static vcsc.teamcode.DebugConstants.WALL_ELBOW;
import static vcsc.teamcode.DebugConstants.WALL_WRIST_ROT;
import static vcsc.teamcode.DebugConstants.armExt;
import static vcsc.teamcode.DebugConstants.armRot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.teamcode.actions.intake.Grab;
import vcsc.teamcode.actions.intake.IntakePoseWall;
import vcsc.teamcode.actions.intake.PreGrabPoseWall;
import vcsc.teamcode.actions.specimen.SpecimenPose;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "SpecimenTest", group = "Test")
public class SpecimenTest extends BaseOpMode {
    SpecimenPose specimenPose;
    PreGrabPoseWall preGrabPose;
    IntakePoseWall intakePoseWall;
    Grab grab;

    @Override
    public void init() {
        super.init();
        specimenPose = new SpecimenPose(rotState, extState, elbowState, wristState);
        preGrabPose = new PreGrabPoseWall(elbowState, wristState, clawState);
        intakePoseWall = new IntakePoseWall(rotState, extState, clawState, preGrabPose);
        grab = new Grab(elbowState, wristState, clawState);
        //gw1.bindButton(GamepadButton.A, intakePoseWall);
        //gw1.bindButton(GamepadButton.B, specimenPose);
        //gw1.bindButton(GamepadButton.X, grab);
    }

    @Override
    public void loop() {
        super.loop();
        rotState.setAngle(armRot);
        wristState.setRot(WALL_WRIST_ROT);
        wristState.setPivotPose(WristPivotPose.FORWARD);
        elbowState.setPosition(WALL_ELBOW);
        extState.setExtensionLength(armExt);
    }
}
