package vcsc.teamcode.component.camera;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;
import vcsc.teamcode.abstracts.Block;

public class Camera extends Actuator {
    private final Limelight3A limelight;

    public Camera(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    @Override
    public void loop() {

    }

    public Block getBlock() {
        LLResult result = limelight.getLatestResult();
        if (result == null) {
            return null;
        }
        List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
        for (LLResultTypes.ColorResult cr : colorResults) {
            return new Block(Block.COLOR.BLUE, cr.getTargetXPixels(), cr.getTargetYPixels(), cr);
        }
        return null;
    }

    @Override
    public void updateState(State newState) {

    }
}
