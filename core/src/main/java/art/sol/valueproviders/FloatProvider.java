package art.sol.valueproviders;

import art.sol.valueproviders.supplier.FloatConsumer;
import art.sol.valueproviders.supplier.FloatSupplier;

public class FloatProvider {
    private final FloatSupplier getter;
    private final FloatConsumer setter;

    private final float[] data = new float[1];

    public FloatProvider(FloatSupplier getter, FloatConsumer setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public float get () {
        return getter.get();
    }

    public void set (float value) {
        setter.consume(value);
    }

    public float[] asPrimitiveArray() {
        return data;
    }

    public void writeData () {

    }
}

