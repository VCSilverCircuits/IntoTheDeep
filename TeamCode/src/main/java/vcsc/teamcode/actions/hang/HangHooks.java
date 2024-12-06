package vcsc.teamcode.actions.hang;


import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.hooks.HookState;

public class HangHooks implements Action {
    HookState hookState;

    public HangHooks(HookState hookState) {
        super();
        this.hookState = hookState;
    }

    @Override
    public void start() {
        hookState.hang();
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
