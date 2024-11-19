package vcsc.teamcode.armOld.wrist.cmp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Wrist {
    ServoImplEx wristX;
    ServoImplEx wristY;

    public Wrist(HardwareMap hardwareMap) {
        wristX = hardwareMap.get(ServoImplEx.class, "wristX");
        wristY = hardwareMap.get(ServoImplEx.class, "wristY");
    }

    public void setAngleX(double angle) {
        wristX.setPosition(angle);
    }

    public void setAngleY(double angle) {
        wristY.setPosition(angle);
    }

    public void setAngle(double angleX, double angleY) {
        wristX.setPosition(angleX);
        wristY.setPosition(angleY);
    }

    public void setPose(WristPose pose) {
        wristX.setPosition(pose.x);
        wristY.setPosition(pose.y);
    }
}