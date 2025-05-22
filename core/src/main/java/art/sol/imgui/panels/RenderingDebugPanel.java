package art.sol.imgui.panels;

import art.sol.API;
import art.sol.valueproviders.io.IntReader;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import imgui.ImGui;

public class RenderingDebugPanel extends ADebugPanel {

    private void drawRow (String label, IntReader intReader) {
        ImGui.labelText(String.valueOf(intReader.read()), label);
        ImGui.separator();
    }

    @Override
    public void renderContent () {
        ImGui.pushItemWidth(110f);
        drawRow("Draw calls:", () -> API.get(GLProfiler.class).getDrawCalls());
        drawRow("Texture binds:", () -> API.get(GLProfiler.class).getTextureBindings());
        drawRow("Shader switches:", () -> API.get(GLProfiler.class).getShaderSwitches());
        drawRow("Vertex amount:", () -> ((int) API.get(GLProfiler.class).getVertexCount().max));
        drawRow("Calls:", () -> API.get(GLProfiler.class).getCalls());
    }

    @Override
    protected String getTitle () {
        return "Rendering";
    }
}
