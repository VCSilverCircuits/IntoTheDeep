package vcsc.core.abstracts.action;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import java.util.ArrayDeque;

import vcsc.core.GlobalTelemetry;

public class ActionBuilder implements Action {
    ArrayDeque<Action> actionQueue = new ArrayDeque<>();

    public ActionBuilder(Action action) {
        actionQueue.add(action);
    }

    public ActionBuilder then(Action action) {
        actionQueue.add(action);
        return this;
    }

    private void startNextAction() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        Action nextAction = actionQueue.peek();
        if (nextAction != null) {
            nextAction.start();
            telemetry.addLine("Starting next action.");
        }
    }

    public void start() {
        startNextAction();
    }

    public void loop() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addData("Remaining actions", actionQueue.size());
        Action currentAction = actionQueue.peek();
        if (currentAction != null) {
            if (currentAction.isFinished()) {
                actionQueue.poll();
                startNextAction();
            } else {
                currentAction.loop();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return actionQueue.isEmpty();
    }

    public void cancel() {
        Action currentAction = actionQueue.poll();
        if (currentAction != null) {
            currentAction.cancel();
        }
        actionQueue.clear();
    }
}
