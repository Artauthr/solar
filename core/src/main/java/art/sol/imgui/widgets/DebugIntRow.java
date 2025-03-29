package art.sol.imgui.widgets;

import art.sol.imgui.DebugRenderable;
import art.sol.valueproviders.io.IntReader;
import imgui.ImGui;

public class DebugIntRow implements DebugRenderable {
    private final String name;
    private final IntReader valueProvider;

    public DebugIntRow (String name, IntReader intReader) {
        this.name = name;
        this.valueProvider = intReader;
    }

    @Override
    public void render() {
        ImGui.labelText(String.valueOf(valueProvider.read()), name);
    }
}
