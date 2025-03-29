package art.sol.valueproviders;

import art.sol.valueproviders.io.BooleanReader;
import art.sol.valueproviders.io.BooleanWriter;

public class BooleanProvider {
    private final BooleanReader reader;
    private final BooleanWriter writer;

    public BooleanProvider (BooleanReader reader, BooleanWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public boolean read () {
        return reader.read();
    }

    public void write (boolean value) {
        writer.write(value);
    }
}
