package art.sol.imgui.widgets;

import imgui.ImGui;

public abstract class ADebugPanel {
    protected void begin () {
        ImGui.begin(getTitle());
    }

    abstract public void renderContent();

    protected String getTitle () {
        return "Debug";
    }

    private void end () {
        ImGui.end();
    }

    public final void render () {
        begin();
        renderContent();
        end();
    }
}
