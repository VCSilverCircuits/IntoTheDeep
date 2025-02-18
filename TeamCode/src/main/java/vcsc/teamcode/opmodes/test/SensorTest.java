package vcsc.teamcode.opmodes.test;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import vcsc.teamcode.component.claw.ClawSensor;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "SensorTest", group = "Testing")
public class SensorTest extends BaseOpMode {
    ClawSensor cs;

    @Override
    public void init() {
        super.init();
        cs = clawActuator.clawSensor;
        cs.init();
        clawState.open();
    }

    @Override
    public void loop() {
        super.loop();
        cs.loop();
        telem.addData("Distance", cs.getDistance());

        NormalizedRGBA normalizedRGBA = cs.getNormalizedColor();
        float[] hsvValues = new float[3];

        Color.colorToHSV(normalizedRGBA.toColor(), hsvValues);

        telem.addData("Red", normalizedRGBA.red * 255);
        telem.addData("Green", normalizedRGBA.green * 255);
        telem.addData("Blue", normalizedRGBA.blue * 255);

        telem.addData("Hue", hsvValues[0]);
        telem.addData("Sat", hsvValues[1]);
        telem.addData("Val", hsvValues[2]);

        telem.addData("Over Block", cs.overBlock());
        telem.addData("Has Block", cs.hasBlock());

        telem.addLine("===================================");

        telem.addData("Distance Sensor Average", distanceSensors.getAverageDistance());
        telem.addData("Distance Sensor Left", distanceSensors.getLeftDistance());
        telem.addData("Distance Sensor Right", distanceSensors.getRightDistance());
        telem.addData("Distance Sensor Angle", distanceSensors.getAngle());

        telem.addLine("===================================");

        telem.addData("Arm ext touching", extActuator.isTouching());

        if (gamepad1.a) {
            clawState.open();
        } else if (cs.hasBlock()) {
            clawState.close();
        }
    }
}
