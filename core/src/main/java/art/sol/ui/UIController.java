package art.sol.ui;

import art.sol.API;
import art.sol.Body;
import art.sol.SolarSystem;
import art.sol.input.BodyInteractionController;
import art.sol.input.InputController;
import art.sol.ui.widgets.SliderFloatModifierPanel;
import art.sol.ui.widgets.UIInteractionOverlay;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import lombok.Getter;

public class UIController implements Disposable {
    @Getter
    private final Stage stage;
    private final Table root;

    private final PolygonSpriteBatch batch;
    private final Viewport viewport;

    @Getter
    private UIInteractionOverlay uiInteractionOverlay;


    private static final int PANEL_PAD = 35;

    public UIController() {
        root = new Table();
        root.setFillParent(true);

        final OrthographicCamera camera = new OrthographicCamera();
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight()/ 2f, 0);
        camera.update();
        viewport = new ScreenViewport(camera);

        batch = new PolygonSpriteBatch();

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
        addTimeStepPanel();
        addPopupOverlay();

        BodyInteractionController bodyInteractionController = API.get(InputController.class).getBodyInteractionController();
        bodyInteractionController.registerListener(uiInteractionOverlay.getBodyHoverListener());
    }

    private void addPopupOverlay () {
        uiInteractionOverlay = new UIInteractionOverlay();
        root.addActor(uiInteractionOverlay);
    }

    private void addTimeStepPanel () {
        SliderFloatModifierPanel sliderFloatModifierPanel = new SliderFloatModifierPanel("Timestep", new FloatValueProvider() {
            @Override
            public float get() {
                return API.get(SolarSystem.class).getTimeStep();
            }

            @Override
            public void set(float value) {
                API.get(SolarSystem.class).setTimeStep(value);
            }
        });
        sliderFloatModifierPanel.setConstraints(0f, 0.2f, 0.0001f);
        root.addActor(sliderFloatModifierPanel);

        float x = stage.getWidth() - sliderFloatModifierPanel.getPrefWidth() - PANEL_PAD;
        float y = stage.getHeight() * 0.5f - PANEL_PAD;
        sliderFloatModifierPanel.setPosition(x, y);
    }

    public void onHover (Body body) {

    }
}
