package vcsc.core.component;

public interface Composable {
    public void init();
    public void init_loop();
    public void start();
    public void loop();
    public void stop();
}
