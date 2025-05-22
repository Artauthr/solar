package art.sol.imgui.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;

public class TextureDebugPanel extends ADebugPanel {
    private final Array<Texture> debugTextures = new Array<>();
    private Texture current;

    public void add (Texture texture) {
        this.debugTextures.add(texture);
    }

    @Override
    public void renderContent () {
        ImGui.beginTabBar("Textures");
        for (Texture debugTexture : debugTextures) {
            if (ImGui.tabItemButton("1")) {
                current = debugTexture;
            }
        }

        ImGui.endTabBar();

        if (current != null) {
            ImGui.image(current.getTextureObjectHandle(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 1, 1, 0);
        }
    }

    @Override
    protected String getTitle () {
        return "Texture";
    }
}
