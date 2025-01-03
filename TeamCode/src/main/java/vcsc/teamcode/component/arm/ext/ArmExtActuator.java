package vcsc.teamcode.component.arm.ext;

import static vcsc.teamcode.component.GlobalConfig.TPR;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.actuator.PoweredPIDFActuator;
import vcsc.core.util.DcMotorGroup;

public class ArmExtActuator extends PoweredPIDFActuator {
    // Three 5:1 ultraplanetary gearbox
    public static final double MOTOR_GEAR_RATIO = 2.89 * 2.89;
    // Gear ratio of driven gears
    public static final double DRIVE_GEAR_RATIO = 60.0 / 56.0;

    // 20 mm pulley
    public static final double PULLEY_DIAMETER = 20;
    public static final double CM_PER_TICK = PULLEY_DIAMETER * Math.PI / (10.0 * DRIVE_GEAR_RATIO * MOTOR_GEAR_RATIO * TPR);
    DcMotorGroup motors;

    public ArmExtActuator(HardwareMap hardwareMap, PIDFCoefficients coefficients) {
        super(coefficients);
        DcMotorEx extension1 = hardwareMap.get(DcMotorEx.class, "extension1");
        DcMotorEx extension2 = hardwareMap.get(DcMotorEx.class, "extension2");
        extension2.setDirection(DcMotorSimple.Direction.REVERSE);
        motors = new DcMotorGroup(extension1, extension2);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    protected void loopPower() {
        motors.setPower(power);
    }

    @Override
    protected void loopPID() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        // NOTE: Encoders for extension run backwards
        double outputPower = controller.calculate(getPosition());
        telemetry.addData("Run Position", controller.getSetPoint());
        telemetry.addData("At Position", controller.atSetPoint());
        telemetry.addData("Output Power", outputPower);
        telemetry.addData("Current position", getPosition());
        motors.setPower(Math.min(Math.abs(outputPower), 0.75) * Math.signum(outputPower));
    }

    @Override
    public double getPosition() {
        return -motors.getCurrentPosition();
    }
}
