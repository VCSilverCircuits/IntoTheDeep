package vcsc.teamcode.component.arm.rot;

import static vcsc.teamcode.component.GlobalConfig.TPR;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.actuator.PoweredPIDFActuator;
import vcsc.core.util.DcMotorGroup;

public class ArmRotActuator extends PoweredPIDFActuator {
    // Three 4:1 ultraplanetary gearboxes
    public static final double MOTOR_GEAR_RATIO = 3.61 * 3.61 * 3.61;
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
    public double getPosition() {
        return -motors.getCurrentPosition();
    }

    public void reset() {
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        controller.setSetPoint(0);
    }

    @Override
    public void loop() {
        super.loop();
//        if (getPosition() < 0) {
//            motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        }
    }

    @Override
    public double getCurrent() {
        return motors.getCurrent(CurrentUnit.MILLIAMPS);
    }

    @Override
    protected void loopPower() {
        motors.setPower(power);
        controller.setSetPoint(getPosition());
    }

    @Override
    protected void loopPID() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        double error = controller.getSetPoint() - getPosition();
        double baseOutput = controller.calculate(getPosition());
        double expFactor = Math.exp(-Math.pow(error / 100.0, 2)); // Gaussian-like scaling
        double outputPower = baseOutput * (1 - expFactor);
        outputPower = Math.min(Math.abs(outputPower), 0.75) * Math.signum(outputPower);
        telemetry.addData("Run Position", controller.getSetPoint());
        telemetry.addData("At Position", controller.atSetPoint());
        telemetry.addData("Output Power", outputPower);
        telemetry.addData("Current position", getPosition());
        motors.setPower(outputPower);
    }
}
