package vcsc.core.assembly;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

import vcsc.core.component.Component;
import vcsc.core.component.Composable;

public class Assembly implements Composable {
    protected ArrayList<Composable> components = new ArrayList<>();

    public Assembly() {
        super();
    }

    protected void registerComponent(Composable component) {
        components.add(component);
    }
x`
    protected void unregisterComponent(Composable component) {
        components.remove(component);
    }

    protected void unregisterComponent(int index) {
        components.remove(index);
    }

    protected void clearComponents() {
        components.clear();
    }

    protected ArrayList<Composable> getComponents() {
        return components;
    }

    @Override
    public void init() {
        for (Composable component : components) {
            component.init();
        }
    }

    @Override
    public void init_loop() {
        for (Composable component : components) {
            component.init_loop();
        }
    }

    @Override
    public void start() {
        for (Composable component : components) {
            component.start();
        }
    }

    @Override
    public void loop() {
        // Update all components
        for (Composable component : components) {
            component.loop();
        }
    }

    @Override
    public void stop() {
        for (Composable component : components) {
            component.stop();
        }
    }
}
