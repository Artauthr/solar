package art.sol.imgui;

import art.sol.imgui.panels.BodyDebugPanel;
import art.sol.imgui.panels.RenderingDebugPanel;
import art.sol.imgui.panels.TextureDebugPanel;
import art.sol.imgui.panels.WorldPanel;
import art.sol.imgui.panels.ADebugPanel;
import com.badlogic.gdx.utils.Array;

public class ImGuiStage {
    private final Array<ADebugPanel> debugWindows;

    public ImGuiStage () {
        debugWindows = new Array<>();
        debugWindows.add(new BodyDebugPanel());
        debugWindows.add(new WorldPanel());
        debugWindows.add(new TextureDebugPanel());
        debugWindows.add(new RenderingDebugPanel());
    }

    public void draw () {
        for (ADebugPanel debugWindow : debugWindows) {
            debugWindow.render();
        }
    }
}
