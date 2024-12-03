package vcsc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import roadrunner.MecanumDrive;
import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.component.LeftHook.LHActuator;
import vcsc.teamcode.component.LeftHook.LHState;
import vcsc.teamcode.component.arm.rot.ArmRotActuator;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawActuator;
import vcsc.teamcode.component.claw.ClawState;

@Autonomous(group = "Testing", name = "HangTest")
class HangTestRevised extends OpMode {
    MecanumDrive drive;
    ArmRotState armRotState;
    ArmRotActuator armRotActuator;
    LHActuator lhActuator;
    LHState lhState;
    boolean debounceA = false;
    boolean debounceB = false;
    boolean hang = false;
    boolean rotate = false;
    private LHState LHState;
    private LHActuator LHActuator;

    @Override
    public void init() {
        GlobalTelemetry.init(telemetry);
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        armRotState = new ArmRotState();
        armRotActuator = new ArmRotActuator(hardwareMap, new PIDFCoefficients(0.01, 0, 0, 0));
        armRotState.registerActuator(armRotActuator);
        LHState = new LHState();
        LHActuator = new LHActuator(hardwareMap.get(ServoImplEx.class, "leftHook"));
        LHState.registerActuator(LHActuator);


    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            if (!debounceA) {
                hang = !hang;
            }
            debounceA = true;
        } else {
            debounceA = false;
        }

        if (gamepad1.b) {
            if (!debounceB) {
                rotate = !rotate;
            }
            debounceB = true;
        } else {
            debounceB = false;
        }

        if (rotate) {
            armRotState.setAngle(0);
        } else {
            armRotState.setAngle(70);
        }

        if (hang) {
            hookRight.setPosition(0.55);
            hookLeft.setPosition(0.5);
        } else {
            hookRight.setPosition(0);
            hookLeft.setPosition(1);
        }

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        drive.updatePoseEstimate();
        armRotActuator.loop();

    }
}
