package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.DebugConstants;
import vcsc.teamcode.actions.BasketPose;
import vcsc.teamcode.component.arm.elbow.ElbowActuator;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtActuator;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotActuator;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawActuator;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristActuator;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristState;

@TeleOp(group = "Testing", name = "SetPoses")
public class TestTele extends OpMode {
    ArmRotState rotState;
    ArmRotActuator rotActuator;

    ArmExtState extState;
    ArmExtActuator extActuator;

    ClawState clawState;
    ClawActuator clawActuator;

    ElbowState elbowState;
    ElbowActuator elbowActuator;

    WristState wristState;
    WristActuator wristActuator;

    BasketPose basketPose;

    boolean debounceA = false;
    boolean tilt = false;

    @Override
    public void init() {
        GlobalTelemetry.init(telemetry);
        rotState = new ArmRotState();
        rotActuator = new ArmRotActuator(hardwareMap, DebugConstants.rotCoeffs);
        rotState.registerActuator(rotActuator);

        extState = new ArmExtState();
        extActuator = new ArmExtActuator(hardwareMap, DebugConstants.extCoeffs);
        extState.registerActuator(extActuator);


        clawState = new ClawState();
        clawActuator = new ClawActuator(hardwareMap.get(ServoImplEx.class, "claw"));
        clawState.registerActuator(clawActuator);

        elbowState = new ElbowState();
        elbowActuator = new ElbowActuator(hardwareMap.get(ServoImplEx.class, "elbow"));
        elbowState.registerActuator(elbowActuator);

        wristState = new WristState();
        wristActuator = new WristActuator(hardwareMap);
        wristState.registerActuator(wristActuator);

        basketPose = new BasketPose(rotState, extState, elbowState, wristState);
    }

    @Override
    public void loop() {
//        if (gamepad1.a) {
//            basketPose.start();
//        }

        if (gamepad1.a) {
            wristState.setPivotPose(WristPivotPose.TILT);
        } else {
            wristState.setPivotPose(WristPivotPose.REVERSE);
        }
        // just testing if this will be useful
        if (gamepad1.right_bumper) {
            clawState.close();
        }
        if (gamepad1.left_bumper) {
            clawState.open();
        }

        clawState.setPosition(gamepad1.right_trigger);

//        rotState.setAngle(DebugConstants.armRot);
//        extState.setExtensionLength(DebugConstants.armExt);
        elbowState.setPosition(DebugConstants.elbow);
        //wristState.setPivot(DebugConstants.wristPivot);

        wristState.setRot(DebugConstants.wristRot);


        rotActuator.setPIDFCoefficients(DebugConstants.rotCoeffs);
        extActuator.setPIDFCoefficients(DebugConstants.extCoeffs);

//        basketPose.loop();
//        rotActuator.loop();
//        extActuator.loop();
        clawActuator.loop();
        elbowActuator.loop();
        wristActuator.loop();
    }
}
