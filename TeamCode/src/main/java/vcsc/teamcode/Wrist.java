package vcsc.teamcode;

import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Wrist {
    ServoImplEx servo;
    static double upPosition = 0.5;
    static double downPosition = 0.1;
    public void setAngle(double angle) {

    }
    public Wrist(ServoImplEx servo) {
        this.servo = servo;
    }
    public void open() {
        servo.setPosition(upPosition);
    }
    public void close() {
        servo.setPosition(downPosition);
    }
}