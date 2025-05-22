package art.sol.imgui.panels;

import art.sol.API;
import art.sol.Body;
import art.sol.imgui.widgets.*;
import art.sol.input.BodyInputListener;
import art.sol.input.BodyInteractionController;
import art.sol.input.InputController;
import imgui.flag.ImGuiColorEditFlags;

public class BodyDebugPanel extends ADebugPanel implements BodyInputListener {
    private Body selection;

    private final DebugVector3Slider positionSlider;
    private final DebugVector3Slider velocitySlider;

    private final DebugFloatSlider massSlider;
    private final DebugFloatSlider radiusSlider;
    private final DebugFloatSlider lightEmissionSlider;

    private final DebugColorPicker bodyColorPicker;
    private final DebugCheckboxWidget activeCheckBox;

    public BodyDebugPanel () {
        BodyInteractionController bodyInteractionController = API.get(InputController.class).getBodyInteractionController();
        bodyInteractionController.registerListener(this);

        positionSlider = new DebugVector3Slider("Position");
        positionSlider.setConstraints(-300, 300);

        velocitySlider = new DebugVector3Slider("Velocity");
        velocitySlider.setConstraints(-300, 300);

        radiusSlider = new DebugFloatSlider("Radius");
        radiusSlider.setConstraints(1f, 20f);

        massSlider = new DebugFloatSlider("Mass");
        massSlider.setConstraints(1f, 20f);

        lightEmissionSlider = new DebugFloatSlider("Light");
        lightEmissionSlider.setConstraints(0f, 100f);

        bodyColorPicker = new DebugColorPicker("Color");
        bodyColorPicker.setFlags(ImGuiColorEditFlags.NoInputs);

        activeCheckBox = new DebugCheckboxWidget("Active");
    }

    public void selectBody (Body body) {
        this.selection = body;
    }

    @Override
    public void onTouchUp (Body body) {
        selectBody(body);
    }

    @Override
    public void renderContent () {
        if (selection == null) {
            return;
        }

        activeCheckBox.render(() -> selection.isActive(), value -> selection.setActive(value));

        positionSlider.render(() -> selection.getPosition());
        velocitySlider.render(() -> selection.getVelocity());

        lightEmissionSlider.render(() -> selection.getLightEmission(), value -> selection.setLightEmission(value));
        radiusSlider.render(() -> selection.getRadius(), value -> selection.setRadius(value));
        massSlider.render(() -> selection.getMass(), value -> selection.setMass(value));

        bodyColorPicker.render(() -> selection.getColor());
    }


    @Override
    protected String getTitle () {
        return "Body";
    }
}
