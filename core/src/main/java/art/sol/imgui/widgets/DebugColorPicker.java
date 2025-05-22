package art.sol.imgui.widgets;

import art.sol.valueproviders.io.ColorProvider;
import com.badlogic.gdx.graphics.Color;
import imgui.ImGui;
import lombok.Setter;

public class DebugColorPicker {
    private final String title;

    @Setter
    private int flags;

    private final float[] data = new float[4];

    public DebugColorPicker (String title) {
        this.title = title;
    }

    public void render (ColorProvider colorProvider) {
        Color targetColor = colorProvider.read();

        data[0] = targetColor.r;
        data[1] = targetColor.g;
        data[2] = targetColor.b;
        data[3] = targetColor.a;

        if (ImGui.colorPicker4(title, data, flags)) {
            targetColor.set(data[0], data[1], data[2], data[3]);
        }
    }
}
