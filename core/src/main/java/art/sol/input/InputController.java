package art.sol.input;

import art.sol.API;
import art.sol.SolarSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import lombok.Getter;

public class InputController {
    private final InputMultiplexer multiplexer;

    @Getter
    private final BodyInteractionController bodyInteractionController;

    private final CameraController cameraController;

    public InputController () {
        multiplexer = new InputMultiplexer();

        cameraController = new CameraController();

        SolarSystem solarSystem = API.get(SolarSystem.class);
        bodyInteractionController = new BodyInteractionController(solarSystem.getBodies());

        multiplexer.addProcessor(cameraController);
        multiplexer.addProcessor(bodyInteractionController);
    }

    public void activate () {
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void addProcessor (InputProcessor inputProcessor) {
        multiplexer.addProcessor(inputProcessor);
    }
}
