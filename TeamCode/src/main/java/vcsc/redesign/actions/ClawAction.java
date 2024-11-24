package vcsc.redesign.actions;

import com.qualcomm.robotcore.hardware.Servo;

import vcsc.redesign.actuators.ClawActuator;

public class ClawAction extends ClawActuator {
    double targetPosition = 0;
    double position = 0;
    double min = 0;
    double max = 0;
    double speed = 0.5;

    public ClawAction(Servo clawServo) {
        super(clawServo);
    }

    public void setSpeed(double speed){
        // I dont know what im putting here yet lol
    }

    public void forcePosition(double position){
        this.position = position;
        targetPosition = position;
    }

    public void scaleRange(double min, double max) {
        this.max = max;
        this.min = min;
    }

    public void close() {
        double minPosition = Servo.MIN_POSITION;
    }
    public void open() {
        double maxPosition = Servo.MAX_POSITION;
    }
}
