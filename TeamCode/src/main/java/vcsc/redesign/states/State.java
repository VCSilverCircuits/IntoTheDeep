package vcsc.redesign.states;

import java.util.ArrayList;

import vcsc.redesign.actuators.Actuator;

public class State {
    ArrayList<Actuator<State>> actuators = new ArrayList<>();
    public void registerActuator(Actuator<State> actuator) {
        actuators.add(actuator);
    }

    public void notifyActuators() {
        for (Actuator<State> actuator : actuators) {
            actuator.updateState(this);
        }
    }
}
