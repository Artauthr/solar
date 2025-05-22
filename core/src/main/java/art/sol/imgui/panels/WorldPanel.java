package art.sol.imgui.panels;

import art.sol.SolarSystem;
import art.sol.imgui.widgets.DebugCheckboxWidget;
import art.sol.imgui.widgets.DebugFloatSlider;
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
        timeStepSlider = new DebugFloatSlider("Timestep");
        timeStepSlider.setConstraints(-0.4f, 0.4f);

        activeCheckBox = new DebugCheckboxWidget("Active");
    }

    @Override
    public void renderContent() {
        ImGui.pushItemWidth(100);
        timeStepSlider.render(() -> SolarSystem.get().getTimeStep(), value -> SolarSystem.get().setTimeStep(value));
        activeCheckBox.render(() -> SolarSystem.get().isActive(), value -> SolarSystem.get().setActive(value));
    }

    @Override
    protected String getTitle() {
        return "World";
    }
}
