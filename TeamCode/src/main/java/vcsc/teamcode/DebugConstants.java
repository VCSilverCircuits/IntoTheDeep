package vcsc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristRotPose;

@Config
public class DebugConstants {
    public static double elbow = ElbowPose.BASKET.getPosition();
    public static double armExt = ArmExtPose.BASKET.getLength();
    public static double armRot = ArmRotPose.BASKET.getAngle();
    public static double wristRot = WristRotPose.BASKET.getPosition();
    public static double wristPivot = WristPivotPose.REVERSE.getPosition();
    public static PIDFCoefficients rotCoeffs = new PIDFCoefficients(0.01, 0, 0, 0);
    public static PIDFCoefficients extCoeffs = new PIDFCoefficients(0.02, 0, 0.00015, 0);

}
