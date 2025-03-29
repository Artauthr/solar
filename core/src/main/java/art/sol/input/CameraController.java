package art.sol.input;

import art.sol.API;
import art.sol.display.render.GraphicsUtils;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraController extends InputAdapter {
    private final static float CAMERA_SPEED = 0.09f;

    public static final float MAX_ZOOM = 5f;
    public static final float MIN_ZOOM = 0.4f;

    @Override
    public boolean scrolled (float amountX, float amountY) {
        GraphicsUtils graphicsUtils = API.get(GraphicsUtils.class);
        Viewport gameViewport = graphicsUtils.getGameViewport();
        Camera camera = gameViewport.getCamera();

        OrthographicCamera orthographicCamera = (OrthographicCamera) camera;
        orthographicCamera.zoom = MathUtils.clamp(orthographicCamera.zoom + amountY * CAMERA_SPEED, MIN_ZOOM, MAX_ZOOM);
        orthographicCamera.update();

        return super.scrolled(amountX, amountY);
    }
}
