package art.sol.imgui.widgets.panels;

import art.sol.SolarSystem;
import art.sol.imgui.widgets.ADebugPanel;
import art.sol.imgui.widgets.DebugFloatSlider;
import art.sol.ui.FloatValueProvider;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class WorldPanel extends ADebugPanel {
    private final DebugFloatSlider timeStepSlider;

    @Override
    protected void begin() {
        ImGui.begin(getTitle(), null, ImGuiWindowFlags.AlwaysAutoResize);
    }

    public WorldPanel() {
        timeStepSlider = new DebugFloatSlider(-0.5f, 0.5f, new FloatValueProvider() {
            @Override
            public float get() {
                return SolarSystem.get().getTimeStep();
            }

            @Override
            public void set(float value) {
                SolarSystem.get().setTimeStep(value);
            }
        });


    }

    @Override
    public void renderContent() {
        ImGui.pushItemWidth(100);
        timeStepSlider.render();
    }

    @Override
    protected String getTitle() {
        return "World";
    }

}
