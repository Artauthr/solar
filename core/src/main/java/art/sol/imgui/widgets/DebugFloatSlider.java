package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.FloatProvider;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.NonNull;
import lombok.Setter;

public class DebugFloatSlider implements DebugRenderable {
    private final String title;

    private float min = 0;
    private float max = 10;

    private final FloatProvider floatProvider;

    @Setter
    private int sliderFlags = ImGuiSliderFlags.None;

    public DebugFloatSlider (String title, @NonNull FloatProvider floatProvider) {
        this.floatProvider = floatProvider;
        this.title = title;
    }

    public void setConstraints (float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public final void render() {
        if (ImGui.sliderFloat(title, floatProvider.asPrimitiveArray(), min, max, null, sliderFlags)) {
            floatProvider.writeData();
        }
    }
}
