package art.sol.observer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class EventModule {
    private final ObjectMap<Class<?>, Array<EventRunner>> eventRunnerMap = new ObjectMap<>();
    private final ObjectMap<Class<? extends Event>, Pool<Event>> eventPools = new ObjectMap<>();

    public EventModule () {
    }

    public void register (EventListener eventListener) {
        final Method[] declaredMethods = eventListener.getClass().getDeclaredMethods();

        for (Method declaredMethod : declaredMethods) {
            EventHandler handlerAnnotation = declaredMethod.getDeclaredAnnotation(EventHandler.class);

            if (handlerAnnotation == null) {
                continue;
            }

            int parameterCount = declaredMethod.getParameterCount();
            if (parameterCount != 1) {
                throw new GdxRuntimeException("Invalid eventHandler declaration");
            }

            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            Class<?> eventType = parameterTypes[0];

            if (!Event.class.isAssignableFrom(eventType)) {
                throw new GdxRuntimeException("Invalid eventHandler param");
            }

            EventRunner eventRunner = event -> {
                declaredMethod.setAccessible(true);
                try {
                    declaredMethod.invoke(eventListener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            register(eventType, eventRunner);
        }
    }

    private void register (Class<?> eventClass, EventRunner eventRunner) {
        if (!eventRunnerMap.containsKey(eventClass)) {
            eventRunnerMap.put(eventClass, new Array<>());
        }

        Array<EventRunner> eventRunners = eventRunnerMap.get(eventClass);
        eventRunners.add(eventRunner);
    }

    public void fireEvent (Event event) {
        Array<EventRunner> eventRunners = eventRunnerMap.get(event.getClass());

        for (EventRunner eventRunner : eventRunners) {
            eventRunner.invoke(event);
        }
    }

    public void fireEvent (Class<? extends Event> eventClass) {
        Event eventInstance = obtainFreeEvent(eventClass);
        fireEvent(eventInstance);
        freeEvent(eventInstance);
    }

    private <T extends Event> T obtainFreeEvent (Class<T> eventClass) {
        return getPool(eventClass).obtain();
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> Pool<T> getPool (Class<T> eventClass) {
        if (!eventPools.containsKey(eventClass)) {
            eventPools.put(eventClass, new Pool<>() {
                @Override
                protected Event newObject () {
                    try {
                        return ClassReflection.newInstance(eventClass);
                    } catch (ReflectionException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        return (Pool<T>) eventPools.get(eventClass);
    }

    private <T extends Event> void freeEvent (T event) {
        Pool<Event> eventPool = eventPools.get(event.getClass());
        eventPool.free(event);
    }
}
