package vcsc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.teamcode.actions.IntakePose;
import vcsc.teamcode.component.arm.ext.ArmExtActuator;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotActuator;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawActuator;
import vcsc.teamcode.component.claw.ClawState;

public class TestTele extends OpMode {
    ArmRotState rotState;
    ArmRotActuator rotActuator;

    ArmExtState extState;
    ArmExtActuator extActuator;

    ClawState clawState;
    ClawActuator clawActuator;

    IntakePose intakePose;

    @Override
    public void init() {
        rotState = new ArmRotState();
        rotActuator = new ArmRotActuator(hardwareMap, new PIDFCoefficients(0, 0, 0, 0));
        rotState.registerActuator(rotActuator);

        extState = new ArmExtState();
        extActuator = new ArmExtActuator(hardwareMap, new PIDFCoefficients(0, 0, 0, 0));
        extState.registerActuator(extActuator);


        clawState = new ClawState();
        clawActuator = new ClawActuator(hardwareMap.get(ServoImplEx.class, "claw"));
        clawState.registerActuator(clawActuator);

        intakePose = new IntakePose(rotState, extState, clawState);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            intakePose.start();
        }

        intakePose.loop();
        rotActuator.loop();
        extActuator.loop();
        clawActuator.loop();
    }
}
