package art.sol.display.render;

import art.sol.Body;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SpriteBatchRenderer extends ARenderer {
    private final SpriteBatch spriteBatch;
    private final Texture circleTexture;

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

    public SpriteBatchRenderer(Viewport viewport) {
        super(viewport);

        this.spriteBatch = new SpriteBatch();
        circleTexture = new Texture(Gdx.files.internal("sprites/glowCircle.png"));
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
