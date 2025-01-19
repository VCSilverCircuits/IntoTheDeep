package vcsc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristRotPose;

@Config
public class DebugConstants {
    public static double elbow = ElbowPose.BASKET.getPosition();
    public static double armExt = 0;
    public static double armRot = 0;
    public static double wristRot = WristRotPose.BASKET.getPosition();
    public static double wristPivot = WristPivotPose.REVERSE.getPosition();
    public static PIDFCoefficients rotCoeffs = new PIDFCoefficients(0.01, 0, 0, 0);
    public static PIDFCoefficients extCoeffs = new PIDFCoefficients(0.015, 0, 0, 0);
    public static double rotSpeed = 10000;
    public static double extSpeed = 10000;
    public static double WALL_ELBOW = 0.75;
    public static double WALL_WRIST_ROT = 0.08;
    public static boolean CLAW_OPEN = true;
}
