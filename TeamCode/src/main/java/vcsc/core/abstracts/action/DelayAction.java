package vcsc.core.abstracts.action;

public class DelayAction implements Action {
    long startTime;
    long delay;
    boolean finished = false;

    public DelayAction(long delay) {
        this.delay = delay;
    }

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        if (System.currentTimeMillis() - startTime >= delay) {
            finished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void cancel() {
        finished = true;
    }
}
