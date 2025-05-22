package art.sol.imgui.widgets;

import art.sol.valueproviders.io.BooleanReader;
import art.sol.valueproviders.io.BooleanWriter;
import imgui.ImGui;

public class DebugCheckboxWidget {
    private final String title;

    public DebugCheckboxWidget (String title) {
        this.title = title;
    }

    public void render (BooleanReader bReader, BooleanWriter bWriter) {
        boolean currentState = bReader.read();
        if (ImGui.checkbox(title, currentState)) {
            bWriter.write(!currentState);
        }
    }
}
