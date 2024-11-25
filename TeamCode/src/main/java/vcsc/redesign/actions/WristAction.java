package vcsc.redesign.actions;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.redesign.actuators.ClawActuator;
import vcsc.redesign.actuators.WristActuator;

public class WristAction extends WristActuator {


    public WristAction(ServoImplEx wristRot, ServoImplEx wristPivot) {
        super(wristRot);
        /* I don't know why this doesn't work but yeah
        super(wristPivot);
         */
    }
    public void setRot(double Rotation){

    }
    public void setPivot(double Pivot){

    }
}
