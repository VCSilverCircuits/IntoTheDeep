package vcsc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.actions.BasketPose;
import vcsc.teamcode.actions.IntakePose;
import vcsc.teamcode.component.arm.elbow.ElbowActuator;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtActuator;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotActuator;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawActuator;
import vcsc.teamcode.component.claw.ClawState;

@TeleOp(group = "Testing", name = "Basket")
public class TestTele extends OpMode {
    ArmRotState rotState;
    ArmRotActuator rotActuator;

    ArmExtState extState;
    ArmExtActuator extActuator;

    ClawState clawState;
    ClawActuator clawActuator;

    ElbowState elbowState;
    ElbowActuator elbowActuator;

    BasketPose basketPose;

    @Override
    public void init() {
        GlobalTelemetry.init(telemetry);
        rotState = new ArmRotState();
        rotActuator = new ArmRotActuator(hardwareMap, new PIDFCoefficients(0.01, 0, 0, 0));
        rotState.registerActuator(rotActuator);

        extState = new ArmExtState();
        extActuator = new ArmExtActuator(hardwareMap, new PIDFCoefficients(0.01, 0, 0, 0));
        extState.registerActuator(extActuator);


        clawState = new ClawState();
        clawActuator = new ClawActuator(hardwareMap.get(ServoImplEx.class, "claw"));
        clawState.registerActuator(clawActuator);

        elbowState = new ElbowState();
        elbowActuator = new ElbowActuator(hardwareMap.get(ServoImplEx.class, "elbow"));
        elbowState.registerActuator(elbowActuator);

        basketPose = new BasketPose(rotState, extState, elbowState);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            basketPose.start();
        }

        basketPose.loop();
        rotActuator.loop();
        extActuator.loop();
        clawActuator.loop();
        elbowActuator.loop();
    }
}
