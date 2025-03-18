package art.sol.imgui.panels;

import art.sol.Body;
import art.sol.imgui.widgets.ADebugPanel;
import imgui.ImGui;

public class BodyDebugPanel extends ADebugPanel {
    private Body selection;

    @Override
    public void renderContent () {
        if (selection != null) {
//            ImGui.sliderFloat2("Position", )
        }
    }


    @Override
    protected String getTitle () {
        return "Body";
    }
}
