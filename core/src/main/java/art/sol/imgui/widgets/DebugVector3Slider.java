package art.sol.imgui.widgets;

import art.sol.valueproviders.io.Vector3Reader;
import com.badlogic.gdx.math.Vector3;
import imgui.ImGui;

public class DebugVector3Slider {
    private final String name;
    private float min;
    private float max;

    private final float[] data = new float[3];

    public DebugVector3Slider (String name) {
        this.name = name;
    }

    public void setConstraints (float min, float max) {
        this.min = min;
        this.max = max;
    }

    public void render (Vector3Reader reader) {
        Vector3 targetVector = reader.read();

        data[0] = targetVector.x;
        data[1] = targetVector.y;
        data[2] = targetVector.z;

        if (ImGui.sliderFloat3(name, data, min, max)) {
            targetVector.set(data[0], data[1], data[2]);
        }
    }
}
