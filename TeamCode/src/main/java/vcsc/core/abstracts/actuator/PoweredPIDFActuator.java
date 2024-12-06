package vcsc.core.abstracts.actuator;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.state.PoweredPIDFState;
import vcsc.core.abstracts.state.State;

public abstract class PoweredPIDFActuator extends Actuator {
    protected PIDFController controller;
    protected PIDFCoefficients coefficients;

    protected double power;
    protected double speed;

    protected double targetPosition = 0;

    protected ElapsedTime loopTime;

    public PoweredPIDFActuator(PIDFCoefficients coefficients) {
        this.coefficients = coefficients;
        controller = new PIDFController(
                coefficients.p,
                coefficients.i,
                coefficients.d,
                coefficients.f
        );
        controller.setTolerance(10);
        loopTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        loopTime.reset();
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

    public abstract double getPosition();

    @Override
    public void loop() {
        /*double newTarget = controller.getSetPoint();
        double deltaPos = targetPosition - controller.getSetPoint();
        double deltaPosAbs = Math.abs(deltaPos);
        double increment = speed * loopTime.time() / 20;

        if (deltaPos > 0) {
            newTarget += Math.min(deltaPosAbs, increment);
        } else {
            newTarget -= Math.min(deltaPosAbs, increment);
        }*/

//        controller.setSetPoint(targetPosition);
        setInAction(!controller.atSetPoint() || power != 0);
        if (power != 0) {
            loopPower();
        } else {
            loopPID();
        }
        loopTime.reset();
    }

    @Override
    public void updateState(State newState) {
        PoweredPIDFState poweredPIDFState = (PoweredPIDFState) newState;
        if (poweredPIDFState.getPower() == 0) {
//            targetPosition = poweredPIDFState.getTargetPosition();
            controller.setSetPoint(poweredPIDFState.getTargetPosition());
            controller.setP(coefficients.p);
            controller.setI(coefficients.i);
            controller.setD(coefficients.d);
            controller.setF(coefficients.f);
        } else if (poweredPIDFState.getTargetPosition() != getPosition()) {
            poweredPIDFState.setTargetPosition(getPosition(), true);
        }
        speed = poweredPIDFState.getSpeed();
        power = poweredPIDFState.getPower();
        setInAction(!controller.atSetPoint() || power != 0);
    }
}
