package art.sol.display.render;

import art.sol.API;
import art.sol.Body;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphicsUtils {
    private Viewport gameViewport;
    private Viewport uiViewport;
    private ARenderer renderer;

    private static final Circle tempCircle = new Circle();
    private static final Vector3 v3 = new Vector3();
    private static final Vector2 v2 = new Vector2();

    public static boolean isHoveringOverBody (float gameSpaceX, float gameSpaceY, Body body) {
        Circle bodyCollisionCircle = getBodyCollisionCircle(body);
        return bodyCollisionCircle.contains(gameSpaceX, gameSpaceY);
    }

    public static Circle getBodyCollisionCircle (Body body) {
        tempCircle.set(body.getPosition().x, body.getPosition().y, body.getRadius());
        return tempCircle;
    }

    public static Vector3 worldToUi (float x, float y, float z) {
        v3.set(x, y, z);

        GraphicsUtils graphicsUtils = API.get(GraphicsUtils.class);

        graphicsUtils.gameViewport.project(v3);
        v3.y = Gdx.graphics.getHeight() - v3.y;
        graphicsUtils.uiViewport.unproject(v3);

        v3.z = 0f;
        return v3;
    }

    public static Vector3 screenToWorld (float screenX, float screenY) {
        v3.set(screenX, screenY, 0f);

        GraphicsUtils graphicsUtils = API.get(GraphicsUtils.class);
        graphicsUtils.gameViewport.unproject(v3);
        return v3;
    }

    public static Vector3 worldToScreen (float worldX, float worldY) {
        v3.set(worldX, worldY, 0f);

        GraphicsUtils graphicsUtils = API.get(GraphicsUtils.class);
        graphicsUtils.gameViewport.project(v3);
        return v3;
    }
}
