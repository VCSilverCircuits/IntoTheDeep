package vcsc.core.abstracts.state;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.actuator.PoweredPIDFActuator;

public class PoweredPIDFState extends State {
    private PoweredPIDFActuator primaryActuator;
    private double power;
    private double targetPosition;
    private double speed;

    public PoweredPIDFState() {
        super();
    }

    @Override
    public void registerActuator(Actuator actuator) {
        super.registerActuator(actuator);
        primaryActuator = (PoweredPIDFActuator) actuators.get(0);
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
        notifyActuators();
    }

    public void reset() {
        primaryActuator.reset();
    }

    public double getRealPosition() {
        return primaryActuator.getPosition();
    }

    public double getCurrent() {
        return primaryActuator.getCurrent();
    }

    public double getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(double targetPosition) {
        setTargetPosition(targetPosition, false);
    }

    public void setTargetPosition(double targetPosition, boolean suppressNotification) {
        this.targetPosition = targetPosition;
        if (!suppressNotification) {
            notifyActuators();
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}
