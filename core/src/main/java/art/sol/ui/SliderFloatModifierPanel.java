package art.sol.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;

public class SliderFloatModifierPanel extends VisWindow {
    private final VisSlider slider;
    private final VisLabel valueLabel;

    public SliderFloatModifierPanel (String name, FloatValueProvider floatValueProvider) {
        super(name, true);
        valueLabel = new VisLabel();

        slider = new VisSlider(0, 10, 1, false);
        setValue(floatValueProvider.get());

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = slider.getValue();
                floatValueProvider.set(value);
                setValue(value);
            }
        });

        add(slider);
        row();
        add(valueLabel);

        setResizable(false);
        setKeepWithinStage(true);
        pack();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();

        Stage stage = getStage();
        if (stage == null) {
            return;
        }

        // prevent window from getting out of stage
        float x = getX();
        float stageWidth = getStage().getWidth();
        float panelWidth = getWidth();
        setX(MathUtils.clamp(x, 0, stageWidth - panelWidth));

        float y = getY();
        float stageHeight = getStage().getHeight();
        float panelHeight = getHeight();
        setY(MathUtils.clamp(y, 0, stageHeight - panelHeight));
    }

    public void setConstraints (float min, float max, float stepSize) {
        slider.setRange(min, max);
        slider.setStepSize(stepSize);
    }

    private void setValue (float value) {
        slider.setValue(value);
        valueLabel.setText(String.valueOf(value));
    }
}
