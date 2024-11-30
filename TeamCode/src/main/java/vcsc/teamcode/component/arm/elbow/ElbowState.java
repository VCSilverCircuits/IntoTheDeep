package vcsc.teamcode.component.arm.elbow;

import vcsc.core.abstracts.state.State;

public class ElbowState extends State {
    double position;

    public ElbowState() {
        super();
        setPose(ElbowPose.STOW);
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
        notifyActuators();
    }

    public void setPose(ElbowPose position) {
        this.position = position.getPosition();
        notifyActuators();
    }
}

