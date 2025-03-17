package art.sol;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import lombok.Setter;

public class SolarSystem {
    public static final float GRAVITY_CONSTANT = 0.4f;

    @Getter
    private final Array<Body> bodies = new Array<>();

    @Getter
    @Setter
    private float timeStep = 0.02f;

    private final Vector2 forceDirection = new Vector2();
    private final Vector2 force = new Vector2();
    private final Vector2 accel = new Vector2();

    public static SolarSystem get () { // helper
        return API.get(SolarSystem.class);
    }

    public void addBody (Body body) {
        bodies.add(body);
    }

    public void updateVelocities () {
        for (int i = 0; i < bodies.size; i++) {
            Body b1 = bodies.get(i);

            for (int j = 0; j < bodies.size; j++) {
                Body b2 = bodies.get(j);

                if (b1 == b2) {
                    continue;
                }

                int m1 = b1.getMass();
                int m2 = b2.getMass();

                Vector2 p1 = b1.getPosition();
                Vector2 p2 = b2.getPosition();

                float sqrDst = Vector2.dst2(p1.x, p1.y, p2.x, p2.y);
                forceDirection.set(p2).sub(p1).nor();
                force.set(forceDirection).scl(GRAVITY_CONSTANT * m1 * m2 / sqrDst);
                accel.set(force).scl((float) 1 / m1);

                b1.deltaVelocity(accel.x * timeStep, accel.y * timeStep);
            }
        }
    }

    public void updatePositions () {
        for (Body body : bodies) {
            body.getPosition().add(body.getVelocity().x * timeStep, body.getVelocity().y * timeStep);
        }
    }
}
