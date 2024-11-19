package vcsc.teamcode.armOld.asy;

import com.acmerobotics.dashboard.config.Config;

import vcsc.core.assembly.Assembly;
import vcsc.teamcode.armOld.cmp.ArmExtension;
import vcsc.teamcode.armOld.cmp.ArmRotation;

@Config
public class Arm extends Assembly {
    ArmRotation rotation;
    ArmExtension extension;

    public Arm(ArmRotation rotation, ArmExtension extension) {
        this.rotation = rotation;
        this.extension = extension;
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
