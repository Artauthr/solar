package art.sol;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Body {
    private float mass;
    private float radius;
    private float lightEmission = 4f;

    private final Vector3 position = new Vector3();
    private final Vector3 velocity = new Vector3();

    private boolean active = true;

    private final Color color = new Color(Color.WHITE);

    public Body (float mass, float radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public void setColor (Color color) {
        this.color.set(color);
    }

    public void deltaVelocity (float x, float y, float z) {
        velocity.add(x, y, z);
    }
}
