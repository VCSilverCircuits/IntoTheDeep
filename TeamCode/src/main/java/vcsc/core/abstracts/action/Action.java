package vcsc.core.abstracts.action;

public interface Action {
    // Get ready
    void init();

    // Start running
    void start();

    // Perform updates if needed
    boolean loop();

    // Check if finished
    boolean isFinished();

    // Clean up when stopped
    void stop();
}