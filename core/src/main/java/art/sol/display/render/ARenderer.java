package art.sol.display.render;

import art.sol.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class ARenderer implements Disposable {
    protected final Viewport viewport;

    abstract public void drawBodies (Array<Body> bodies);

    public ARenderer (Viewport viewport) {
        this.viewport = viewport;
    }
}
