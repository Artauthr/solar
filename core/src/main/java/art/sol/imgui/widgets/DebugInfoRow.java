package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.FloatProvider;
import art.sol.valueproviders.ValueProvider;
import imgui.ImGui;

public class DebugInfoRow implements DebugRenderable {
    private final String name;
    private final FloatProvider valueProvider;

    public DebugInfoRow (String name, FloatProvider floatProvider) {
        this.name = name;
        this.valueProvider = floatProvider;
    }

    @Override
    public void render() {
        ImGui.labelText(String.valueOf(valueProvider.get()), name);
    }
}
