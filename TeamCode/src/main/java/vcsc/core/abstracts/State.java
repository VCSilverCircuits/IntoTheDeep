package vcsc.core.abstracts;

import java.util.ArrayList;

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
