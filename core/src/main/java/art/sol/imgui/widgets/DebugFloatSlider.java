package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.FloatProvider;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.Setter;

public class DebugFloatSlider implements DebugRenderable {
    @Setter
    private FloatProvider floatProvider;

    private String title;

    private final float min;
    private final float max;

    private final int sliderFlags;

    public DebugFloatSlider (String title, float min, float max) {
        this(title, min, max, ImGuiSliderFlags.None);
    }

    public DebugFloatSlider (String title, float min, float max, int sliderFlags) {
        this.title = title;
        this.sliderFlags = sliderFlags;
        this.min = min;
        this.max = max;
        this.title = title;
    }

    @Override
    public final void render() {
        if (floatProvider == null) return;

        if (ImGui.sliderFloat(title, floatProvider.asPrimitiveArray(), min, max, null, sliderFlags)) {
            floatProvider.writeUpdatedValue();
        }
    }
}
