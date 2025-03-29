package art.sol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Utils {
    public static byte[] loadBytes (String internalPath) {
        FileHandle fontHandle = Gdx.files.internal(internalPath);
        return fontHandle.readBytes();
    }
}
