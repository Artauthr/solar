package art.sol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Utils {
    public static byte[] loadBytes (String assetRelativePath) {
        FileHandle fontHandle = Gdx.files.internal(assetRelativePath);
        return fontHandle.readBytes();
    }
}
