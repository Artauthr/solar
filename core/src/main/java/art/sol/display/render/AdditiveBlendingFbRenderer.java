package art.sol.display.render;

import art.sol.Body;
import art.sol.display.ShaderManager;
import art.sol.display.StarBackgroundFrameBuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdditiveBlendingFbRenderer extends ARenderer {
    private static final Logger log = LoggerFactory.getLogger(AdditiveBlendingFbRenderer.class);
    private final SpriteBatch spriteBatch;
    private final Texture circleTexture;
    private  Texture lightTexture;

    private final Texture glowFadeoutTexture;

    @Getter
    private TextureRegion lightBufferTextureRegion;
    private FrameBuffer lightFrameBuffer;

    private FrameBuffer planetsFrameBuffer;
    private TextureRegion planetsBufferTextureRegion;

    @Getter
    private TextureRegion trailBufferTextureRegion;
    private FrameBuffer trailFrameBuffer;

    private final ShaderProgram glowFadeShader;

    @Getter
    private StarBackgroundFrameBuffer starBackgroundFrameBuffer;

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

        starBackgroundFrameBuffer = new StarBackgroundFrameBuffer();
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

        // planets
        if (planetsFrameBuffer != null) {
            planetsFrameBuffer.dispose();
        }

        planetsFrameBuffer = createFrameBuffer();
        planetsBufferTextureRegion = new TextureRegion(planetsFrameBuffer.getColorBufferTexture());
        planetsBufferTextureRegion.flip(false, true);
    }

    private FrameBuffer createFrameBuffer () {
        final int fbWidth = Gdx.graphics.getWidth();
        final int fbHeight = Gdx.graphics.getHeight();

        final FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, fbWidth, fbHeight, true);
        fb.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        log.info("Created Frame Buffer");
        return fb;
    }

    @Override
    public void drawBodies (Array<Body> bodies) {
        drawLightPass(bodies);
        drawTracePass(bodies);
        drawPlanets(bodies);

        starBackgroundFrameBuffer.drawToFrameBuffer(lightTexture);
        // draw all frameBuffers
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_ALPHA);

        spriteBatch.begin();
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.setProjectionMatrix(spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // draw trail fb with a shader that removes near 0 alpha pixels
        spriteBatch.draw(starBackgroundFrameBuffer.getTextureRegion(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.draw(planetsBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.draw(lightBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spriteBatch.setShader(glowFadeShader);
        spriteBatch.draw(trailBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.setShader(null);


        spriteBatch.end();
    }

    private void drawLightPass (Array<Body> bodies) {
        // render glow light textures to lightFrameBuffer
        lightFrameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        for (Body body : bodies) {
            Vector2 position = body.getPosition();
            Color color = body.getColor();
            float radius = body.getRadius();
            float size = radius * body.getLightEmission();
            spriteBatch.setColor(color);
            spriteBatch.draw(lightTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }

        spriteBatch.end();
        lightFrameBuffer.end();
    }

    private void drawTracePass (Array<Body> bodies) {
        // render traces that planets leave when moving
        trailFrameBuffer.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_ALPHA);

        // draw a rectangle with alpha to gradually make existing glows transparent
        spriteBatch.setProjectionMatrix(spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch.begin();
        spriteBatch.setColor(0f, 0f, 0f, 0.02f);
        spriteBatch.draw(glowFadeoutTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        // draw the light textures
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
    }

    private void drawPlanets (Array<Body> bodies) {
        // just draw planets
        planetsFrameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        for (Body body : bodies) {
            Vector2 position = body.getPosition();
            Color color = body.getColor();
            float radius = body.getRadius();
            float size = radius * 2;

            spriteBatch.setColor(color);
            spriteBatch.draw(circleTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }
        spriteBatch.end();
        planetsFrameBuffer.end();
    }

    @Override
    public void onResize(int width, int height) {
        super.onResize(width, height);
        invalidateFrameBuffers();
        starBackgroundFrameBuffer.getScreenViewport().update(width, height, true);
        starBackgroundFrameBuffer.regenerate();

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        lightFrameBuffer.dispose();
        trailFrameBuffer.dispose();
        lightTexture.dispose();
        circleTexture.dispose();
        glowFadeoutTexture.dispose();
        planetsFrameBuffer.dispose();
        glowFadeShader.dispose();
    }
}
