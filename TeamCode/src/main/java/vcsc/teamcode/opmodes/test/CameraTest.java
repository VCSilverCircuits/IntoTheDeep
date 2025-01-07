package vcsc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.teamcode.abstracts.Block;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(group = "Test", name = "CameraTest")
public class CameraTest extends BaseOpMode {
    @Override
    public void loop() {
//        super.loop();
        camera.loop();
        Block block = camera.getBlock();
        if (block != null) {
            telem.addData("Block", block.getColor());
            telem.addData("X", block.getX());
            telem.addData("Y", block.getY());
            telem.addData("Pitch", block.getInnerResult().getTargetPoseCameraSpace().getOrientation().getPitch());
            telem.addData("Roll", block.getInnerResult().getTargetPoseCameraSpace().getOrientation().getRoll());
            telem.addData("Yaw", block.getInnerResult().getTargetPoseCameraSpace().getOrientation().getYaw());
        } else {
            telem.addLine("No blocks detected.");
        }
    }
}
