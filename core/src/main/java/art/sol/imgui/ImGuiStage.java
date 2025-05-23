package art.sol.imgui;

import art.sol.imgui.panels.*;
import com.badlogic.gdx.utils.Array;

public class ImGuiStage {
    private final Array<ADebugPanel> debugWindows;

    public ImGuiStage () {
        debugWindows = new Array<>();
        debugWindows.add(new BodyDebugPanel());
        debugWindows.add(new WorldPanel());
        debugWindows.add(new TextureDebugPanel());
        debugWindows.add(new RenderingDebugPanel());
        debugWindows.add(new AdditiveRenderingDebugPanel());
    }

    public void draw () {
        for (ADebugPanel debugWindow : debugWindows) {
            debugWindow.render();
        }
    }
}
