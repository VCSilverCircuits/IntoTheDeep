package vcsc.core.util;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import vcsc.core.abstracts.action.Action;

public class GamepadWrapper {
    HashMap<GamepadButton, Action> actionMap;
    HashMap<GamepadButton, Boolean> actionDebounce;
    HashMap<GamepadButton, ArrayList<Runnable>> runnableMap;
    ArrayList<Action> runningActions;


    public GamepadWrapper() {
        runningActions = new ArrayList<>();
        actionMap = new HashMap<>();
        actionDebounce = new HashMap<>();
        runnableMap = new HashMap<>();
        for (GamepadButton btn : GamepadButton.values()) {
            actionDebounce.put(btn, false);
        }
    }

    public void bindButton(GamepadButton btn, Action act) {
        actionMap.put(btn, act);
    }

    private void callActions(GamepadButton btn) {
        Action act = actionMap.get(btn);
        if (act != null) {
            act.start();
            runningActions.add(act);

        }
    }

    public void callRunnables(GamepadButton btn) {
        ArrayList<Runnable> runnables = runnableMap.get(btn);
        if (runnables != null) {
            for (Runnable runnable : runnables) {
                runnable.run();
            }
        }
    }

    private boolean isDebounced(GamepadButton btn) {
        return Boolean.TRUE.equals(actionDebounce.get(btn));
    }

    private void debounce(GamepadButton btn) {
        actionDebounce.put(btn, true);
    }

    private void resetActions(GamepadButton btn) {
        actionDebounce.put(btn, false);
    }

    private void linkButton(boolean boolBtn, GamepadButton btn) {
        if (boolBtn && !isDebounced(btn)) {
            callActions(btn);
            callRunnables(btn);
            debounce(btn);
        } else {
            resetActions(btn);
        }
    }

    public void bindRunnable(GamepadButton btn, Runnable runnable) {
        if (!runnableMap.containsKey(btn)) {
            runnableMap.put(btn, new ArrayList<>());
        }
        Objects.requireNonNull(runnableMap.get(btn)).add(runnable);
    }

    public void loop(Gamepad gamepad) {
        linkButton(gamepad.a, GamepadButton.A);
        linkButton(gamepad.b, GamepadButton.B);
        linkButton(gamepad.x, GamepadButton.X);
        linkButton(gamepad.y, GamepadButton.Y);
        linkButton(gamepad.dpad_up, GamepadButton.DPAD_UP);
        linkButton(gamepad.dpad_down, GamepadButton.DPAD_DOWN);
        linkButton(gamepad.dpad_left, GamepadButton.DPAD_LEFT);
        linkButton(gamepad.dpad_right, GamepadButton.DPAD_RIGHT);
        linkButton(gamepad.left_bumper, GamepadButton.LEFT_BUMPER);
        linkButton(gamepad.right_bumper, GamepadButton.RIGHT_BUMPER);
        linkButton(gamepad.left_trigger > 0, GamepadButton.LEFT_TRIGGER);
        linkButton(gamepad.right_trigger > 0, GamepadButton.RIGHT_TRIGGER);
        linkButton(gamepad.left_stick_button, GamepadButton.LEFT_STICK_BUTTON);
        linkButton(gamepad.right_stick_button, GamepadButton.RIGHT_STICK_BUTTON);
        linkButton(gamepad.start, GamepadButton.START);
        linkButton(gamepad.back, GamepadButton.BACK);

        ArrayList<Action> actionsCopy = new ArrayList<>(runningActions);

        for (Action act : actionsCopy) {
            act.loop();
            if (act.isFinished()) {
                runningActions.remove(act);
            }
        }
    }
}
