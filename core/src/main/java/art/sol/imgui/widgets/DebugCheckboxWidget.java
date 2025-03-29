package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.BooleanProvider;
import art.sol.valueproviders.io.BooleanReader;
import art.sol.valueproviders.io.BooleanWriter;
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

        boolean currentState = booleanProvider.read();
        if (ImGui.checkbox(title, currentState)) {
            booleanProvider.write(!currentState);
        }
    }
}
