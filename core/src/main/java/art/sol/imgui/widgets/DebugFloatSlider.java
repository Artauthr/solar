package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.ui.FloatValueProvider;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.Setter;

public class DebugFloatSlider implements DebugRenderable {
    private final FloatValueProvider floatValueProvider;
    private final float[] modifierValue;

    @Setter
    private float min;
    @Setter
    private float max;

    private final int sliderFlags;

    public DebugFloatSlider (float min, float max, int sliderFlags, FloatValueProvider valueProvider) {
        this.floatValueProvider = valueProvider;
        this.sliderFlags = sliderFlags;
        this.min = min;
        this.max = max;

        float value = valueProvider.get();
        modifierValue = new float[] {value};
    }

    public DebugFloatSlider (float min, float max, FloatValueProvider valueProvider) {
        this(min, max, ImGuiSliderFlags.None, valueProvider);
    }

    @Override
    public void render() {
        if (ImGui.sliderFloat("Timestep", modifierValue, min, max, null, sliderFlags)) {
            floatValueProvider.set(modifierValue[0]);
        }
    }
}
