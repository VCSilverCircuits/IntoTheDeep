package vcsc.core.abstracts.action;

import java.util.ArrayList;
import java.util.Arrays;

public class ParallelAction implements Action {
    private final ArrayList<Action> _actions = new ArrayList<Action>();

    public ParallelAction(Action... actions) {
        _actions.addAll(Arrays.asList(actions));
    }

    @Override
    public void start() {
        for (Action action : _actions) {
            action.start();
        }
    }

    @Override
    public void loop() {
        for (Action action : _actions) {
            action.loop();
        }
    }

    @Override
    public boolean isFinished() {
        for (Action action : _actions) {
            if (!action.isFinished()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void cancel() {
        for (Action action : _actions) {
            action.cancel();
        }
    }
}
