package vcsc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Arm {
    public static double kP_rot = 0.001, kI_rot = 0, kD_rot = 0, kF_rot = 0;
    public static double kP_ext = 0.001, kI_ext = 0, kD_ext = 0, kF_ext = 0;
    // 28 Ticks/rev * 1 rev /(Pi * 0.787 in)
    /*static double TICKS_PER_REV = 28;
    static double TICKS_PER_INCH = TICKS_PER_REV / (Math.PI * 0.787);
    static double INCHES_PER_TICK = 1 / TICKS_PER_INCH;
    static double TICKS_PER_DEGREE = TICKS_PER_REV * 360;
    static double DEGREES_PER_TICK = 1 / TICKS_PER_DEGREE;*/
    public static PIDFCoefficients extCoeffs = new PIDFCoefficients(0.01, 0, 0.0001, 0);
    public static PIDFCoefficients rotCoeffs = new PIDFCoefficients(0.005, 0, 0.00035, 0);
    public static PIDFController rotationController = new PIDFController(rotCoeffs.p, rotCoeffs.i, rotCoeffs.d, rotCoeffs.f);
    public static PIDFController extensionController = new PIDFController(extCoeffs.p, extCoeffs.i, extCoeffs.d, extCoeffs.f);
    double extensionPower = 0;
    double extensionTarget, rotationTarget;
    DcMotorEx extension1, extension2, rotation1, rotation2;

    public Arm(HardwareMap hardwareMap) {
        extension1 = hardwareMap.get(DcMotorEx.class, "extension1");
        extension1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extension1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension1.setDirection(DcMotorSimple.Direction.REVERSE);

        extension2 = hardwareMap.get(DcMotorEx.class, "extension2");
        extension2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extension2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rotation1 = hardwareMap.get(DcMotorEx.class, "rotation1");
        rotation1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotation1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rotation1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rotation2 = hardwareMap.get(DcMotorEx.class, "rotation2");
        rotation2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotation2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rotation2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rotationController = new PIDFController(kP_rot, kI_rot, kD_rot, kF_rot);
        //extensionController = new PIDFController(kP_ext, kI_ext, kD_ext, kF_ext);
    }

    public void setPosition(double extension, double angle) {
        setExtension(extension);
        setRotation(angle);
    }

    public void setPose(ArmPose pose) {
        setExtension(pose.extension);
        setRotation(pose.rotation);
    }

    public void setExtensionPower(double power) {
        this.extensionPower = power;
    }

    public double getRotation() {
        return rotation1.getCurrentPosition();
    }

    public void setRotation(double position) {
        rotationTarget = position;
    }

    public double getExtension() {
        return extension1.getCurrentPosition();
    }

    public void setExtension(double position) {
        extensionTarget = position;
    }

    public void update(Telemetry telem) {
        extensionController.setP(extCoeffs.p);
        extensionController.setI(extCoeffs.i);
        extensionController.setD(extCoeffs.d);
        extensionController.setF(extCoeffs.f);
        rotationController.setP(rotCoeffs.p);
        rotationController.setI(rotCoeffs.i);
        rotationController.setD(rotCoeffs.d);
        rotationController.setF(rotCoeffs.f);
        double newRot = rotationController.calculate(getRotation(), rotationTarget);
        double newExt = extensionController.calculate(getExtension(), extensionTarget);
        MultipleTelemetry mt = new MultipleTelemetry(telem, FtcDashboard.getInstance().getTelemetry());
        mt.addData("Target", extensionTarget);
        mt.addData("Current", getExtension());
        mt.addData("Power", newExt);
        mt.update();

        if (extension1.getCurrentPosition() < 1600 || extension1.getCurrentPosition() > 3300) {
            this.extensionPower = 0;
        }

        rotation1.setPower(newRot);
        rotation2.setPower(newRot);
        if (this.extensionPower != 0) {
            extension1.setPower(-this.extensionPower);
            extension2.setPower(-this.extensionPower);
            extensionTarget = extension1.getCurrentPosition();

        } else {
            extension1.setPower(-newExt);
            extension2.setPower(-newExt);
        }
    }
}
