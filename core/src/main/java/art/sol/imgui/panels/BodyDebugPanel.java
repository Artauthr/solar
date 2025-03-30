package art.sol.imgui.panels;

import art.sol.API;
import art.sol.Body;
import art.sol.imgui.widgets.*;
import art.sol.input.BodyInputListener;
import art.sol.input.BodyInteractionController;
import art.sol.input.InputController;
import art.sol.valueproviders.Float2Provider;
import art.sol.valueproviders.FloatProvider;
import art.sol.valueproviders.io.FloatReader;
import art.sol.valueproviders.io.FloatWriter;
import imgui.flag.ImGuiColorEditFlags;

public class BodyDebugPanel extends ADebugPanel implements BodyInputListener {
    private Body selection;

    private final DebugFloat2Slider positionSlider;
    private final DebugFloat2Slider velocitySlider;

    private DebugFloatSlider massSlider;
    private final DebugFloatSlider radiusSlider;
    private final DebugFloatSlider lightEmissionSlider;

    private final DebugColorPicker bodyColorPicker;

    private DebugCheckboxWidget activeCheckBox;

    public BodyDebugPanel () {
        BodyInteractionController bodyInteractionController = API.get(InputController.class).getBodyInteractionController();
        bodyInteractionController.registerListener(this);



        positionSlider = new DebugFloat2Slider("Position");
        positionSlider.setConstraints(-300, 300);
        velocitySlider = new DebugFloat2Slider("Velocity");
        velocitySlider.setConstraints(-10, 10);

//        massSlider = new DebugFloatSlider("Mass", 0, 100);

        radiusSlider = new DebugFloatSlider("Radius", new FloatProvider(() -> {
            if (selection == null) {
                return 0f;
            }
            return selection.getRadius();
        }, value -> {
            if (selection != null) {
                selection.setRadius(value);
            }
        }));
        radiusSlider.setConstraints(1f, 20f);

        lightEmissionSlider = new DebugFloatSlider("Light", () -> {
            if (selection == null) {
                return 0f;
            }
            return selection.getLightEmission();
        }, value -> {
            if (selection != null) {
                selection.setLightEmission(value);
            }
        });
        lightEmissionSlider.setConstraints(0f, 100f);

        bodyColorPicker = new DebugColorPicker("Color");
        bodyColorPicker.setFlags(ImGuiColorEditFlags.NoInputs);
//        bodyColorPicker.setTargetColor();

//        activeCheckBox = new DebugCheckboxWidget("Active");
//
//        activeCheckBox = new DebugCheckboxWidget("aa", new BooleanProvider() {
//            @Override
//            public boolean get () {
//                return false;
//            }
//
//            @Override
//            public void set (boolean value) {
//
//            }
//        })

//        activeCheckBox = new DebugCheckboxWidget("Active", new BooleanProvider() {
//            @Override
//            public boolean get () {
//                if (selection == null) {
//                    return false;
//                }
//                return selection.isActive();
//            }
//
//            @Override
//            public void set (boolean value) {
//                if (selection != null) {
//                    selection.setActive(value);
//                }
//            }
//        });
    }

    public void selectBody (Body body) {
        this.selection = body;
        positionSlider.setTargetVector(body.getPosition());
        velocitySlider.setTargetVector(body.getVelocity());
        bodyColorPicker.setTargetColor(body.getColor());
//        activeCheckBox.setTarget()
    }

    @Override
    public void onTouchUp (Body body) {
        selectBody(body);
    }

    @Override
    public void renderContent () {
        if (selection != null) {
            positionSlider.render();
            velocitySlider.render();
            lightEmissionSlider.render();
            radiusSlider.render();
            bodyColorPicker.render();
//            ImGui.sliderFloat2("Position",)
        }
    }


    @Override
    protected String getTitle () {
        return "Body";
    }
}
