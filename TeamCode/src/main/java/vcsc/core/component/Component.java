package vcsc.core.component;

import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class Component {

    public Component(final HardwareMap hMap) {

    }

    abstract public void loop();
}
