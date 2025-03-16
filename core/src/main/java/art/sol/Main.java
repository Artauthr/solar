package art.sol;

import art.sol.display.render.*;
import art.sol.input.InputController;
import art.sol.ui.UIController;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {
    private SolarSystem solarSystem;
    private UIController uiController;

    private ARenderer renderer;

    public static final int WORLD_WIDTH = 300;
    public static final int WORLD_HEIGHT = 200;

    @Override
    public void create() {
        init();

        Body body = new Body(100000, 40);
        body.getVelocity().set(0f, 0f);
        body.setColor(Color.SLATE);
//        body.getPosition().set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);
        body.getPosition().set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
//        body.getPosition().set(20, 45);

        Body body1 = new Body(40000, 40);
        body1.getVelocity().set(0, 20);
        body1.setColor(Color.WHITE);
        body1.getPosition().set(WORLD_WIDTH / 2f + 23, WORLD_HEIGHT / 2f + 8);

        Body body2 = new Body(3, 18);
        body2.getVelocity().set(2, 13);
        body2.setColor(Color.GOLDENROD);
//        body2.getPosition().set(WORLD_WIDTH / 2f + 23, WORLD_HEIGHT / 2f + 8);
        body2.getPosition().set(800, 350);

        Body body3 = new Body(3, 3);
        body3.getVelocity().set(4, 10);
        body3.setColor(Color.PURPLE);
        body3.getPosition().set(WORLD_WIDTH / 2 - 40, WORLD_HEIGHT / 2 - 40);


        solarSystem.addBody(body);
//        solarSystem.addBody(body1);
        solarSystem.addBody(body2);
//        solarSystem.addBody(body3);
    }

    private void init () {
        GraphicsUtils graphicsUtils = new GraphicsUtils();

        solarSystem = new SolarSystem();
        uiController = new UIController();

        API.getInstance().register(graphicsUtils);
        API.getInstance().register(solarSystem);
        API.getInstance().register(uiController);

        InputController inputController = new InputController();
        API.getInstance().register(inputController);

        this.renderer = createRenderer();
        uiController.init();

        inputController.addProcessor(uiController.getStage());

        API.get(GraphicsUtils.class).setUiViewport(uiController.getStage().getViewport());

        inputController.activate();
    }

    private ARenderer createRenderer () {
        final OrthographicCamera camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT/ 2f, 0);
        camera.update();

//        Viewport viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
//        API.get(GraphicsUtils.class).setGameViewport(viewport);

        ScreenViewport viewport = new ScreenViewport(camera);
        viewport.setWorldSize(WORLD_WIDTH, WORLD_HEIGHT);
        API.get(GraphicsUtils.class).setGameViewport(viewport);

        return new AdditiveBlendingFbRenderer(viewport);
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
        renderer.onResize(width, height);

        API.get(UIController.class).onResize(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        uiController.dispose();
    }
}
