package art.sol.input;

import art.sol.API;
import art.sol.display.render.GraphicsUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraController extends InputAdapter {
    private final static float CAMERA_ZOOM_SPEED = 0.09f;
    private final static float CAMERA_MOVE_SPEED = 20f;

    public static final float MAX_ZOOM = 10f;
    public static final float MIN_ZOOM = 0.01f;

    @Override
    public boolean scrolled (float amountX, float amountY) {
        GraphicsUtils graphicsUtils = API.get(GraphicsUtils.class);
        Viewport gameViewport = graphicsUtils.getGameViewport();
        Camera camera = gameViewport.getCamera();

        OrthographicCamera orthographicCamera = (OrthographicCamera) camera;
        orthographicCamera.zoom = MathUtils.clamp(orthographicCamera.zoom + amountY * CAMERA_ZOOM_SPEED, MIN_ZOOM, MAX_ZOOM);
        orthographicCamera.update();

        return super.scrolled(amountX, amountY);
    }

    @Override
    public boolean keyDown (int keycode) {
        float deltaX = 0f;
        float deltaY = 0f;

        if (keycode == Input.Keys.A) {
            deltaX -= CAMERA_MOVE_SPEED;
        }
        if (keycode == Input.Keys.D) {
            deltaX += CAMERA_MOVE_SPEED;
        }
        if (keycode == Input.Keys.W) {
            deltaY += CAMERA_MOVE_SPEED;
        }
        if (keycode == Input.Keys.S) {
            deltaY -= CAMERA_MOVE_SPEED;
        }

        if (deltaX != 0 || deltaY != 0) {
            GraphicsUtils graphicsUtils = API.get(GraphicsUtils.class);
            Viewport gameViewport = graphicsUtils.getGameViewport();
            Camera camera = gameViewport.getCamera();

            OrthographicCamera orthographicCamera = (OrthographicCamera) camera;
            System.err.println("adding " + deltaX);
            orthographicCamera.position.add(deltaX, deltaY, 0);
            orthographicCamera.update();
        }

        return super.keyDown(keycode);

    }
}
