package vcsc.core.abstracts.action;

import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.PathChain;

import java.util.function.Supplier;

public class PathAction implements Action {
    Follower follower;
    PathChain path;
    boolean holdEnd = true;
    Supplier<Boolean> endCondition;

    public PathAction(Follower follower, PathChain pathChain) {
        this.follower = follower;
        this.path = pathChain;
        this.endCondition = () -> !follower.isBusy();
    }

    public PathAction(Follower follower, PathChain pathChain, boolean holdEnd) {
        this.follower = follower;
        this.path = pathChain;
        this.holdEnd = holdEnd;
    }

    public PathAction(Follower follower, PathChain pathChain, Supplier<Boolean> endCondition) {
        this.follower = follower;
        this.path = pathChain;
        this.endCondition = endCondition;
    }


    public PathAction(Follower follower, PathChain pathChain, boolean holdEnd, Supplier<Boolean> endCondition) {
        this.follower = follower;
        this.path = pathChain;
        this.holdEnd = holdEnd;
        this.endCondition = endCondition;
    }

    @Override
    public void start() {
        follower.followPath(path, holdEnd);
    }

    @Override
    public void loop() {
        follower.update();
        if (isFinished()) {
            cancel();
        }
    }

    @Override
    public boolean isFinished() {
        return endCondition.get();
    }

    @Override
    public void cancel() {
        follower.breakFollowing();
    }
}
