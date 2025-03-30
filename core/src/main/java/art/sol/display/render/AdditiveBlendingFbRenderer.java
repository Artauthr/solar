package art.sol.display.render;

import art.sol.Body;
import art.sol.Main;
import art.sol.display.ShaderManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdditiveBlendingFbRenderer extends ARenderer {
    private static final Logger log = LoggerFactory.getLogger(AdditiveBlendingFbRenderer.class);
    private final SpriteBatch spriteBatch;
    private final Texture circleTexture;
    private final Texture lightTexture;

    private final Texture glowFadeoutTexture;

    @Getter
    private TextureRegion lightBufferTextureRegion;
    private FrameBuffer lightFrameBuffer;

    @Getter
    private TextureRegion trailBufferTextureRegion;
    private FrameBuffer trailFrameBuffer;

    private ShaderProgram glowFadeShader;

    public static final float GLOW_DECAY_RATE = 0.05f;

    private final static float BACKGROUND_COLOR_VALUE = 0.06f;

    public AdditiveBlendingFbRenderer (Viewport viewport) {
        super(viewport);

        ShaderProgram defaultShader = ShaderManager.getOrCreateShader("default");
        this.spriteBatch = new SpriteBatch(1000, defaultShader);

        glowFadeShader = ShaderManager.getOrCreateShader("fade");

        circleTexture = new Texture(Gdx.files.internal("sprites/circle.png"));
        circleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        lightTexture = new Texture(Gdx.files.internal("sprites/glow-circle.png"));
        lightTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        glowFadeoutTexture = new Texture(Gdx.files.internal("sprites/rect.png"));
        glowFadeoutTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void invalidateFrameBuffers () {
        if (lightFrameBuffer != null) {
            lightFrameBuffer.dispose();
        }
        lightFrameBuffer = createFrameBuffer();
        lightBufferTextureRegion = new TextureRegion(lightFrameBuffer.getColorBufferTexture());
        lightBufferTextureRegion.flip(false, true);

        // trail
        if (trailFrameBuffer != null) {
            trailFrameBuffer.dispose();
        }

        trailFrameBuffer = createFrameBuffer();
        trailBufferTextureRegion = new TextureRegion(trailFrameBuffer.getColorBufferTexture());
        trailBufferTextureRegion.flip(false, true);
    }

    private FrameBuffer createFrameBuffer () {
        int fbWidth = Main.WORLD_WIDTH;
        int fbHeight = Main.WORLD_HEIGHT;
        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, fbWidth, fbHeight, true);
        fb.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        log.info("Created Frame Buffer");
        return fb;
    }

    @Override
    public void drawBodies (Array<Body> bodies) {
        // LIGHTING PASS (render light textures to lightFrameBuffer)
        lightFrameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        for (Body body : bodies) {
            Vector2 position = body.getPosition();
            Color color = body.getColor();
            float radius = body.getRadius();
            float size = radius * 2 * 6;
            spriteBatch.setColor(color);
            spriteBatch.draw(lightTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }
        spriteBatch.end();
        lightFrameBuffer.end();
        //

        // GLOWING PASS (render traces)
        trailFrameBuffer.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_ZERO, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_ALPHA);


        spriteBatch.setProjectionMatrix(spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch.begin();

        spriteBatch.setColor(0f, 0f, 0f, 0.02f);
        spriteBatch.draw(glowFadeoutTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_ALPHA);
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        for (Body body : bodies) {
            Vector2 position = body.getPosition();
            Color color = body.getColor();
            float radius = body.getRadius();
            float size = radius * 2f;
            spriteBatch.setColor(color);
            spriteBatch.draw(lightTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }
        spriteBatch.end();
        trailFrameBuffer.end();
        //




        // PLANETS PASS (draw planets)
//        Gdx.gl.glClearColor(0, 0, 0, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        for (Body body : bodies) {
            Vector2 position = body.getPosition();
            Color color = body.getColor();
            float radius = body.getRadius();
            float size = radius * 2;

            spriteBatch.setColor(color);
            spriteBatch.draw(circleTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }
        spriteBatch.end();
        //

        // FB PASS (draw all framebuffers)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_ALPHA);

        spriteBatch.begin();
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.setProjectionMatrix(spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        spriteBatch.setShader(glowFadeShader);
        spriteBatch.draw(trailBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.setShader(null);

        spriteBatch.draw(lightBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spriteBatch.end();


    }

    @Override
    public void onResize(int width, int height) {
        super.onResize(width, height);
        invalidateFrameBuffers();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        lightFrameBuffer.dispose();
        lightTexture.dispose();
        circleTexture.dispose();
        glowFadeShader.dispose();
    }
}
