package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.Float2Provider;
import art.sol.valueproviders.Vec2Provider;
import art.sol.valueproviders.io.FloatReader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.Setter;

public class DebugFloat2Slider implements DebugRenderable {
    private Float2Provider float2Provider;

    @Setter
    private String title;

    private float min;
    private float max;

    @Setter
    private int sliderFlags;

    public DebugFloat2Slider (String title) {
        this(title, null);
    }

    public DebugFloat2Slider (String title, Vector2 target) {
        this.title = title;
        this.float2Provider = new Vec2Provider(target);

        sliderFlags = ImGuiSliderFlags.AlwaysClamp;
    }

    public void setTarget (Vector2 target) {
        this.float2Provider = new Vec2Provider(target);
//        FloatReader readerX = float2Provider.getReaderX();

        setConstraints(-1000, 1000);
    }

    public void setConstraints (float min, float max) {
        this.min = min;
        this.max = max;
    }

    public void setTargetVector (Vector2 targetVector) {
        this.float2Provider = new Vec2Provider(targetVector);

    }

    @Override
    public final void render() {
        if (float2Provider == null) return;

//        ImGui.pushItemWidth(100f);
//        ImGui.text(title); // Display the label first
//        ImGui.sameLine(); // Keep the next element on the same line
        ImGui.pushItemWidth(150f);
        if (ImGui.sliderFloat2(title, float2Provider.asPrimitiveArray(), min, max, null, sliderFlags)) {
            float2Provider.writeUpdatedValues();
        }

    }
}
