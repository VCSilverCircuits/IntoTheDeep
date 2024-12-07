package vcsc.teamcode;

import com.acmerobotics.roadrunner.ftc.LynxFirmware;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "LinearAuto")
public class ScrimmageAuto extends LinearOpMode {

    //double TPMM = (28*3.61*3.61*96*3.14159);

    DcMotorEx leftFront, leftBack, rightBack, rightFront;

    ElapsedTime timer = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {
        LynxFirmware.throwIfModulesAreOutdated(hardwareMap);

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        leftFront = hardwareMap.get(DcMotorEx.class, "frontLeft");
        leftBack = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightBack = hardwareMap.get(DcMotorEx.class, "backRight");
        rightFront = hardwareMap.get(DcMotorEx.class, "frontRight");

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        //Initialization Code Goes Here
        waitForStart();
        //Auto Code Goes Here
        timer.reset();

        /*leftFront.setTargetPosition(-(int)(1270*TPMM));
        leftBack.setTargetPosition((int)(1270*TPMM));
        rightFront.setTargetPosition((int)(1270*TPMM));
        rightBack.setTargetPosition(-(int)(1270*TPMM));
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/

        //IMMA COMMENT
        leftFront.setPower(0.5);
        rightFront.setPower(-0.5);
        leftBack.setPower(-0.5);
        rightBack.setPower(0.5);
        while (timer.milliseconds() < 2000) {
            telemetry.addLine("moving");
            telemetry.update();
        }
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

    }
}