package vcsc.teamcode.armOld.claw.cmp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Claw {
    static double openPosition = 0.8;
    static double closedPosition = 0;
    ServoImplEx servo;

    public Claw(HardwareMap hardwareMap) {
        this.servo = hardwareMap.get(ServoImplEx.class, "claw");
    }

    public void setPosition(double position) {
        this.servo.setPosition(closedPosition + (openPosition - closedPosition) * position);
    }

    public void open() {
        servo.setPosition(openPosition);
    }

    public void close() {
        servo.setPosition(closedPosition);
    }
}