package vcsc.teamcode.armOld.cmp;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.core.GlobalTelemetry;
import vcsc.core.component.PoweredPID;
import vcsc.core.hardware.DcMotorGroup;

public class ArmExtension extends PoweredPID {
    public static final double TPR = 28;
    public static final double GEAR_RATIO = 1 / (3.0 * 4.0) * 56.0 / 60;
    public static final double PULLER_DIAMETER = 2; // in cm
    public static final double TICKS_PER_CM = TPR * GEAR_RATIO / (PULLER_DIAMETER * Math.PI);

    public static final double MIN_LENGTH = 10;
    public static final double MAX_LENGTH = 80;

    public static final PIDFCoefficients EXTENSION_COEFFS = new PIDFCoefficients(0.01, 0, 0.0001, 0);

    DcMotorGroup motors;

    public ArmExtension(HardwareMap hMap) {
        super(hMap);
        DcMotorEx extension1 = hMap.get(DcMotorEx.class, "extension1");
        DcMotorEx extension2 = hMap.get(DcMotorEx.class, "extension2");
        extension2.setDirection(DcMotorSimple.Direction.REVERSE);
        motors = new DcMotorGroup(extension1, extension2);
        motors.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        controller = new PIDFController(EXTENSION_COEFFS.p, EXTENSION_COEFFS.i, EXTENSION_COEFFS.d, EXTENSION_COEFFS.f);

    }

    @Override
    public double getPosition() {
        return -motors.getCurrentPosition();
    }

    public double getTargetLength() {
        return controller.getSetPoint() / TICKS_PER_CM;
    }

    public void setTargetLength(double length) {
        controller.setSetPoint(length * TICKS_PER_CM);
    }

    public double getLength() {
        return getPosition() / TICKS_PER_CM;
    }


    @Override
    public void loop() {
        double outputPower = controller.calculate(getPosition());
        if (power != 0) {
            outputPower = power;
            controller.setSetPoint(getPosition());
        }

        if (getLength() < MIN_LENGTH) {
            outputPower = Math.max(0, outputPower);
        } else if (getLength() > MAX_LENGTH) {
            outputPower = Math.min(0, outputPower);
        }

        if (controller.getSetPoint() < MIN_LENGTH) {
            setTargetLength(MIN_LENGTH);
        } else if (controller.getSetPoint() > MAX_LENGTH) {
            setTargetLength(MAX_LENGTH);
        }

        GlobalTelemetry.getInstance().addData("Power", power);
        GlobalTelemetry.getInstance().addData("Output power", outputPower);

        motors.setPower(outputPower);
    }
}
