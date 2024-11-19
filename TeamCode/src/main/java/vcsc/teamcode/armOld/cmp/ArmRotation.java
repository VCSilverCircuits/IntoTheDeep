package vcsc.teamcode.armOld.cmp;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.core.component.PoweredPID;
import vcsc.core.hardware.DcMotorGroup;

public class ArmRotation extends PoweredPID {
    public static final double TPR = 28;
    public static final double GEAR_RATIO = 1 / (4.0 * 4.0 * 4.0) * 24.0 / 52;
    public static final double TICKS_PER_DEGREE = TPR * GEAR_RATIO / 360;

    public static final double MIN_ANGLE = 0;
    public static final double MAX_ANGLE = 90;

    public static final PIDFCoefficients ROTATION_COEFFS = new PIDFCoefficients(0.005, 0, 0.00035, 0);

    DcMotorGroup motors;

    public ArmRotation(HardwareMap hMap) {
        super(hMap);
        DcMotorEx rotation1 = hMap.get(DcMotorEx.class, "rotation1");
        DcMotorEx rotation2 = hMap.get(DcMotorEx.class, "rotation2");
        motors = new DcMotorGroup(rotation1, rotation2);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        controller = new PIDFController(ROTATION_COEFFS.p, ROTATION_COEFFS.i, ROTATION_COEFFS.d, ROTATION_COEFFS.f);
    }

    @Override
    public double getPosition() {
        return motors.getCurrentPosition();
    }

    public double getTargetAngle() {
        return controller.getSetPoint() / TICKS_PER_DEGREE;
    }

    public void setTargetAngle(double angle) {
        controller.setSetPoint(angle * TICKS_PER_DEGREE);
    }

    public double getAngle() {
        return getPosition() / TICKS_PER_DEGREE;
    }

    @Override
    public void loop() {
        double outputPower = controller.calculate(getPosition());
        if (power != 0) {
            outputPower = power;
            controller.setSetPoint(getPosition());
        }

        if (getAngle() < MIN_ANGLE) {
            outputPower = Math.max(0, outputPower);
        } else if (getAngle() > MAX_ANGLE) {
            outputPower = Math.min(0, outputPower);
        }

        if (controller.getSetPoint() < MIN_ANGLE) {
            setTargetAngle(MIN_ANGLE);
        } else if (controller.getSetPoint() > MAX_ANGLE) {
            setTargetAngle(MAX_ANGLE);
        }

        motors.setPower(outputPower);
    }
}
