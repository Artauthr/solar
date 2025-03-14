package art.sol.display.render;

import art.sol.Body;
import art.sol.display.GBuffer;
import art.sol.display.ShaderLoader;
import art.sol.display.Shaders;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LightMapSpriteBatchRender extends ARenderer {
    private final SpriteBatch spriteBatch;
    private final ShaderProgram shaderProgram;
    private final GBuffer gBuffer;

    private final Texture circleTexture;

    public LightMapSpriteBatchRender (Viewport viewport) {
        super(viewport);

        this.shaderProgram = ShaderLoader.load(Shaders.MULTI_LIGHT_CAST.getName());
        this.spriteBatch = new SpriteBatch(1500, shaderProgram);

        circleTexture = new Texture(Gdx.files.internal("sprites/glowCircle.png"));

    }

    @Override
    public void drawBodies(Array<Body> bodies) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        for (Body body : bodies) {
            int size = body.getRadius() * 2;

            spriteBatch.setColor(body.getColor());
            spriteBatch.draw(circleTexture, body.getPosition().x, body.getPosition().y, size, size);
        }

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
