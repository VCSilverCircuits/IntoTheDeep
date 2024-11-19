package vcsc.redesign.states;


public class ClawState extends State {
    double position;
    public ClawState() {
        super();
    }

    public void setPosition(double position) {
        this.position = position;
        notifyActuators();
    }

    public double getPosition() {
        return position;
    }
}
