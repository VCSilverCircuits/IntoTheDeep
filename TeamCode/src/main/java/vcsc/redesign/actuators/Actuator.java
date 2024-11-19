package vcsc.redesign.actuators;

import com.qualcomm.robotcore.hardware.HardwareMap;

import vcsc.redesign.states.State;

public abstract class Actuator<S extends State> {
    protected boolean _inAction;

    public void init() {

    }

    public abstract void loop();

    public boolean inAction() {
        return _inAction;
    }

    protected void setInAction(boolean inAction) {
        _inAction = inAction;
    }

    public abstract void updateState(S newState);
}
