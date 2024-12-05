package vcsc.core.abstracts.action;

public interface Action {
    // Start running
    void start();

    // Perform updates if needed
    void loop();

    // Check if finished
    boolean isFinished();

    void cancel();
}
