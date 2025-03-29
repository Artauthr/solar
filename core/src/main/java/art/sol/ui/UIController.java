package art.sol.ui;

import art.sol.API;
import art.sol.Body;
import art.sol.display.ShaderLoader;
import art.sol.input.BodyInputListener;
import art.sol.input.BodyInteractionController;
import art.sol.input.InputController;
import art.sol.ui.widgets.UIInteractionOverlay;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import lombok.Getter;

public class UIController implements Disposable, BodyInputListener {
    @Getter
    private final Stage stage;
    private final Table root;

    private final PolygonSpriteBatch batch;
    private final Viewport viewport;

//    private UIInteractionOverlay uiInteractionOverlay;


    private static final int PANEL_PAD = 35;

    public UIController() {
        root = new Table();
        root.setFillParent(true);

        final OrthographicCamera camera = new OrthographicCamera();
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight()/ 2f, 0);
        camera.update();
        viewport = new ScreenViewport(camera);

        ShaderProgram defaultUiShader = ShaderLoader.load("default");
        batch = new PolygonSpriteBatch(2000, defaultUiShader);

        stage = new Stage(viewport, batch);
        stage.addActor(root);

        VisUI.load();
        VisUI.setDefaultTitleAlign(Align.center);
    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        stage.draw();
    }

    public void onResize (int width, int height) {
        viewport.update(width, height, true);
    }

    public void init () {
//        addTimeStepPanel();
//        addPopupOverlay();

        BodyInteractionController bodyInteractionController = API.get(InputController.class).getBodyInteractionController();
//        bodyInteractionController.registerListener(uiInteractionOverlay.getBodyInputListener());
    }

    @Override
    public void onHoverEnter (Body body) {
        BodyInputListener.super.onHoverEnter(body);
    }

    @Override
    public void onHoverExit (Body body) {
        BodyInputListener.super.onHoverExit(body);
    }

    private void addPopupOverlay () {
//        uiInteractionOverlay = new UIInteractionOverlay();
//        root.addActor(uiInteractionOverlay);
    }

    public void onHover (Body body) {

    }
}
