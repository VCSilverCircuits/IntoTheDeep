package vcsc.redesign.actions;

import vcsc.redesign.states.ClawState;

public abstract class ClawAction extends ClawState {
    double targetPosition = 0;
    double position = 0;
    double min = 0;
    double max = 0;
    double speed = 0.5;


    @Override
    public void setPosition(double position) { targetPosition = position;
    }

    @Override
    public double getPosition() { return position; }

    public void forcePosition(double position){
        this.position = position;
        targetPosition = position;
    }

    public void scaleRange(double min, double max) {
        this.max = max;
        this.min = min;
    }

    public void close() {
        setPosition(0);
    }
    public void  open() {
        setPosition(1);
    }
}
