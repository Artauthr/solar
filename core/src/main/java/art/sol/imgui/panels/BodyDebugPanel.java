package art.sol.imgui.panels;

import art.sol.API;
import art.sol.Body;
import art.sol.imgui.widgets.*;
import art.sol.input.BodyInputListener;
import art.sol.input.BodyInteractionController;
import art.sol.input.InputController;

public class BodyDebugPanel extends ADebugPanel implements BodyInputListener {
    private Body selection;

    private DebugFloat2Slider positionSlider;
    private DebugFloat2Slider velocitySlider;

    private DebugFloatSlider massSlider;
    private DebugFloatSlider radiusSlider;

    private DebugColorPicker bodyColorPicker;

    private DebugCheckboxWidget activeCheckBox;

    public BodyDebugPanel () {
        BodyInteractionController bodyInteractionController = API.get(InputController.class).getBodyInteractionController();
        bodyInteractionController.registerListener(this);

        positionSlider = new DebugFloat2Slider("Position");
        velocitySlider = new DebugFloat2Slider("Velocity");

//        massSlider = new DebugFloatSlider("Mass", 0, 100);
//        radiusSlider = new DebugFloatSlider("Radius", 0, 50);

        bodyColorPicker = new DebugColorPicker("Color");
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
            bodyColorPicker.render();
//            ImGui.sliderFloat2("Position",)
        }
    }


    @Override
    protected String getTitle () {
        return "Body";
    }
}
