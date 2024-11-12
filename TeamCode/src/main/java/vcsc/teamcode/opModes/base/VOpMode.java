package vcsc.teamcode.opModes.base;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.robot.DeepRobot;

public class VOpMode extends OpMode {
    protected DeepRobot r;
    protected MultipleTelemetry mt;

    @Override
    public void init() {
        GlobalTelemetry.init(telemetry);
        mt = new MultipleTelemetry(telemetry, GlobalTelemetry.getInstance());
    }

    @Override
    public void loop() {
        r.loop();
        mt.update();
    }
}
