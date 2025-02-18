package vcsc.teamcode.opmodes.base;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.GlobalTelemetry;
import vcsc.core.util.GamepadWrapper;
import vcsc.teamcode.DebugConstants;
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

public class BaseOpModeAuto extends OpMode {
    protected ArmRotState rotState;
    protected ArmExtState extState;
    protected ClawState clawState;
    protected ElbowState elbowState;
    protected WristState wristState;

    protected GamepadWrapper gw1, gw2;

    protected ElapsedTime matchTimer;
    protected HookState hookState;
    protected Camera camera;
    protected MultipleTelemetry telem;
    ArmRotActuator rotActuator;
    ArmExtActuator extActuator;
    ClawActuator clawActuator;
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

        gw1 = new GamepadWrapper();
        gw2 = new GamepadWrapper();

        matchTimer = new ElapsedTime();
    }

    @Override
    public void start() {
        matchTimer.reset();
        wristState.setPose(WristPose.STOW);
        elbowState.setPose(ElbowPose.STOW);
        rotActuator.setMaxSpeed(0.6);
        clawState.close();
    }

    @Override
    public void loop() {
        rotActuator.loop();
        extActuator.loop();
        clawActuator.loop();
        elbowActuator.loop();
        wristActuator.loop();
        hookActuator.loop();

        gw1.loop(gamepad1);
        gw2.loop(gamepad2);
        telem.update();
    }
}
