package vcsc.redesign.actuators;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.redesign.DcMotorGroup;
import vcsc.redesign.states.ArmExtensionState;

public class ExtActuator extends PoweredPIDFActuator<ArmExtensionState>{
    DcMotorGroup motors;
    public ExtActuator(HardwareMap hardwareMap, PIDFCoefficients coefficients) {
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
        // NOTE: Encoders for extension run backwards
        double outputPower = controller.calculate(-motors.getCurrentPosition());
        motors.setPower(outputPower);
    }
}
