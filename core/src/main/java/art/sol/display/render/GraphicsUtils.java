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

    private static final Circle tempCircle = new Circle();
    private static final Vector3 v3 = new Vector3();
    private static final Vector2 v2 = new Vector2();

    public static boolean isHoveringOverBody (float gameSpaceX, float gameSpaceY, Body body) {
        Circle bodyCollisionCircle = getBodyCollisionCircle(body);
        return bodyCollisionCircle.contains(gameSpaceX, gameSpaceY);
    }

    public static Circle getBodyCollisionCircle (Body body) {
        tempCircle.set(body.getPosition(), body.getRadius());
        return tempCircle;
    }

    public static Vector2 gameToUi (float x, float y, float z) {
        v3.set(x, y, z);

        GraphicsUtils graphicsUtils = API.get(GraphicsUtils.class);

        graphicsUtils.gameViewport.project(v3);
        v3.y = Gdx.graphics.getHeight() - v3.y;
        graphicsUtils.uiViewport.unproject(v3);

        return v2.set(v3.x, v3.y);
    }
}
