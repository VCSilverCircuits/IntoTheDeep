package vcsc.teamcode.arm;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import vcsc.core.assembly.Assembly;

@Config
public class Arm extends Assembly {
    ArmRotation rotation;
    ArmExtension extension;

    public Arm(HardwareMap hardwareMap) {
        super(hardwareMap);
        rotation = new ArmRotation(hardwareMap);
        extension = new ArmExtension(hardwareMap);
        registerComponent(rotation);
        registerComponent(extension);
    }

    public void setPosition(double extension, double angle) {
        setExtension(extension);
        setRotation(angle);
    }

    /*public void setPose(ArmPose pose) {
        setExtension(pose.extension);
        setRotation(pose.rotation);
    }*/

    public void setExtensionPower(double power) {
        extension.setPower(power);
    }

    public void setRotationPower(double power) {
        rotation.setPower(power);
    }

    public double getRotation() {
        return rotation.getAngle();
    }

    public void setRotation(double position) {
        rotation.setTargetAngle(position);
    }

    public double getExtension() {
        return extension.getLength();
    }

    public void setExtension(double position) {
        extension.setTargetLength(position);
    }

    public void loop() {
        super.loop();
    }
}
