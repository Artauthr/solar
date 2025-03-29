package art.sol.valueproviders;

import art.sol.valueproviders.io.IntReader;
import art.sol.valueproviders.io.IntWriter;

public class IntProvider {
    private final int[] data = new int[1];

    private final IntReader reader;
    private final IntWriter writer;

    public IntProvider (IntReader rx, IntWriter wx) {
        this.reader = rx;
        this.writer = wx;
    }

    public int[] asPrimitiveArray () {
        data[0] = reader.read();
        return data;
    }

    public final void writeData () {
        writer.write(data[0]);
    }
}