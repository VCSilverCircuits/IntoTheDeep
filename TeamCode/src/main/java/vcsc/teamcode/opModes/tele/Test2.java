package vcsc.teamcode.opModes.tele;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import roadrunner.MecanumDrive;
import vcsc.teamcode.armOld.asy.Arm;
import vcsc.teamcode.opModes.base.VOpMode;
import vcsc.teamcode.robot.DeepRobot;

@TeleOp(name = "Test2", group = "Test")
public class Test2 extends VOpMode {
    MecanumDrive drive;
    Arm arm;

    @Override
    public void init() {
        super.init();
        r = new DeepRobot(hardwareMap, new Pose2d(0, 0, 0));
        arm = r.getArm();
    }

    @Override
    public void loop() {
        super.loop();
        arm.setExtensionPower(-gamepad1.right_stick_y);
        arm.setRotationPower(-gamepad1.right_stick_x);
        mt.addData("Rotation", arm.getRotation());
        mt.addData("Extension", arm.getExtension());
        mt.update();
    }
}
