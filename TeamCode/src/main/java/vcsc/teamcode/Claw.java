package vcsc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Claw {
    static double openPosition = 0.5;
    static double closedPosition = 0.1;
    ServoImplEx servo;

    public Claw(HardwareMap hardwareMap) {
        this.servo = hardwareMap.get(ServoImplEx.class, "claw");
    }

    public void open() {
        servo.setPosition(openPosition);
    }

    public void close() {
        servo.setPosition(closedPosition);
    }
}