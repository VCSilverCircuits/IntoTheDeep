package vcsc.redesign.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.redesign.actuators.ClawActuator;
import vcsc.redesign.mock.MockServo;
import vcsc.redesign.states.ClawState;

@TeleOp(name = "Testing", group = "Testing")
public class Testing extends OpMode {
    ClawState clawState;
    ClawActuator clawActuator;
    MockServo clawServo;

    public static void main(String[] args) {
        Testing testing = new Testing();
        testing.init();
        do {
            testing.loop();
        } while (testing.clawState.getPosition() != 1);
    }

    @Override
    public void init() {
        clawServo = new MockServo(System.currentTimeMillis());
        clawServo.forcePosition(0.8);
        clawState = new ClawState();
        clawState.setPosition(0);
        clawActuator = new ClawActuator(clawServo);

    }

    @Override
    public void loop() {
        clawServo.loop(System.currentTimeMillis());
        clawActuator.updateState(clawState);
        clawActuator.loop();
        System.out.println("State: " + clawState.getPosition());
        System.out.println("Servo: " + clawServo.getPosition());
    }
}
