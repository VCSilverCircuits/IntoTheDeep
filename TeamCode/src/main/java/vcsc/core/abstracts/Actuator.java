package vcsc.core.abstracts;

public abstract class Actuator {
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

    public abstract void updateState(State newState);
}
