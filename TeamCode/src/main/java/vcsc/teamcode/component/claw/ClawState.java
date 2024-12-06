package vcsc.teamcode.component.claw;


import vcsc.core.abstracts.state.State;

public class ClawState extends State {
    double position = 0;

    public ClawState() {
        super();
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
        notifyActuators();
    }

    public void setPosition(ClawPose position) {
        this.position = position.getPos();
        notifyActuators();
    }

    public void open() {
        setPosition(ClawPose.OPEN);
    }

    public void close() {
        setPosition(ClawPose.CLOSED);
    }
}
