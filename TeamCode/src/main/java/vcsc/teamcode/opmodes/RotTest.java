package vcsc.teamcode.opmodes;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.component.arm.rot.ArmRotActuator;
import vcsc.teamcode.component.arm.rot.ArmRotState;

@Autonomous(group = "Testing", name = "RotTest")
public class RotTest extends OpMode {
    double position = 0.0;
    double speed = 0.3;//1.0 / 5000.0;
    ServoImplEx servo;
    ServoImplEx hookRight;
    ServoImplEx hookLeft;
    ServoImplEx claw;
    ArmRotState armRotState;
    ArmRotActuator armRotActuator;
    boolean debounce = false;
    boolean hang = false;

    @Override
    public void init() {
        GlobalTelemetry.init(telemetry);
//        servo = hardwareMap.get(ServoImplEx.class, "hookRight");
        hookRight = hardwareMap.get(ServoImplEx.class, "hookRight");
        hookLeft = hardwareMap.get(ServoImplEx.class, "hookLeft");
        claw = hardwareMap.get(ServoImplEx.class, "claw");
        armRotState = new ArmRotState();
        armRotActuator = new ArmRotActuator(hardwareMap, new PIDFCoefficients(0.01, 0, 0, 0));
        armRotState.registerActuator(armRotActuator);
    }

    @Override
    public void loop() {
        MultipleTelemetry tele = GlobalTelemetry.getInstance();
        speed -= gamepad1.left_stick_y / 1000.0;// 100000.0;
        position -= gamepad1.right_stick_y * speed;
        position = Math.min(position, 1.0);
        position = Math.max(position, 0.0);
        speed = Math.min(speed, 1); // 0.05);
        speed = Math.max(speed, 1.0 / 3000.0);

        claw.setPosition(0.4 + (0.55 - 0.4) * gamepad1.right_trigger);

        if (gamepad1.a) {
            if (!debounce) {
                hang = !hang;
            }
            debounce = true;
        } else {
            debounce = false;
        }

        if (hang) {
            hookRight.setPosition(0.55);
            hookLeft.setPosition(0.5);
        } else {
            hookRight.setPosition(0);
            hookLeft.setPosition(1);
        }

//        servo.setPosition(position);
//        armRotState.setAngle(180 * position);
        armRotState.setPower(-gamepad1.right_stick_y * speed);
        armRotActuator.loop();
//        tele.addData("Set angle", 180 * position);
//        tele.addData("Position", position);
        tele.addData("Speed", speed);
        tele.addData("Angle", armRotState.getAngle());
        telemetry.update();
    }
}
