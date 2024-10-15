package vcsc.teamcode;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Claw {
    ServoImplEx servo;
    static double openPosition = 0.5;
    static double closedPosition = 0.1;

    public Claw(ServoImplEx servo) {
        this.servo = servo;
    }
    public void open() {
        servo.setPosition(openPosition);
    }
    public void close() {
        servo.setPosition(closedPosition);
    }
}