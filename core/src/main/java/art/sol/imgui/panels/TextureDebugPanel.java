package art.sol.imgui.panels;

import art.sol.imgui.widgets.ADebugPanel;
import art.sol.util.Supplier;
import com.badlogic.gdx.graphics.Texture;
import imgui.ImGui;

public class TextureDebugPanel extends ADebugPanel {
    private final Supplier<Texture> textureSupplier;

    public TextureDebugPanel (Supplier<Texture> textureSupplier) {
        this.textureSupplier = textureSupplier;
    }

    @Override
    public void renderContent () {
        Texture debugTexture = textureSupplier.get();
        if (debugTexture == null) {
            return;
        }

        ImGui.image(debugTexture.getTextureObjectHandle(), 400f, 200f, 0, 1, 1, 0);
    }

    @Override
    protected String getTitle () {
        return "Texture";
    }
}
