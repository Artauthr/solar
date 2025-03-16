package art.sol.input;

import art.sol.API;
import art.sol.Body;
import art.sol.display.render.GraphicsUtils;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.NonNull;


public class BodyInteractionController extends InputAdapter {
    private final Array<Body> targetBodies;
    private final Array<BodyHoverListener> listeners = new Array<>();

    private static final Vector3 tempVector3 = new Vector3();

    public BodyInteractionController(@NonNull Array<Body> targetBodies) {
        this.targetBodies = targetBodies;
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY) {
        tempVector3.set(screenX, screenY, 0);

        Viewport gameViewport = API.get(GraphicsUtils.class).getGameViewport();
        gameViewport.unproject(tempVector3);

        for (Body body : targetBodies) {
            if (GraphicsUtils.isHoveringOverBody(tempVector3.x, tempVector3.y, body)) {
                for (BodyHoverListener listener : listeners) {
                    listener.onHoverEnter(body);
                }
                return true;
            } else {
                for (BodyHoverListener listener : listeners) {
                    listener.onHoverExit(body);
                }
            }
        }

        return super.mouseMoved(screenX, screenY);
    }

    public void registerListener (BodyHoverListener listener) {
        if (!listeners.contains(listener, true)) {
            listeners.add(listener);
        }
    }

}
