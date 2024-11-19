package vcsc.core.state;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

enum RunMode {
    POWER,
    PID;
}

public abstract class PoweredPIDState extends State {
    protected double power;
    double targetPosition;
    RunMode runMode;

    public void setPower(double power) {
        this.power = power;
        runMode = RunMode.POWER;
    }

    public double getPower() {return this.power; }

    public void setTarget(double target) {
        targetPosition = target;
    }

    public double getTarget(double target) {
        return targetPosition;
    }

    abstract public double getPosition();

    public void loop() {
        if (this.power == 0) {
            runMode = RunMode.PID;
        }
    }
}