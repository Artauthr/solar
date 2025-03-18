package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.FloatProvider;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.Setter;

public class DebugFloatSlider implements DebugRenderable {
    private final FloatProvider floatProvider;

    @Setter
    private float min;
    @Setter
    private float max;

    private final int sliderFlags;

    public DebugFloatSlider (float min, float max, int sliderFlags, FloatProvider valueProvider) {
        this.floatProvider = valueProvider;
        this.sliderFlags = sliderFlags;
        this.min = min;
        this.max = max;
    }

    public DebugFloatSlider (float min, float max, FloatProvider valueProvider) {
        this(min, max, ImGuiSliderFlags.None, valueProvider);
    }

    @Override
    public void render() {
        if (ImGui.sliderFloat("Timestep", floatProvider.primitiveArray(), min, max, null, sliderFlags)) {
            floatProvider.writeValue();
        }
    }
}
