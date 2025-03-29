package art.sol.valueproviders;

import com.badlogic.gdx.math.Vector2;

public class Vec2Provider extends Float2Provider {

    public Vec2Provider (Vector2 target) {
        super(
                () -> target.x,
                () -> target.y,
                value -> target.x = value,
                value -> target.y = value);
    }
}
