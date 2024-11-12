package vcsc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import roadrunner.MecanumDrive;
import vcsc.core.hardware.Robot;
import vcsc.teamcode.arm.Arm;

public class DeepRobot extends Robot {
    Arm arm;
    MecanumDrive drive;

    public DeepRobot(HardwareMap hMap) {
        this(hMap, new Pose2d(0, 0, 0));
    }

    public DeepRobot(HardwareMap hMap, Pose2d pose) {
        super(hMap);
        arm = new Arm(hMap);
        drive = new MecanumDrive(hMap, pose);
        registerComponent(arm);
    }


    public MecanumDrive getDrive() {
        return drive;
    }

    public Arm getArm() {
        return arm;
    }

    @Override
    public void loop() {
        super.loop();
        drive.updatePoseEstimate();
    }
}
