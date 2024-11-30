package vcsc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import vcsc.teamcode.opmodes.base.BaseLinearOpMode;

@Autonomous(group = "Main", name = "MainAuto")
public class MainAuto extends BaseLinearOpMode {
    @Override
    public void runOpMode() {
        super.runOpMode();
        Action park = drive.actionBuilder(new Pose2d(0, 0, 0))
                .lineToY(-50).build();
        waitForStart();
        matchTimer.reset();

        Actions.runBlocking(park);

    }
}
