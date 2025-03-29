package art.sol.valueproviders;

import com.badlogic.gdx.graphics.Color;

public class ColorProvider extends Float4Provider {
    public ColorProvider (Color target) {
        super(
                () -> target.r,
                () -> target.g,
                () -> target.b,
                () -> target.a,
                value -> target.r = value,
                value -> target.g = value,
                value -> target.b = value,
                value -> target.a = value);
    }
}
