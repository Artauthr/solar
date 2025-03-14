package art.sol;

import art.sol.display.render.ARenderer;
import art.sol.display.render.LightMapSpriteBatchRender;
import art.sol.display.render.SpriteBatchRenderer;
import art.sol.display.render.SimpleShapeRenderer;
import art.sol.ui.UIController;
import art.sol.util.PixmapTextureGenerator;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {
    private SolarSystem solarSystem;
    private UIController uiController;

    private ARenderer renderer;
    private Viewport viewport;

    private static final float WORLD_WIDTH = 240;
    private static final float WORLD_HEIGHT = 150;

    public enum RendererType {
        REGULAR_SHAPE_RENDERER,
        SPRITE_BATCH_RENDERER,
        LIGHTMAP_BATCH_RENDERER
    }

    @Override
    public void create() {
        init();
        PixmapTextureGenerator.generateGlowTexture("circle", 30);


        Body body = new Body(12000, 7);
        body.getVelocity().set(0f, 0f);
        body.setColor(Color.SLATE);
        body.getPosition().set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2 - 5);

        Body body1 = new Body(5, 3);
        body1.getVelocity().set(0, 14);
        body1.setColor(Color.WHITE);
        body1.getPosition().set(WORLD_WIDTH / 2 + 20, WORLD_HEIGHT / 2 - 10);

        Body body2 = new Body(3, 3);
        body2.getVelocity().set(0, 13);
        body2.setColor(Color.GOLDENROD);
        body2.getPosition().set(WORLD_WIDTH / 2 - 30, WORLD_HEIGHT / 2 - 20);


        solarSystem.addBody(body);
        solarSystem.addBody(body1);
        solarSystem.addBody(body2);


        API.getInstance().register(solarSystem);
        API.getInstance().register(uiController);

        uiController.init();
    }

    private void init () {
        solarSystem = new SolarSystem();

        final OrthographicCamera camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT/ 2f, 0);
        camera.update();

        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        renderer = createRenderer(RendererType.SPRITE_BATCH_RENDERER);

        uiController = new UIController();
    }

    private ARenderer createRenderer (RendererType rendererType) {
        switch (rendererType) {
            case REGULAR_SHAPE_RENDERER :
                return new SimpleShapeRenderer(viewport);
            case SPRITE_BATCH_RENDERER:
                return new SpriteBatchRenderer(viewport);
            case LIGHTMAP_BATCH_RENDERER:
                return new LightMapSpriteBatchRender(viewport);
        }
        return null;
    }

    @Override
    public void render() {
        solarSystem.updateVelocities();
        solarSystem.updatePositions();

        renderer.drawBodies(solarSystem.getBodies());

        uiController.act(Gdx.graphics.getDeltaTime());
        uiController.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);

        API.get(UIController.class).onResize(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        uiController.dispose();
    }
}
