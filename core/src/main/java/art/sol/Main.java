package art.sol;

import art.sol.display.ShaderManager;
import art.sol.display.render.*;
import art.sol.imgui.ImGuiController;
import art.sol.input.InputController;
import art.sol.ui.UIController;
import art.sol.util.PixmapTextureGenerator;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {
    private SolarSystem solarSystem;
    private UIController uiController;
    private ImGuiController imGuiController;

    private ARenderer renderer;
    private ShapeRenderer debugShapeRenderer;

    public static final int WORLD_WIDTH = 300;
    public static final int WORLD_HEIGHT = 200;
    public static final boolean PROFILING_ENABLED = true;

    @Override
    public void create () {
        init();

        Body body = new Body(100000, 10);
        body.getVelocity().set(0f, 0f);
//        body.setColor(Color.SLATE);
        body.getPosition().set(WORLD_WIDTH / 2f + body.getRadius(), WORLD_HEIGHT / 2f);
//        body.getPosition().set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
//        body.getPosition().set(20, 45);

        Body body1 = new Body(2, 3);
        body1.getVelocity().set(0, 20f);
//        body1.setColor(Color.WHITE);
        body1.getPosition().set(WORLD_WIDTH / 2f - 36, WORLD_HEIGHT / 2f - 4);

        Body body2 = new Body(3, 2);
        body2.getVelocity().set(2, 13);
        body2.setColor(Color.GOLDENROD);
        body2.getPosition().set(WORLD_WIDTH / 2f + 23, WORLD_HEIGHT / 2f + 8);
//        body2.getPosition().set(800, 350);

        Body body3 = new Body(3, 5);
        body3.getVelocity().set(4, 10);
        body3.setColor(Color.PINK);
//        body3.getPosition().set(800, 250);
        body3.getPosition().set(WORLD_WIDTH / 2f - 23, WORLD_HEIGHT / 2f + 8);

        Body body4 = new Body(3, 12);
        body4.getVelocity().set(4, 14);
        body4.setColor(Color.ROYAL);
        body4.getPosition().set(500, 350);

        solarSystem.addBody(body);
        solarSystem.addBody(body1);
//        solarSystem.addBody(body2);
//        solarSystem.addBody(body3);
//        solarSystem.addBody(body4);

    }

    private void init () {
        API.getInstance().register(new ShaderManager());

        GraphicsUtils graphicsUtils = new GraphicsUtils();

        if (PROFILING_ENABLED) {
            GLProfiler glProfiler = new GLProfiler(Gdx.graphics);
            glProfiler.enable();

            API.getInstance().register(glProfiler);
        }

        solarSystem = new SolarSystem();
        uiController = new UIController();

        API.getInstance().register(graphicsUtils);
        API.getInstance().register(solarSystem);
        API.getInstance().register(uiController);

        InputController inputController = new InputController();
        API.getInstance().register(inputController);

        this.renderer = createRenderer();
        graphicsUtils.setRenderer(renderer);

        uiController.init();

        inputController.addProcessor(uiController.getStage());

        API.get(GraphicsUtils.class).setUiViewport(uiController.getStage().getViewport());

        inputController.activate();
        imGuiController = new ImGuiController();
    }

    private ARenderer createRenderer () {
        final OrthographicCamera camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        Viewport viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        API.get(GraphicsUtils.class).setGameViewport(viewport);

        debugShapeRenderer = new ShapeRenderer(5000, ShaderManager.getOrCreateShader("default"));
        debugShapeRenderer.setAutoShapeType(true);

//        ScreenViewport viewport = new ScreenViewport(camera);
//        viewport.setWorldSize(WORLD_WIDTH, WORLD_HEIGHT);
        API.get(GraphicsUtils.class).setGameViewport(viewport);

        return new AdditiveBlendingFbRenderer(viewport);
    }

    @Override
    public void render () {
        solarSystem.update();

        renderer.drawBodies(solarSystem.getBodies());

        uiController.act(Gdx.graphics.getDeltaTime());
        uiController.draw();

        imGuiController.render();

        if (PROFILING_ENABLED) {
            GLProfiler glProfiler = API.get(GLProfiler.class);
            glProfiler.reset();
        }

        debugShapeRenderer.setProjectionMatrix(API.get(GraphicsUtils.class).getGameViewport().getCamera().combined);

        debugShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugShapeRenderer.setColor(Color.RED);
//        debugShapeRenderer.line(0, Gdx.graphics.getHeight() * 0.5f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 0.5f);
//        debugShapeRenderer.line(Gdx.graphics.getWidth() * 0.5f, 0, Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight());

        debugShapeRenderer.end();
    }

    @Override
    public void resize (int width, int height) {
        super.resize(width, height);
        renderer.onResize(width, height);

        API.get(UIController.class).onResize(width, height);
    }

    @Override
    public void dispose () {
        renderer.dispose();
        uiController.dispose();
        imGuiController.dispose();
    }
}
