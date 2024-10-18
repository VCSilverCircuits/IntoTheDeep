package vcsc.core.component;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Component {

    void init(final HardwareMap hMap);

    void update();
}
