package vcsc.core.component;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public abstract class PoweredPID extends Component {
    protected PIDFController controller;
    protected PIDFCoefficients coefficients;
    protected double power;

    protected PoweredPID(HardwareMap hMap) {
        super(hMap);
    }

    public void setCoefficients(PIDFCoefficients coefficients) {
        this.coefficients = coefficients;
        controller = new PIDFController(coefficients.p, coefficients.i, coefficients.d, coefficients.f);
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setTarget(double target) {
        controller.setSetPoint(target);
    }

    public double getTarget(double target) {
        return controller.getSetPoint();
    }

    abstract public double getPosition();

    abstract public void loop();


}
