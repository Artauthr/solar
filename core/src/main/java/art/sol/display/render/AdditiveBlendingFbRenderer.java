package art.sol.display.render;

import art.sol.Body;
import art.sol.display.ShaderManager;
import art.sol.display.StarBackgroundFrameBuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdditiveBlendingFbRenderer extends ARenderer {
    private static final Logger log = LoggerFactory.getLogger(AdditiveBlendingFbRenderer.class);

    private final SpriteBatch spriteBatch;

    private final Texture circleTexture;
    private final Texture lightTexture;
    private final Texture glowFadeoutTexture;


    private FrameBuffer lightFrameBuffer;
    private FrameBuffer planetsFrameBuffer;
    private FrameBuffer trailFrameBuffer;

    @Getter
    private TextureRegion lightBufferTextureRegion;

    @Getter
    private TextureRegion planetsBufferTextureRegion;

    @Getter
    private TextureRegion trailBufferTextureRegion;

    private final ShaderProgram glowFadeShader;
    private final ShaderProgram radialShader;

    @Getter
    private final StarBackgroundFrameBuffer starBackgroundFrameBuffer;

    @Getter
    @Setter
    private boolean drawLightPass = true;
    @Getter
    @Setter
    private boolean drawTracePass = true;
    @Getter
    @Setter
    private boolean drawPlanetsPass = true;

    public AdditiveBlendingFbRenderer (Viewport viewport) {
        super(viewport);

        ShaderProgram defaultShader = ShaderManager.getOrCreateShader("default");
        this.spriteBatch = new SpriteBatch(1000, defaultShader);

        glowFadeShader = ShaderManager.getOrCreateShader("fade");
        radialShader = ShaderManager.getOrCreateShader("radial");

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

        // draw all frameBuffers

//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        spriteBatch.begin();
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.setProjectionMatrix(spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

//        spriteBatch.disableBlending();
//        spriteBatch.enableBlending();
//        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        spriteBatch.draw(starBackgroundFrameBuffer.getTextureRegion(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (drawPlanetsPass) {
            spriteBatch.draw(planetsBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (drawLightPass) {
            spriteBatch.draw(lightBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (drawTracePass) {
            spriteBatch.draw(trailBufferTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }


        spriteBatch.end();
    }

    private Color lightPassColor = new Color(1f, 1f, 1f, 0.5f);

    private void drawLightPass (Array<Body> bodies) {
        // render glow light textures to lightFrameBuffer
        lightFrameBuffer.begin();
        Gdx.gl.glClearColor(0,0,0,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.enableBlending();
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        spriteBatch.setColor(lightPassColor);

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        for (Body body : bodies) {
            Vector3 position = body.getPosition();
            Color color = body.getColor();
            float radius = body.getRadius();
            float size = radius * 2 * body.getLightEmission();
            lightPassColor.set(color.r, color.g, color.b, 1f);

            spriteBatch.setColor(lightPassColor);
            spriteBatch.draw(lightTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }

        spriteBatch.setColor(Color.WHITE);
        spriteBatch.end();

        lightFrameBuffer.end();

    }

    private void drawTracePass (Array<Body> bodies) {
        // render traces that planets leave when moving
        trailFrameBuffer.begin();
        spriteBatch.enableBlending();
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

//        spriteBatch.setShader(glowFadeShader);
        // draw a rectangle with alpha to gradually make existing glows transparent
//        spriteBatch.setProjectionMatrix(spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
//        spriteBatch.begin();
//        spriteBatch.setColor(0f, 0f, 0f, 0.02f);
//        spriteBatch.draw(glowFadeoutTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        spriteBatch.end();

        // draw the light textures
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        spriteBatch.setShader(glowFadeShader);

        for (Body body : bodies) {
            Vector3 position = body.getPosition();
            Color color = body.getColor();
            float radius = body.getRadius();
            float size = radius * 2f;
            spriteBatch.setColor(color);
            spriteBatch.draw(lightTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }

        spriteBatch.setShader(ShaderManager.getOrCreateShader("default"));

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

        spriteBatch.setShader(radialShader);

        for (Body body : bodies) {
            Vector3 position = body.getPosition();
            Color color = body.getColor();
            float radius = body.getRadius();
            float size = radius * 2;

            spriteBatch.setColor(color);
            spriteBatch.draw(circleTexture, position.x - size * 0.5f, position.y - size * 0.5f, size, size);
        }

        spriteBatch.setShader(null);
        spriteBatch.end();
        planetsFrameBuffer.end();
    }

    @Override
    public void onResize(int width, int height) {
        super.onResize(width, height);
        invalidateFrameBuffers();
        starBackgroundFrameBuffer.getScreenViewport().update(width, height, true);
        starBackgroundFrameBuffer.regenerate();

        starBackgroundFrameBuffer.drawToFrameBuffer(lightTexture);
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

    public void regenStars () {

//        starBackgroundFrameBuffer.regenerate();
//        starBackgroundFrameBuffer.drawToFrameBuffer(lightTexture);
    }
}
