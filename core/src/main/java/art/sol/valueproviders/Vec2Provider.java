package art.sol.valueproviders;

import com.badlogic.gdx.math.Vector2;

public class Vec2Provider extends Float2Provider {
    private final Vector2 target;

    public Vec2Provider (Vector2 target) {
        this.target = target;
    }

    @Override
    public float readX () {
        return target.x;
    }

    @Override
    public float readY () {
        return target.y;
    }

    @Override
    public void writeX (float x) {
        target.x =  x;
    }

    @Override
    public void writeY (float y) {
        target.y = y;
    }
}
