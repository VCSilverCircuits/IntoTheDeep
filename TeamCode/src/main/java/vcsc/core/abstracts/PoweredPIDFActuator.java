package vcsc.core.abstracts;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public abstract class PoweredPIDFActuator extends Actuator {
    protected PIDFController controller;
    protected PIDFCoefficients coefficients;

    protected double power;

    public PoweredPIDFActuator(PIDFCoefficients coefficients) {
        this.coefficients = coefficients;
        controller = new PIDFController(
                coefficients.p,
                coefficients.i,
                coefficients.d,
                coefficients.f
        );
    }

    public void setPIDFCoefficients(PIDFCoefficients coefficients) {
        this.coefficients = coefficients;
        this.controller.setPIDF(
                coefficients.p,
                coefficients.i,
                coefficients.d,
                coefficients.f
        );
    }

    protected abstract void loopPower();

    protected abstract void loopPID();

    protected abstract double getPosition();

    @Override
    public void loop() {
        setInAction(!controller.atSetPoint() || power != 0);
        if (power != 0) {
            loopPower();
        } else {
            loopPID();
        }
    }

    @Override
    public void updateState(State newState) {
        PoweredPIDFState poweredPIDFState = (PoweredPIDFState) newState;
        if (poweredPIDFState.getPower() == 0) {
            controller.setSetPoint(poweredPIDFState.getTargetPosition());
            controller.setP(coefficients.p);
            controller.setI(coefficients.i);
            controller.setD(coefficients.d);
            controller.setF(coefficients.f);
        } else if (poweredPIDFState.getTargetPosition() != getPosition()) {
            poweredPIDFState.setTargetPosition(getPosition(), true);
        }

        power = poweredPIDFState.getPower();
    }
}
