package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.Float2Provider;
import art.sol.valueproviders.Vec2Provider;
import com.badlogic.gdx.math.Vector2;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import lombok.Setter;

public class DebugFloat2Slider implements DebugRenderable {
    private Float2Provider float2Provider;

    @Setter
    private String title;

    private final float min;
    private final float max;
    private final int sliderFlags;

    public DebugFloat2Slider (String title, float min, float max) {
        this(title, null, min, max, ImGuiSliderFlags.None);
    }

    public DebugFloat2Slider (String title, Vector2 target, float min, float max) {
        this(title, target, min, max, ImGuiSliderFlags.None);
    }

    public DebugFloat2Slider (String title, Vector2 target, float min, float max, int sliderFlags) {
        this.sliderFlags = sliderFlags;
        this.min = min;
        this.max = max;
        this.title = title;

        this.float2Provider = new Vec2Provider(target);
    }

    public void setTargetVector (Vector2 targetVector) {
        this.float2Provider = new Vec2Provider(targetVector);
    }

    @Override
    public final void render() {
        if (float2Provider == null) return;

        if (ImGui.sliderFloat2(title, float2Provider.asPrimitiveArray(), min, max, null, sliderFlags)) {
            float2Provider.writeUpdatedValues();
        }
    }
}
