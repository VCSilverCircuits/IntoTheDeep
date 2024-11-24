package vcsc.redesign.states;


public abstract class ClawState extends State {
    double position = 0;
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
