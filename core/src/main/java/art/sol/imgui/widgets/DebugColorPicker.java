package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.ColorProvider;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Null;
import imgui.ImGui;

public class DebugColorPicker implements DebugRenderable {
    private final String title;
    private ColorProvider colorProvider;

    public DebugColorPicker (String title) {
        this(title, null);
    }

    public DebugColorPicker (String title, @Null Color target) {
        this.title = title;
        this.colorProvider = new ColorProvider(target);
    }

    public void setTargetColor (Color color) {
        this.colorProvider = new ColorProvider(color);
    }

    @Override
    public void render () {
        if (ImGui.colorPicker4(title, colorProvider.asPrimitiveArray())) {
            colorProvider.writeUpdatedValues();
        }
    }
}
