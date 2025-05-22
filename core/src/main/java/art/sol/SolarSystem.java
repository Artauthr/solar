package art.sol;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import lombok.Setter;

public class SolarSystem {
    public static final float GRAVITY_CONSTANT = 0.4f;

    @Getter
    @Setter
    private Array<Body> bodies = new Array<>();

    @Getter
    @Setter
    private float timeStep = 0.02f;

    private final Vector3 forceDirection = new Vector3();
    private final Vector3 force = new Vector3();
    private final Vector3 accel = new Vector3();

    @Getter
    @Setter
    private boolean active = false;

    public static SolarSystem get () { // helper
        return API.get(SolarSystem.class);
    }

    public void addBody (Body body) {
        bodies.add(body);
    }

    public void update () {
        if (active) {
            updateVelocities();
            updatePositions();
        }
    }

    private void updateVelocities () {
        for (int i = 0; i < bodies.size; i++) {
            Body b1 = bodies.get(i);

            if (!b1.isActive()) {
                continue;
            }

            for (int j = 0; j < bodies.size; j++) {
                Body b2 = bodies.get(j);

                if (!b2.isActive()) {
                    return;
                }

                if (b1 == b2) {
                    continue;
                }

                float m1 = b1.getMass();
                float m2 = b2.getMass();

                Vector3 p1 = b1.getPosition();
                Vector3 p2 = b2.getPosition();

                float sqrDst = p1.dst2(p2);
                forceDirection.set(p2).sub(p1).nor(); // normalized direction
                force.set(forceDirection).scl(GRAVITY_CONSTANT * m1 * m2 / sqrDst);
                accel.set(force).scl(1f / m1);

                b1.getVelocity().mulAdd(accel, timeStep);
            }
        }
    }


    private void updatePositions () {
        for (Body body : bodies) {
            final Vector3 position = body.getPosition();
            final Vector3 velocity = body.getVelocity();

            position.mulAdd(velocity, timeStep);
        }
    }
}
