package art.sol;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class API {
    private final ObjectMap<Class<?>, Object> classMap = new ObjectMap<>();
    private static API instance;

    public static API getInstance () {
        if (instance == null) {
            instance = new API();
        }

        return instance;
    }

    public <T> void register (T object) {
        classMap.put(object.getClass(), object);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get (Class<T> cls) {
        return (T) instance.classMap.get(cls);
    }

    public void dispose () {
        for (ObjectMap.Entry<Class<?>, Object> classObjectEntry : classMap) {
            Object object = classObjectEntry.value;
            if (object instanceof Disposable) {
                ((Disposable) object).dispose();
            }
        }
    }
}
