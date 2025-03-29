package art.sol.imgui.panels;

import art.sol.API;
import art.sol.imgui.widgets.ADebugPanel;
import art.sol.imgui.widgets.DebugIntRow;
import art.sol.valueproviders.io.IntReader;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;

public class RenderingDebugPanel extends ADebugPanel {
    private final Array<DebugIntRow> rows = new Array<>();

    public RenderingDebugPanel () {
        makeRow("Draw calls:", () -> API.get(GLProfiler.class).getDrawCalls());
        makeRow("Texture binds:", () -> API.get(GLProfiler.class).getTextureBindings());
        makeRow("Shader switches:", () -> API.get(GLProfiler.class).getShaderSwitches());
        makeRow("Vertex amount:", () -> ((int) API.get(GLProfiler.class).getVertexCount().max));
        makeRow("Calls:", () -> API.get(GLProfiler.class).getCalls());
    }

    private void makeRow (String name, IntReader intReader) {
        rows.add(new DebugIntRow(name, intReader));
    }

    @Override
    public void renderContent () {
        for (int i = 0; i < rows.size; i++) {
            DebugIntRow row = rows.get(i);
            if (i > 0 && i < rows.size) {
                ImGui.separator();
            }
            ImGui.pushItemWidth(110f);
            row.render();
        }
    }

    @Override
    protected String getTitle () {
        return "GPU";
    }
}
