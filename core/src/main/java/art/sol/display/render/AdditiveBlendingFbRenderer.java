package art.sol.display.render;

import art.sol.Body;
import art.sol.display.DoubleFbGaussianBlur;
import art.sol.display.ManagedFrameBuffer;
import art.sol.display.ShaderManager;
import art.sol.display.StarBackgroundFrameBuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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

    private ManagedFrameBuffer lightsFb;
    private ManagedFrameBuffer planetsFb;
    private ManagedFrameBuffer trailFb;

    private final ShaderProgram glowFadeShader;
    private final ShaderProgram radialShader;

    @Getter
    private final DoubleFbGaussianBlur blur;


    @Getter
    private final StarBackgroundFrameBuffer starBackgroundFrameBuffer;

    private final Color lightPassColor = new Color(1f, 1f, 1f, 0.5f);

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
        blur = new DoubleFbGaussianBlur();

        lightsFb = new ManagedFrameBuffer();
        planetsFb = new ManagedFrameBuffer();
        trailFb = new ManagedFrameBuffer();
    }

    @Override
    public void drawBodies (Array<Body> bodies) {
        drawLightPass(bodies);
        drawTracePass(bodies);
        drawPlanets(bodies);

        blur.draw(spriteBatch);

        // draw all frameBuffers
        spriteBatch.begin();
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.setProjectionMatrix(spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch.draw(starBackgroundFrameBuffer.getTextureRegion(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (drawPlanetsPass) {
            spriteBatch.draw(planetsFb.getTextureRegion(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (drawLightPass) {
            spriteBatch.draw(lightsFb.getTextureRegion(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (drawTracePass) {
            spriteBatch.draw(trailFb.getTextureRegion(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        spriteBatch.enableBlending();
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        TextureRegion blurTextureRegion = blur.getTextureRegion();
        spriteBatch.draw(blurTextureRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        spriteBatch.end();
    }


    private void drawLightPass (Array<Body> bodies) {
        // render glow light textures to lightFrameBuffer
        lightsFb.begin();
        Gdx.gl.glClearColor(0,0,0,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.enableBlending();
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

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

        lightsFb.end();

    }

    private void drawTracePass (Array<Body> bodies) {
        // render traces that planets leave when moving
        trailFb.begin();
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
        trailFb.end();
    }

    private void drawPlanets (Array<Body> bodies) {
        // just draw planets
        planetsFb.begin();
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
        planetsFb.end();
    }

    @Override
    public void onResize(int width, int height) {
        super.onResize(width, height);
//        starBackgroundFrameBuffer.getScreenViewport().update(width, height, true);
//        starBackgroundFrameBuffer.regenerate();
//
//        starBackgroundFrameBuffer.drawToFrameBuffer(lightTexture);
    }


    @Override
    public void dispose() {
        spriteBatch.dispose();
        lightsFb.dispose();
        trailFb.dispose();
        lightTexture.dispose();
        circleTexture.dispose();
        glowFadeoutTexture.dispose();
        planetsFb.dispose();
        glowFadeShader.dispose();
    }

    public void regenStars () {

//        starBackgroundFrameBuffer.regenerate();
//        starBackgroundFrameBuffer.drawToFrameBuffer(lightTexture);
    }
}
