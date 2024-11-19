package vcsc.core.bx;

import vcsc.core.ButtonBinding;
import vcsc.core.hardware.Robot;

public interface Behavior {
    public void init(Robot robot);
    public void init_loop();
    public void start();
    public void loop();
    public void stop();
    public boolean alive();
    public ButtonBinding getPreferredBinding();
}
