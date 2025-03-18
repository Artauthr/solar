package art.sol.imgui.panels;

import art.sol.SolarSystem;
import art.sol.imgui.widgets.ADebugPanel;
import art.sol.imgui.widgets.DebugFloatSlider;
import art.sol.valueproviders.FloatProvider;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class WorldPanel extends ADebugPanel {
    private final DebugFloatSlider timeStepSlider;

    @Override
    protected void begin() {
        ImGui.begin(getTitle(), null, ImGuiWindowFlags.AlwaysAutoResize);
    }

    public WorldPanel() {
        timeStepSlider = new DebugFloatSlider(-0.5f, 0.5f, new FloatProvider() {
            @Override
            public float readValue () {
                return SolarSystem.get().getTimeStep();
            }

            @Override
            public void writeValue () {
                SolarSystem.get().setTimeStep(get());
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
