package vcsc.core.abstracts;

import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;


public class ActionManager {
    private final ArrayList<Action> _actions = new ArrayList<Action>();
    // Binding map from gamepad buttons to actions
    private final HashMap<GamepadButton, Action> _bindings = new HashMap<>();
    private final boolean _running = false;
    private final PriorityQueue<Action> _actionQueue = new PriorityQueue<>();

    public void loop(Gamepad gamepad) {
        for (GamepadButton btn : _bindings.keySet()) {
            if (btn.get()) {
                _actionQueue.add(_bindings.get(btn));
            }
        }
    }

    public void bind(Action action, GamepadButton btn) {
        _bindings.put(btn, action);
    }

}
