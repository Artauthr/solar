package art.sol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class Utils {
    public static byte[] loadBytes (String internalPath) {
        FileHandle fontHandle = Gdx.files.internal(internalPath);
        return fontHandle.readBytes();
    }
}
