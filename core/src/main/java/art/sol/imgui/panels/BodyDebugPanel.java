package art.sol.imgui.panels;

import art.sol.Body;
import art.sol.imgui.widgets.*;
import art.sol.valueproviders.BooleanProvider;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;

public class BodyDebugPanel extends ADebugPanel {
    private Body selection;

    private DebugFloat2Slider positionSlider;
    private DebugFloat2Slider velocitySlider;

    private DebugFloatSlider massSlider;
    private DebugFloatSlider radiusSlider;

    private DebugColorPicker bodyColorPicker;

    private DebugCheckboxWidget activeCheckBox;

    public BodyDebugPanel () {
        positionSlider = new DebugFloat2Slider("Position",-100, 100);
        velocitySlider = new DebugFloat2Slider("Velocity", -100, 100);

        massSlider = new DebugFloatSlider("Mass", 0, 100);
        radiusSlider = new DebugFloatSlider("Radius", 0, 50);

        bodyColorPicker = new DebugColorPicker("Color");

        activeCheckBox = new DebugCheckboxWidget("Active",
                () -> selection != null && selection.isActive(),
                value -> { if (selection != null) selection.setActive(value); }
        );

        activeCheckBox = new DebugCheckboxWidget("aa", new BooleanProvider() {
            @Override
            public boolean get () {
                return false;
            }

            @Override
            public void set (boolean value) {

            }
        })

        activeCheckBox = new DebugCheckboxWidget("Active", new BooleanProvider() {
            @Override
            public boolean get () {
                if (selection == null) {
                    return false;
                }
                return selection.isActive();
            }

            @Override
            public void set (boolean value) {
                if (selection != null) {
                    selection.setActive(value);
                }
            }
        });
    }


    @Override
    public void renderContent () {
        if (selection != null) {
            positionSlider.render();
            velocitySlider.render();
//            ImGui.sliderFloat2("Position",)
        }
    }


    @Override
    protected String getTitle () {
        return "Body";
    }
}
