package vcsc.core;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class GlobalTelemetry {
    static MultipleTelemetry mt;

    public static void init(Telemetry telem) {
        GlobalTelemetry.mt = new MultipleTelemetry(telem, FtcDashboard.getInstance().getTelemetry());
    }

    public static MultipleTelemetry getInstance() {
        return GlobalTelemetry.mt;
    }
}
