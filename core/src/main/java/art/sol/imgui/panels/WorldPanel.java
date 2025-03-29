package art.sol.imgui.panels;

import art.sol.SolarSystem;
import art.sol.imgui.widgets.ADebugPanel;
import art.sol.imgui.widgets.DebugCheckboxWidget;
import art.sol.imgui.widgets.DebugFloatSlider;
import art.sol.valueproviders.BooleanProvider;
import art.sol.valueproviders.FloatProvider;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class WorldPanel extends ADebugPanel {
    private final DebugFloatSlider timeStepSlider;
    private final DebugCheckboxWidget activeCheckBox;

    @Override
    protected void begin() {
        ImGui.begin(getTitle(), null, ImGuiWindowFlags.AlwaysAutoResize);
    }

    public WorldPanel() {
        timeStepSlider = new DebugFloatSlider("Timestep",
                new FloatProvider(() ->
                SolarSystem.get().getTimeStep(),
                value -> SolarSystem.get().setTimeStep(value)));
        
        timeStepSlider.setConstraints(-0.4f, 0.4f);

        activeCheckBox = new DebugCheckboxWidget("Active",
                new BooleanProvider(
                        ()-> SolarSystem.get().isActive(),
                        value -> SolarSystem.get().setActive(value)));
    }

    @Override
    public void renderContent() {
        ImGui.pushItemWidth(100);
        timeStepSlider.render();
        activeCheckBox.render();
    }

    @Override
    protected String getTitle() {
        return "World";
    }
}
