package vcsc.teamcode.opModes.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import roadrunner.MecanumDrive;


@Autonomous(name = "Basic Autonomous", group = "Autonomous")
public class redRightTestAuto extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Initialize the drive class for RoadRunner
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

        // Define the starting position of the robot
        // Set the initial pose estimate for the drive
        Action TrajectoryAction1 = drive.actionBuilder(new Pose2d(0,0,0))
                .strafeTo(new Vector2d(0,-50))
                .build();
        // Wait for the start command
        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(TrajectoryAction1);

    }
}