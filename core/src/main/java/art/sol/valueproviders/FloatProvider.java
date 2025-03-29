package art.sol.valueproviders;

import art.sol.valueproviders.io.FloatReader;
import art.sol.valueproviders.io.FloatWriter;

public class FloatProvider {
    private final float[] data = new float[1];

    private final FloatReader reader;
    private final FloatWriter writer;

    public FloatProvider (FloatReader rx, FloatWriter wx) {
        this.reader = rx;
        this.writer = wx;
    }

    public float[] asPrimitiveArray () {
        data[0] = reader.read();
        return data;
    }

    public final void writeData () {
        writer.write(data[0]);
    }
}
