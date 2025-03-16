package art.sol.observer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class EventModule {
    private final ObjectMap<Class<?>, Array<Method>> eventMap = new ObjectMap<>();

    public void register (Class<?> classToRegister) {
        Method[] declaredMethods = classToRegister.getDeclaredMethods();

        Array<Method> collectedMethods = new Array<>();

        for (Method declaredMethod : declaredMethods) {
            EventHandler handlerAnnotation = declaredMethod.getDeclaredAnnotation(EventHandler.class);

            if (handlerAnnotation == null) {
                continue;
            }

            collectedMethods.add(declaredMethod);
        }

        if (collectedMethods.isEmpty()) {
            log.error("Trying to register event handler class with no handler methods");
            return;
        }

        eventMap.put(classToRegister, collectedMethods);
    }

//    public Event obtainFreeEvent (Class<?> eventClass) {
//
//    }

    public void fireEvent (Event event) {

    }
}
