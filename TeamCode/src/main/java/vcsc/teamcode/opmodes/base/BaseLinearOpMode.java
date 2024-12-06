package vcsc.teamcode.opmodes.base;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import roadrunner.MecanumDrive;
import vcsc.core.GlobalTelemetry;
import vcsc.teamcode.DebugConstants;
import vcsc.teamcode.component.arm.elbow.ElbowActuator;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtActuator;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotActuator;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawActuator;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristActuator;
import vcsc.teamcode.component.wrist.WristState;

public class BaseLinearOpMode extends LinearOpMode {
    protected ArmRotState rotState;
    protected ArmExtState extState;
    protected ClawState clawState;
    protected ElbowState elbowState;
    protected WristState wristState;
    protected MecanumDrive drive;

    protected ElapsedTime matchTimer;

    ArmRotActuator rotActuator;
    ArmExtActuator extActuator;
    ClawActuator clawActuator;
    ElbowActuator elbowActuator;
    WristActuator wristActuator;

    @Override
    public void runOpMode() {
        GlobalTelemetry.init(telemetry);
        rotState = new ArmRotState();
        rotActuator = new ArmRotActuator(hardwareMap, DebugConstants.rotCoeffs);
        rotState.registerActuator(rotActuator);

        extState = new ArmExtState();
        extActuator = new ArmExtActuator(hardwareMap, DebugConstants.extCoeffs);
        extState.registerActuator(extActuator);

        clawState = new ClawState();
        clawActuator = new ClawActuator(hardwareMap.get(ServoImplEx.class, "claw"));
        clawState.registerActuator(clawActuator);

        elbowState = new ElbowState();
        elbowActuator = new ElbowActuator(hardwareMap.get(ServoImplEx.class, "elbow"));
        elbowState.registerActuator(elbowActuator);

        wristState = new WristState();
        wristActuator = new WristActuator(hardwareMap);
        wristState.registerActuator(wristActuator);

        matchTimer = new ElapsedTime();
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
    }

    protected void updateComponents() {
        rotActuator.loop();
        extActuator.loop();
        clawActuator.loop();
        elbowActuator.loop();
        wristActuator.loop();

        drive.updatePoseEstimate();
    }
}
