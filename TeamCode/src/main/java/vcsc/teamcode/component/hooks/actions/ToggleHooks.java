package vcsc.teamcode.component.hooks.actions;


import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.hooks.HookState;

public class ToggleHooks implements Action {
    HookState hookState;

    public ToggleHooks(HookState hookState) {
        super();
        this.hookState = hookState;
    }

    @Override
    public void start() {
        if (hookState.isHang()) {
            hookState.open();
        } else {
            hookState.hang();
        }
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void cancel() {

    }
}
