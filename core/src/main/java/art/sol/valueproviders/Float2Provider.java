package art.sol.valueproviders;

import art.sol.valueproviders.io.FloatReader;
import art.sol.valueproviders.io.FloatWriter;
import lombok.Getter;

public class Float2Provider {
    private final float[] data = new float[2];

    @Getter
    private final FloatReader readerX;
    @Getter
    private final FloatReader readerY;

    private final FloatWriter writerX;
    private final FloatWriter writerY;

    public Float2Provider (FloatReader rx, FloatReader ry, FloatWriter wx, FloatWriter wy) {
        this.readerX = rx;
        this.readerY = ry;
        this.writerX = wx;
        this.writerY = wy;
    }

    public float[] asPrimitiveArray () {
        data[0] = readerX.read();
        data[1] = readerY.read();
        return data;
    }

    public final void writeUpdatedValues () {
        writerX.write(data[0]);
        writerY.write(data[1]);
    }
}
