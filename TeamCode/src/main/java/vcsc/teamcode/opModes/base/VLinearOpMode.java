package vcsc.teamcode.opModes.base;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.robot.DeepRobot;

public class VLinearOpMode extends LinearOpMode {
    protected MultipleTelemetry mt;
    DeepRobot r;

    @Override
    public void runOpMode() {
        GlobalTelemetry.init(telemetry);
        mt = new MultipleTelemetry(telemetry, GlobalTelemetry.getInstance());
    }
}
