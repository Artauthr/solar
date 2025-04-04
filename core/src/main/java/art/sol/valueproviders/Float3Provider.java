package art.sol.valueproviders;

import art.sol.valueproviders.io.FloatReader;
import art.sol.valueproviders.io.FloatWriter;

public class Float3Provider {
    private final float[] data = new float[3];

    private final FloatReader readerX;
    private final FloatReader readerY;
    private final FloatReader readerZ;

    private final FloatWriter writerX;
    private final FloatWriter writerY;
    private final FloatWriter writerZ;

    public Float3Provider (
            FloatReader rx,
            FloatReader ry,
            FloatReader rz,
            FloatWriter wx,
            FloatWriter wy,
            FloatWriter wz) {
        this.readerX = rx;
        this.readerY = ry;
        this.readerZ = rz;
        this.writerX = wx;
        this.writerY = wy;
        this.writerZ = wz;
    }

    public float[] asPrimitiveArray () {
        data[0] = readerX.read();
        data[1] = readerY.read();
        data[2] = readerZ.read();
        return data;
    }

    public final void writeUpdatedValues () {
        writerX.write(data[0]);
        writerY.write(data[1]);
        writerZ.write(data[2]);
    }
}
