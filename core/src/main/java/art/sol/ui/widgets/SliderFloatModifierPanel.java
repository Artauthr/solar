package art.sol.ui.widgets;

import art.sol.ui.FloatValueProvider;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;

public class SliderFloatModifierPanel extends AFloatingPanel {
    private final VisSlider slider;
    private final VisLabel valueLabel;

    public SliderFloatModifierPanel (String name, FloatValueProvider floatValueProvider) {
        super(name);
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
        pack();
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
