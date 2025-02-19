package vcsc.core.abstracts.action;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.PathChain;

import java.util.ArrayDeque;
import java.util.function.Supplier;

import vcsc.core.GlobalTelemetry;

public class ActionBuilder implements Action {
    // TODO: Change this to a list
    ArrayDeque<Action> actionQueue = new ArrayDeque<>();

    Follower follower;

    public ActionBuilder() {

    }

    public ActionBuilder(Action action) {
        actionQueue.add(action);
    }

    public ActionBuilder then(Action action) {
        actionQueue.add(action);
        return this;
    }

    public ActionBuilder then(Action... actions) {
        ParallelAction parallelAction = new ParallelAction(actions);
        actionQueue.add(parallelAction);
        return this;
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }

    public ActionBuilder thenFollowPath(PathChain path, boolean holdEnd, Supplier<Boolean> endCondition) {
        actionQueue.add(new PathAction(this.follower, path, holdEnd, endCondition));
        return this;
    }

    public ActionBuilder thenFollowPath(PathChain path, Supplier<Boolean> endCondition) {
        actionQueue.add(new PathAction(this.follower, path, endCondition));
        return this;
    }

    public ActionBuilder thenFollowPath(PathChain path, boolean holdEnd) {
        actionQueue.add(new PathAction(this.follower, path, holdEnd));
        return this;
    }

    public ActionBuilder thenFollowPath(PathChain path) {
        actionQueue.add(new PathAction(this.follower, path));
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
