package vcsc.core.abstracts.state;

import java.util.ArrayList;

import vcsc.core.abstracts.actuator.Actuator;

public abstract class State {
    ArrayList<Actuator> actuators = new ArrayList<>();

    public boolean actuatorsInAction() {
        for (Actuator actuator : actuators) {
            if (actuator.inAction())
                return true;
        }
        return false;
    }

    public void registerActuator(Actuator actuator) {
        actuators.add(actuator);
    }

    public void notifyActuators() {
        for (Actuator actuator : actuators) {
            actuator.updateState(this);
        }
    }
}
