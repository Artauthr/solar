package art.sol;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

public class Body {
    @Getter
    @Setter
    private float mass;

    @Getter
    @Setter
    private float radius;

    @Getter
    @Setter
    private float lightEmission = 12f;

    @Getter
    private final Vector2 position = new Vector2();
    @Getter
    private final Vector2 velocity = new Vector2();

    @Getter
    @Setter
    private boolean active = true;

    @Getter
    private final Color color = new Color(Color.WHITE);

    public Body (float mass, float radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public void setColor (Color color) {
        this.color.set(color);
    }

    public void deltaVelocity (float x, float y) {
        velocity.add(x, y);
    }
}
