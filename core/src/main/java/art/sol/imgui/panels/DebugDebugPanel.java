package art.sol.imgui.panels;

import art.sol.API;
import art.sol.display.render.ARenderer;
import art.sol.display.render.AdditiveBlendingFbRenderer;
import art.sol.display.render.GraphicsUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import imgui.ImGui;

public class DebugDebugPanel extends ADebugPanel {
    @Override
    public void renderContent () {
        ARenderer renderer = API.get(GraphicsUtils.class).getRenderer();
        if (!(renderer instanceof AdditiveBlendingFbRenderer)) {
            return;
        }

        AdditiveBlendingFbRenderer caster = (AdditiveBlendingFbRenderer) renderer;
        TextureRegion textureRegion = caster.getBlur().getTextureRegion();

        ImGui.image(textureRegion.getTexture().getTextureObjectHandle(),
                Gdx.graphics.getWidth() * 0.33f, Gdx.graphics.getHeight() * 0.33f,
                0, 1, 1, 0);
    }

    @Override
    protected String getTitle () {
        return "DebugDebuggg";
    }
}