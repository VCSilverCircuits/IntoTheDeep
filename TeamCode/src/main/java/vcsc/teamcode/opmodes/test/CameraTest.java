package vcsc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.abstracts.Block;
import vcsc.teamcode.component.camera.Camera;

@TeleOp(group = "Test", name = "CameraTest")
public class CameraTest extends OpMode {
    Camera camera;
    MultipleTelemetry telem;

    @Override
    public void init() {
        camera = new Camera(hardwareMap);
        GlobalTelemetry.init(telemetry);
        telem = GlobalTelemetry.getInstance();
    }

    @Override
    public void loop() {
//        super.loop();
        camera.loop();
        Block block = camera.getBlock();
        if (block != null) {
            telem.addData("Block", block.getColor());
            telem.addData("X", block.getX());
            telem.addData("Y", block.getY());
            telem.addData("Angle", block.getAngle());
        } else {
            telem.addLine("No blocks detected.");
        }
        telem.update();
    }
}
