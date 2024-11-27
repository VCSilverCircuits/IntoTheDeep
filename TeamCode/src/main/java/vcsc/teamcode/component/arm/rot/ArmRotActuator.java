package vcsc.teamcode.component.arm.rot;

import static vcsc.teamcode.component.GlobalConfig.TPR;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.PoweredPIDFActuator;
import vcsc.core.util.DcMotorGroup;

public class ArmRotActuator extends PoweredPIDFActuator {
    // Three 4:1 ultraplanetary gearboxes
    public static final double MOTOR_GEAR_RATIO = 4.0 * 4.0 * 4.0;
    // Gear ratio of driven gears
    public static final double DRIVE_GEAR_RATIO = 52.0 / 24.0;
    public static final double DEGREES_PER_TICK = 360.0 / (TPR * MOTOR_GEAR_RATIO * DRIVE_GEAR_RATIO);
    DcMotorGroup motors;

    public ArmRotActuator(HardwareMap hardwareMap, PIDFCoefficients coefficients) {
        super(coefficients);
        DcMotorEx rotation1 = hardwareMap.get(DcMotorEx.class, "rotation1");
        DcMotorEx rotation2 = hardwareMap.get(DcMotorEx.class, "rotation2");
        rotation1.setDirection(DcMotorSimple.Direction.REVERSE);
        motors = new DcMotorGroup(rotation1, rotation2);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    protected double getPosition() {
        return -motors.getCurrentPosition();
    }

    @Override
    protected void loopPower() {
        motors.setPower(power);
        controller.setSetPoint(getPosition());
    }

    @Override
    protected void loopPID() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        double outputPower = controller.calculate(getPosition());
        telemetry.addData("Run Position", controller.getSetPoint());
        telemetry.addData("Output Power", outputPower);
        telemetry.addData("Current position", getPosition());
        motors.setPower(outputPower);
    }
}
