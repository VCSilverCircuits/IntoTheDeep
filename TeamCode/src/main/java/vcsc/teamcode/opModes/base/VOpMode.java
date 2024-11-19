package vcsc.teamcode.opModes.base;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import roadrunner.MecanumDrive;
import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.armOld.asy.Arm;
import vcsc.teamcode.armOld.cmp.ArmExtension;
import vcsc.teamcode.armOld.cmp.ArmRotation;
import vcsc.teamcode.robot.DeepRobot;

public class VOpMode extends OpMode {
    protected DeepRobot r;
    protected MultipleTelemetry mt;

    @Override
    public void init() {
        GlobalTelemetry.init(telemetry);
        ArmRotation rotation = new ArmRotation(hardwareMap);
        ArmExtension extension = new ArmExtension(hardwareMap);
        Arm arm = new Arm(rotation, extension);
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        r = new DeepRobot(arm, drive);
        mt = new MultipleTelemetry(telemetry, GlobalTelemetry.getInstance());
    }

    @Override
    public void loop() {
        r.loop();
        mt.update();
    }
}
