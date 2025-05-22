package art.sol.imgui.widgets;

import art.sol.valueproviders.io.FloatReader;
import art.sol.valueproviders.io.FloatWriter;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.Setter;

public class DebugFloatSlider {
    private final String title;

    private float min = 0;
    private float max = 10;

    @Setter
    private int sliderFlags = ImGuiSliderFlags.None;

    private final float[] data = new float[1];

    public DebugFloatSlider (String title) {
        this.title = title;
    }

    public void setConstraints (float min, float max) {
        this.min = min;
        this.max = max;
    }

    public final void render (FloatReader fr, FloatWriter fw) {
        float current = fr.read();
        data[0] = current;

        if (ImGui.sliderFloat(title, data, min, max, null, sliderFlags)) {
            fw.write(data[0]);
        }
    }
}
