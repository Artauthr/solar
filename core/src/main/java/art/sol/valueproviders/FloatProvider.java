package art.sol.valueproviders;

public abstract class FloatProvider {
    private final float[] data = new float[1];

    public float readValue () {
        return 0f;
    }

    public float get () {
        return data[0];
    }

    private void validate () {
    }

    // change the actual variable affected by this
    public void writeValue () {
    }

    public float[] primitiveArray () {
        return data;
    }
}
