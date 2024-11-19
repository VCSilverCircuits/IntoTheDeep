package vcsc.core.component;

import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class Component implements Composable {

    public Component(final HardwareMap hMap) {

    }

    @Override
    public void init() {

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    abstract public void loop();
}
