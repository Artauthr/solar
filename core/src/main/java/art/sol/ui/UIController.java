package art.sol.ui;

import art.sol.API;
import art.sol.SolarSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;

public class UIController implements Disposable {
    private final Stage stage;
    private final Table root;

    private final PolygonSpriteBatch batch;
    private final ScreenViewport viewport;

    private static final int PANEL_PAD = 35;

    public UIController() {
        root = new Table();
        root.setFillParent(true);

        viewport = new ScreenViewport();
        batch = new PolygonSpriteBatch();

        stage = new Stage(viewport, batch);
        stage.addActor(root);

        VisUI.load();
        VisUI.setDefaultTitleAlign(Align.center);
        Gdx.input.setInputProcessor(stage);
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
}
