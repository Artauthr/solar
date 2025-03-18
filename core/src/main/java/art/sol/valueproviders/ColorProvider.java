package art.sol.valueproviders;

import com.badlogic.gdx.graphics.Color;

public class ColorProvider {
    private final float[] data = new float[4];
    private final Color target;

    public ColorProvider (Color target) {
        this.target = target;
    }

    public float[] asPrimitiveArray () {
        data[0] = target.r;
        data[1] = target.g;
        data[2] = target.b;
        data[3] = target.a;
        return data;
    }

    public final void writeUpdatedValues () {
        target.r = data[0];
        target.g = data[1];
        target.b = data[2];
        target.a = data[3];
    }
}
