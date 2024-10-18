package vcsc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Arm {
    DcMotorEx rotation;
    public Arm(DcMotorEx rotation) {
        this.rotation = rotation;
    }
    public void setExtensionLength(double length ) { // Length in CM

    }

    public void retract() {

    }

    public void moveToAngle(double angle) {

    }
}