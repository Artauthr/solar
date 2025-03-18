package art.sol.imgui.panels;

import art.sol.API;
import art.sol.imgui.widgets.ADebugPanel;
import art.sol.imgui.widgets.DebugInfoRow;
import art.sol.valueproviders.FloatProvider;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;

public class RenderingDebugPanel extends ADebugPanel {
    private final Array<DebugInfoRow> rows = new Array<>();

    public RenderingDebugPanel () {
        makeRow("Draw calls:", new FloatProvider() {
            @Override
            public float readValue () {
                return API.get(GLProfiler.class).getDrawCalls();
            }
        });
        makeRow("Texture binds:", new FloatProvider() {
            @Override
            public float readValue () {
                return API.get(GLProfiler.class).getTextureBindings();
            }
        });
        makeRow("Shader switches:", new FloatProvider() {
            @Override
            public float readValue () {
                return API.get(GLProfiler.class).getShaderSwitches();
            }
        });
        makeRow("Vertex amount:", new FloatProvider() {
            @Override
            public float readValue () {
                return API.get(GLProfiler.class).getVertexCount().max;
            }
        });
        makeRow("Calls:", new FloatProvider() {
            @Override
            public float readValue () {
                return API.get(GLProfiler.class).getCalls();
            }
        });
    }

    private void makeRow (String name, FloatProvider floatProvider) {
        rows.add(new DebugInfoRow(name, floatProvider));
    }

    @Override
    public void renderContent () {
        for (int i = 0; i < rows.size; i++) {
            DebugInfoRow row = rows.get(i);
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
