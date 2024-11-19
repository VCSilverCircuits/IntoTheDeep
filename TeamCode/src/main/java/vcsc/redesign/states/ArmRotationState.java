package vcsc.redesign.states;

public class ArmRotationState extends PoweredPIDFState {
    public void setAngle(double length) {
        // TODO: FIX THIS MATH THAT IS TOTALLY NOT LEGIT
        setTargetPosition(length * Math.PI);
    }

    public double getAngle() {
        // TODO: FIX THIS MATH THAT IS TOTALLY NOT LEGIT
        return getTargetPosition() * Math.PI;
    }
}