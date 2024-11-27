package vcsc.core.mock;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class MockServo implements Servo {
    double position = 0;
    double targetPosition = 0;
    double min = 0;
    double max = 1;
    double speed = 0.5;
    long lastTime;
    Direction direction = Direction.FORWARD;

    public MockServo(long startTime) {
        this.lastTime = startTime;
    }

    @Override
    public ServoController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public double getPosition() {
        return position;
    }

    @Override
    public void setPosition(double position) {
        targetPosition = position;
    }

    public void forcePosition(double position) {
        this.position = position;
        targetPosition = position;
    }

    @Override
    public void scaleRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return "MockServo";
    }

    @Override
    public String getConnectionInfo() {
        return "";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }

    public void loop(long time) {
        // Calculate the time since the last loop
        long deltaTime = time - lastTime;
        if (deltaTime < 100) {
            return;
        }
        lastTime = time;

        // Calculate the speed based on the time since the last loop
        double iterSpeed = speed * deltaTime / 1000;

        // Increase the position of the servo by the speed
        position += Math.signum(targetPosition - position) * Math.min(iterSpeed, Math.abs(targetPosition - position));
        // Clamp the position to the min and max values
        position = Math.min(max, Math.max(min, position));
    }
}
