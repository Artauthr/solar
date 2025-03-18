package art.sol.valueproviders;

public abstract class Float2Provider {
    private final float[] data = new float[2];

    public abstract float readX ();
    public abstract float readY ();

    public abstract void writeX (float x);
    public abstract void writeY (float y);

    public float[] asPrimitiveArray () {
        data[0] = readX();
        data[1] = readY();
        return data;
    }

    public final void writeUpdatedValues () {
        writeX(data[0]);
        writeY(data[1]);
    }
}
