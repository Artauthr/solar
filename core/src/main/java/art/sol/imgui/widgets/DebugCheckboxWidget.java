package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.BooleanProvider;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.Setter;

public class DebugCheckboxWidget implements DebugRenderable {
    private final String title;
    private final BooleanProvider booleanProvider;

    public DebugCheckboxWidget (String title, BooleanProvider booleanProvider) {
        this.title = title;
        this.booleanProvider = booleanProvider;
    }

    @Override
    public void render () {
        if (booleanProvider == null) return;

        boolean currentState = booleanProvider.get();
        if (ImGui.checkbox(title, currentState)) {
            booleanProvider.set(!currentState);
        }
    }
}
