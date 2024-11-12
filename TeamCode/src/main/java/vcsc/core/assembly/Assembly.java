package vcsc.core.assembly;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

import vcsc.core.component.Component;

public class Assembly extends Component {
    protected ArrayList<Component> components = new ArrayList<>();

    public Assembly(HardwareMap hMap) {
        super(hMap);
    }

    protected void registerComponent(Component component) {
        components.add(component);
    }

    protected void unregisterComponent(Component component) {
        components.remove(component);
    }

    protected void unregisterComponent(int index) {
        components.remove(index);
    }

    protected void clearComponents() {
        components.clear();
    }

    protected ArrayList<Component> getComponents() {
        return components;
    }

    @Override
    public void loop() {
        // Update all components
        for (Component component : components) {
            component.loop();
        }
    }
}
