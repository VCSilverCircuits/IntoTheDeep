package vcsc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm {

    public final double TPI = 100;
    final double minPosition = 0;
    private final double maxPosition = 42;
    private final double decelerationStart = 37;
    private final double kP = 0.1;
    private final double kI = 0.01;
    private final double kD = 0.1;
    private final double targetPosition = 0;
    private final double currentPosition = 0;
    public DcMotorEx extension;
    DcMotorEx rotation;
    private double previousError = 0;
    private double integral = 0;

    public Arm(HardwareMap hardwareMap) {
        this.rotation = hardwareMap.get(DcMotorEx.class, "armRotation");
        this.rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rotation.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.extension = hardwareMap.get(DcMotorEx.class, "armExtension");
        this.extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.extension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setExtensionLength(double length) { // Length in CM
        double currentPositionInches = extension.getCurrentPosition() / TPI;
        double targetPositionInches = length;

        if (targetPositionInches < 0) {
            targetPositionInches = 0;
        } else if (targetPositionInches > maxPosition) {
            targetPositionInches = maxPosition;
        }

        double error = targetPositionInches - currentPositionInches;

        if (targetPositionInches <= decelerationStart) {
            error *= (targetPositionInches / decelerationStart);
        } else if (targetPositionInches >= maxPosition - decelerationStart) {
            error *= ((maxPosition - targetPositionInches) / decelerationStart);
        }
        integral += error;
        double derivative = error - previousError;
        double output = (kP * error) + (kI * integral) + (kD * derivative);

        extension.setPower(output);
        previousError = error;
    }

    public void retract() {
        setExtensionLength(0);
    }

    public void moveToAngle(double targetAngle) {
        double currentAngle = rotation.getCurrentPosition(); // Get current position in encoder ticks

        // Example conversion: Convert angle to ticks (adjust based on your gear ratio)
        double targetTicks = convertAngleToTicks(targetAngle);

        // Calculate error for angle
        double angleError = targetTicks - currentAngle;

        // Simple proportional control for angle adjustment (scale as needed)
        double angleOutput = angleError * 0.05;

        // Apply power to the rotation motor for angle adjustment
        rotation.setPower(angleOutput);
    }

    private double convertAngleToTicks(double angle) {
        // Conversion logic to transform an angle into encoder ticks
        double ticksPerDegree = 10; // Example conversion factor (adjust as needed)
        return angle * ticksPerDegree;
    }
}

