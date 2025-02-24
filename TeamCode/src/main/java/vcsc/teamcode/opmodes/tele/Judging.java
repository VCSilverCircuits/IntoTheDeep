package vcsc.teamcode.opmodes.tele;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.opmodes.base.BaseOpMode;

@Disabled
@TeleOp(name = "Judging", group = "Main")
public class Judging extends BaseOpMode {
    double wristRotateSpeed = 0.005;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();

    }

    @Override
    public void loop() {
        super.loop();
        // Rotation of wrist
        double newPivot = wristState.getPivot() + gamepad2.right_stick_x * wristRotateSpeed;
        newPivot = Math.min(Math.max(newPivot, WristPivotPose.MIN.getPosition()), WristPivotPose.MAX.getPosition());
        wristState.setPivot(newPivot);
    }
}


