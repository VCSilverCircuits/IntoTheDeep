package vcsc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Arm {

  public final double TPI = 100;
   private final double maxPosition = 42;
   private final double decelerationStart = 37;
   final double minPosition = 0;
   private double targetPosition = 0;
   private double currentPosition = 0;
   private final double kP = 0.1;
   private final double kI = 0.01;
   private final double kD = 0.1;
   private double previousError = 0;
   private double integral = 0;
    DcMotorEx rotation;
    public DcMotorEx extention;
    public Arm(DcMotorEx rotation) {
        this.rotation = rotation;
        this.rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rotation.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.extention = extention;
        this.extention.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.extention.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void setExtensionLength(double length ) { // Length in CM
       double currentPositionInches = extention.getCurrentPosition() / TPI;
       double targetPositionInches = length;

       if (targetPositionInches < 0) {
           targetPositionInches = 0;
       } else if (targetPositionInches > maxPosition) {
           targetPositionInches = maxPosition;
       }
       double error = targetPositionInches - currentPositionInches;

       if (targetPositionInches <= decelerationStart) {
           error *= (targetPositionInches / decelerationStart);
       } else if (targetPositionInches >= maxPosition - decelerationStart); {
           error *= ((maxPosition - targetPositionInches) / decelerationStart);
       }
       integral += error;
        double derivative = error - previousError;
        double output = (kP * error) + (kI * integral) + (kD * derivative);

        extention.setPower(output);
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

