package vcsc.redesign.states;

public class ArmExtensionState extends PoweredPIDFState {
    public void setExtensionLength(double length) {
        // TODO: FIX THIS MATH THAT IS TOTALLY NOT LEGIT
        setTargetPosition(length * Math.PI);
    }

    public double getExtensionLength() {
        // TODO: FIX THIS MATH THAT IS TOTALLY NOT LEGIT
        return getTargetPosition() * Math.PI;
    }
}