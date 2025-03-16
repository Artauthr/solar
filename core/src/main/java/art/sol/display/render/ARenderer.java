package art.sol.display.render;

import art.sol.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ARenderer implements Disposable {
    @Getter
    protected final Viewport viewport;

    abstract public void drawBodies (Array<Body> bodies);

    public void onResize (int width, int height) {
        viewport.update(width, height, true);
    }

    public ARenderer (Viewport viewport) {
        this.viewport = viewport;
    }
}
