package vcsc.teamcode.robot;

import roadrunner.MecanumDrive;
import vcsc.core.hardware.Robot;
import vcsc.teamcode.armOld.asy.Arm;

public class DeepRobot extends Robot {
    Arm arm;
    MecanumDrive drive;

    public DeepRobot(Arm arm, MecanumDrive drive) {
        super();
        this.arm = arm;
        this.drive = drive;
        registerComponent(arm);
    }

    @Override
    public void loop() {
        super.loop();
        drive.updatePoseEstimate();
    }
}
