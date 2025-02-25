package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawActuator;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.claw.actions.OpenClawAction;

public class AbstractTesting extends OpMode {
    ArmRotState armRotState;
    ClawState clawState;
    OpenClawAction openClaw;

    @Override
    public void init() {
        clawState = new ClawState();
        ClawActuator clawAct = new ClawActuator(hardwareMap);
        clawState.registerActuator(clawAct);

        openClaw = new OpenClawAction(clawState);
    }

    @Override
    public void loop() {
    }
}
