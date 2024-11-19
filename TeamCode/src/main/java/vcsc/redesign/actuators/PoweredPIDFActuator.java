package vcsc.redesign.actuators;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.redesign.states.PoweredPIDFState;

public abstract class PoweredPIDFActuator<S extends PoweredPIDFState> extends Actuator<S> {
    PIDFController controller;
    PIDFCoefficients coefficients;

    double power;

    public PoweredPIDFActuator(PIDFCoefficients coefficients) {
        this.coefficients = coefficients;
        controller = new PIDFController(
            coefficients.p,
            coefficients.i,
            coefficients.d,
            coefficients.f
        );
    }

    protected abstract void loopPower();

    protected abstract void loopPID();

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
    public void updateState(S newState) {
        controller.setSetPoint(newState.getTargetPosition());
        controller.setP(coefficients.p);
        controller.setI(coefficients.i);
        controller.setD(coefficients.d);
        controller.setF(coefficients.f);

        power = newState.getPower();
    }
}
