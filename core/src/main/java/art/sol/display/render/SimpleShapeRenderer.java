package art.sol.display.render;

import art.sol.Body;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SimpleShapeRenderer extends ARenderer {
    private final ShapeRenderer shapeRenderer;

    private static final int CIRCLE_SEGMENT_COUNT = 30;

    public SimpleShapeRenderer (Viewport viewport) {
        super(viewport);

        this.shapeRenderer = new ShapeRenderer(8000);
        this.shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void drawBodies (Array<Body> bodies) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        for (Body body : bodies) {
            Vector2 position = body.getPosition();

            shapeRenderer.setColor(body.getColor());
            shapeRenderer.circle(position.x, position.y, body.getRadius(), CIRCLE_SEGMENT_COUNT);
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
