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
        timeStepSlider = new DebugFloatSlider("Timestep",
                new FloatProvider(() ->
                SolarSystem.get().getTimeStep(),
                value -> SolarSystem.get().setTimeStep(value)));
        
        timeStepSlider.setConstraints(-0.05f, 0.05f);
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
