package vcsc.teamcode.opmodes.base;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.pedropathing.follower.Follower;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;
import roadrunner.MecanumDrive;
import vcsc.core.GlobalTelemetry;
import vcsc.core.util.GamepadWrapper;
import vcsc.teamcode.DebugConstants;
import vcsc.teamcode.component.DistanceSensors;
import vcsc.teamcode.component.arm.elbow.ElbowActuator;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtActuator;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotActuator;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.camera.Camera;
import vcsc.teamcode.component.claw.ClawActuator;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.hooks.HookActuator;
import vcsc.teamcode.component.hooks.HookState;
import vcsc.teamcode.component.wrist.WristActuator;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.component.wrist.WristState;

public class BaseOpMode extends OpMode {
    protected ArmRotState rotState;
    protected ArmExtState extState;
    protected ClawState clawState;
    protected ElbowState elbowState;
    protected WristState wristState;
    protected MecanumDrive drive;
    protected Follower follower;

    protected GamepadWrapper gw1, gw2;

    protected ElapsedTime matchTimer;
    protected HookState hookState;
    protected Camera camera;
    protected MultipleTelemetry telem;
    protected DistanceSensors distanceSensors;
    // NOTE: DO NOT LEAVE THIS AS PROTECTED
    protected ClawActuator clawActuator;
    // NOTE: DO NOT LEAVE THIS AS PROTECTED
    protected ArmExtActuator extActuator;
    ArmRotActuator rotActuator;
    ElbowActuator elbowActuator;
    WristActuator wristActuator;
    HookActuator hookActuator;

    @Override
    public void init() {
        GlobalTelemetry.init(telemetry);
        telem = GlobalTelemetry.getInstance();
        rotState = new ArmRotState();
        rotActuator = new ArmRotActuator(hardwareMap, DebugConstants.rotCoeffs);
        rotState.registerActuator(rotActuator);

        extState = new ArmExtState();
        extActuator = new ArmExtActuator(hardwareMap, DebugConstants.extCoeffs);
        extState.registerActuator(extActuator);

        clawState = new ClawState();
        clawActuator = new ClawActuator(hardwareMap);
        clawState.registerActuator(clawActuator);

        elbowState = new ElbowState();
        elbowActuator = new ElbowActuator(hardwareMap.get(ServoImplEx.class, "elbow"));
        elbowState.registerActuator(elbowActuator);

        wristState = new WristState();
        wristActuator = new WristActuator(hardwareMap);
        wristState.registerActuator(wristActuator);

        camera = new Camera(hardwareMap);

        hookState = new HookState();
        hookActuator = new HookActuator(
                hardwareMap.get(ServoImplEx.class, "hookLeft"),
                hardwareMap.get(ServoImplEx.class, "hookRight")
        );
        hookState.registerActuator(hookActuator);

        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        distanceSensors = new DistanceSensors(hardwareMap);

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);

        gw1 = new GamepadWrapper();
        gw2 = new GamepadWrapper();

        matchTimer = new ElapsedTime();
    }

    @Override
    public void start() {
        matchTimer.reset();
        wristState.setPose(WristPose.STOW);
        elbowState.setPose(ElbowPose.STOW);
        clawState.close();
    //    rotActuator.setMaxSpeed(0.8);
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        rotActuator.loop();
        extActuator.loop();
        clawActuator.loop();
        elbowActuator.loop();
        wristActuator.loop();
        hookActuator.loop();
        camera.loop();

        gw1.loop(gamepad1);
        gw2.loop(gamepad2);

//        drive.updatePoseEstimate();
        follower.update();
        telem.update();
    }
}
