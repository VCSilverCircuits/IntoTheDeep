package vcsc.core.abstracts;

public interface Action {
    // Get ready
    void init();

    // Start running
    void start();

    // Perform updates if needed
    void loop();

    // Check if finished
    boolean isFinished();

    // Clean up when stopped
    void stop();
}
