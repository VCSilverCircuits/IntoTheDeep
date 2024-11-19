package vcsc.redesign.actuators;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.redesign.DcMotorGroup;
import vcsc.redesign.states.ArmExtensionState;
import vcsc.redesign.states.ArmRotationState;

public class RotActuator extends PoweredPIDFActuator<ArmRotationState>{
    DcMotorGroup motors;
    public RotActuator(HardwareMap hardwareMap, PIDFCoefficients coefficients) {
        super(coefficients);
        DcMotorEx extension1 = hardwareMap.get(DcMotorEx.class, "rotation1");
        DcMotorEx extension2 = hardwareMap.get(DcMotorEx.class, "rotation2");
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
        double outputPower = controller.calculate(motors.getCurrentPosition());
        motors.setPower(outputPower);
    }
}
