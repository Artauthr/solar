package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.FloatProperty;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.Setter;

public class DebugFloatSlider implements DebugRenderable {
    private final FloatProperty floatProperty;
    private final float[] modifierValue;

    @Setter
    private float min;
    @Setter
    private float max;

    private final int sliderFlags;

    public DebugFloatSlider (float min, float max, int sliderFlags, FloatProperty valueProvider) {
        this.floatProperty = valueProvider;
        this.sliderFlags = sliderFlags;
        this.min = min;
        this.max = max;

        float value = valueProvider.get();
        modifierValue = new float[] {value};
    }

    public DebugFloatSlider (float min, float max, FloatProperty valueProvider) {
        this(min, max, ImGuiSliderFlags.None, valueProvider);
    }

    @Override
    public void render() {
        if (ImGui.sliderFloat("Timestep", modifierValue, min, max, null, sliderFlags)) {
            floatProperty.set(modifierValue[0]);
        }
    }
}
