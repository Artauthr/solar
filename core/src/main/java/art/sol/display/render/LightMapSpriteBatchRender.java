package art.sol.display.render;

import art.sol.Body;
import art.sol.display.ShaderLoader;
import art.sol.display.Shaders;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LightMapSpriteBatchRender extends ARenderer {
    private final SpriteBatch spriteBatch;
    private final ShaderProgram traceShader;

    private final Texture circleTexture;
    private final ShaderProgram atmosphereShader;

    public LightMapSpriteBatchRender (Viewport viewport) {
        super(viewport);

        this.traceShader = ShaderLoader.load(Shaders.TRACE.getName());
        this.atmosphereShader = ShaderLoader.load(Shaders.ATMOSPHERE.getName());

        this.spriteBatch = new SpriteBatch(1500, atmosphereShader);

        circleTexture = new Texture(Gdx.files.internal("sprites/circle.png"));
    }

    private Vector3 colorVector = new Vector3();
    private float timer = 0f;

    @Override
    public void drawBodies(Array<Body> bodies) {
        ScreenUtils.clear(0.06f, 0.06f, 0.06f, 1f);

        timer += Gdx.graphics.getDeltaTime();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

//        atmosphereShader.setUniformf("u_resolution", 1000, 1000);
//        atmosphereShader.setUniformf("u_time", timer);
        spriteBatch.begin();
        Body first = bodies.first();
        spriteBatch.draw(circleTexture,first.getPosition().x, first.getPosition().y, first.getRadius() * 2, first.getRadius() * 2);

//        for (Body body : bodies) {
//            int size = body.getRadius() * 2;

//            Color color = body.getColor();
//            colorVector.set(color.r, color.g, color.b);
//            colorVector.set(1f, 0.1f, 0.1f);

//            traceShader.setUniformf("u_glowColor", colorVector);
//            traceShader.setUniformf("u_intensity", 39f);

//            atmosphereShader.setUniformf("u_time", timer);
//
//            spriteBatch.setColor(body.getColor());
//            spriteBatch.draw(circleTexture, body.getPosition().x, body.getPosition().y, size, size);
//        }

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        traceShader.dispose();
        atmosphereShader.dispose();
    }
}
