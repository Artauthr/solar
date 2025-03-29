package art.sol.valueproviders;

import art.sol.valueproviders.io.FloatReader;
import art.sol.valueproviders.io.FloatWriter;

public class Float4Provider {
    private final float[] data = new float[4];

    private final FloatReader readerX;
    private final FloatReader readerY;
    private final FloatReader readerZ;
    private final FloatReader readerA;

    private final FloatWriter writerX;
    private final FloatWriter writerY;
    private final FloatWriter writerZ;
    private final FloatWriter writerA;

    public Float4Provider (
            FloatReader rx,
            FloatReader ry,
            FloatReader rz,
            FloatReader ra,
            FloatWriter wx,
            FloatWriter wy,
            FloatWriter wz,
            FloatWriter wa) {
        this.readerX = rx;
        this.readerY = ry;
        this.readerZ = rz;
        this.readerA = ra;
        this.writerX = wx;
        this.writerY = wy;
        this.writerZ = wz;
        this.writerA = wa;
    }

    public float[] asPrimitiveArray () {
        data[0] = readerX.read();
        data[1] = readerY.read();
        data[2] = readerZ.read();
        data[3] = readerA.read();
        return data;
    }

    public final void writeUpdatedValues () {
        writerX.write(data[0]);
        writerY.write(data[1]);
        writerZ.write(data[2]);
        writerA.write(data[3]);
    }
}