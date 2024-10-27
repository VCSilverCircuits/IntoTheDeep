package vcsc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
public class Hooks {
    public static double posUp = 1;
    public static double posDown = 0.5;
    ServoImplEx hook1;
    ServoImplEx hook2;
    boolean up = false;

    public Hooks(HardwareMap hardwareMap) {
        hook1 = hardwareMap.get(ServoImplEx.class, "hook1");
        hook2 = hardwareMap.get(ServoImplEx.class, "hook2");
    }

    public void raise() {
        hook1.setPosition(posUp);
        hook2.setPosition(posDown);
    }

    public void lower() {
        hook1.setPosition(posDown);
        hook2.setPosition(posUp);
    }

    public void toggle() {
        if (up) {
            lower();
        } else {
            raise();
        }
        up = !up;
    }
}
