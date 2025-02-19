package vcsc.teamcode.component.arm.ext;

import static vcsc.teamcode.component.GlobalConfig.TPR;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.actuator.PoweredPIDFActuator;
import vcsc.core.util.DcMotorGroup;

public class ArmExtActuator extends PoweredPIDFActuator {
    // Three 5:1 ultraplanetary gearbox
    public static final double MOTOR_GEAR_RATIO = 2.89 * 2.89; // 3.61
    // Gear ratio of driven gears
    public static final double DRIVE_GEAR_RATIO = 60.0 / 56.0;

    // 20 mm pulley
    public static final double PULLEY_DIAMETER = 30;
    public static final double CM_PER_TICK = PULLEY_DIAMETER * Math.PI / (10.0 * DRIVE_GEAR_RATIO * MOTOR_GEAR_RATIO * TPR);
    public static final double MAX_EXTENSION_POWER = 1.0;
    DcMotorGroup motors;
    TouchSensor touchSensor;

    public ArmExtActuator(HardwareMap hardwareMap, PIDFCoefficients coefficients) {
        super(coefficients);
        DcMotorEx extension1 = hardwareMap.get(DcMotorEx.class, "extension1");
        DcMotorEx extension2 = hardwareMap.get(DcMotorEx.class, "extension2");
        touchSensor = hardwareMap.get(TouchSensor.class, "slideLimitSensor");
        extension2.setDirection(DcMotorSimple.Direction.REVERSE);
        motors = new DcMotorGroup(extension1, extension2);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        super.loop();
        MultipleTelemetry telem = GlobalTelemetry.getInstance();
//        if (getPosition() < 0) {
//            motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        }
    }

    public boolean isTouching() {
        return touchSensor.isPressed();
    }

    @Override
    protected void loopPower() {
        motors.setPower(power);
    }

    public void reset() {
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        controller.setSetPoint(0);
    }

    @Override
    protected void loopPID() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        // NOTE: Encoders for extension run backwards
        double outputPower = controller.calculate(getPosition());
//        telemetry.addData("Run Position", controller.getSetPoint());
//        telemetry.addData("At Position", controller.atSetPoint());
//        telemetry.addData("Output Power", outputPower);
//        telemetry.addData("Current position", getPosition());
        motors.setPower(Math.min(Math.abs(outputPower), MAX_EXTENSION_POWER) * Math.signum(outputPower));
    }

    @Override
    public double getPosition() {
        return -motors.getCurrentPosition();
    }

    @Override
    public double getCurrent() {
        return motors.getCurrent(CurrentUnit.MILLIAMPS);
    }
}
