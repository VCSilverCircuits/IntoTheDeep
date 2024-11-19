package vcsc.redesign.states;

public class PoweredPIDFState extends State {
    private double power;
    private double targetPosition;

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
        notifyActuators();
    }

    public double getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
        notifyActuators();
    }
}
