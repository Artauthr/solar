package art.sol.display.render;

import art.sol.API;
import art.sol.Body;
import art.sol.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdditiveBlendingFbRenderer extends ARenderer {
    private static final Logger log = LoggerFactory.getLogger(AdditiveBlendingFbRenderer.class);
    private final SpriteBatch spriteBatch;
    private final Texture circleTexture;
    private final Texture lightTexture;

    private TextureRegion frameBufferTextureRegion;
    private FrameBuffer lightFrameBuffer;

    private final static float BACKGROUND_COLOR_VALUE = 0.06f;

    public AdditiveBlendingFbRenderer (Viewport viewport) {
        super(viewport);

        this.spriteBatch = new SpriteBatch();

        circleTexture = new Texture(Gdx.files.internal("sprites/circle.png"));
        circleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        lightTexture = new Texture(Gdx.files.internal("sprites/glow-circle.png"));
        lightTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        createFrameBuffer();
    }

    private void createFrameBuffer () {
        // TODO: 16.03.25 maybe not needed
        if (lightFrameBuffer != null) {
            lightFrameBuffer.dispose();
        }
        int fbWidth = Gdx.graphics.getWidth();
        int fbHeight = Gdx.graphics.getHeight();
//
//        int fbWidth = Main.WORLD_WIDTH;
//        int fbHeight = Main.WORLD_HEIGHT;
        lightFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, fbWidth, fbHeight, true);
        lightFrameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        frameBufferTextureRegion = new TextureRegion(lightFrameBuffer.getColorBufferTexture());
        frameBufferTextureRegion.flip(false, true);

        log.info("Created Frame Buffer");
    }

    private Color debugColor = new Color(0.5f, 0.5f, 0.5f, 0.3f);


    @Override
    public void drawBodies (Array<Body> bodies) {
        lightFrameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();


        /* LIGHTING PASS **/
        for (Body body : bodies) {
            Vector2 position = body.getPosition();
            Color color = body.getColor();
            int radius = body.getRadius();
            int size = radius * 2 * 6;

            spriteBatch.setColor(color);
            spriteBatch.draw(lightTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }


        spriteBatch.end();
        lightFrameBuffer.end();


        // main pass
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(BACKGROUND_COLOR_VALUE, BACKGROUND_COLOR_VALUE, BACKGROUND_COLOR_VALUE, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        spriteBatch.begin();
        for (Body body : bodies) {
            Vector2 position = body.getPosition();
            Color color = body.getColor();
            int radius = body.getRadius();
            int size = radius * 2;

            spriteBatch.setColor(color);
            spriteBatch.draw(circleTexture, position.x - radius, position.y - radius, size, size);
        }

        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(frameBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spriteBatch.end();
    }

    @Override
    public void onResize(int width, int height) {
        super.onResize(width, height);
//        createFrameBuffer();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        lightFrameBuffer.dispose();
        lightTexture.dispose();
        circleTexture.dispose();
    }
}
