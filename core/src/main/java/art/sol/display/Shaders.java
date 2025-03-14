package art.sol.display;

import lombok.Getter;

@Getter
public enum Shaders {
    MULTI_LIGHT_CAST("multi_light_cast");

    private final String name;

    Shaders (String name) {
        this.name = name;
    }

}
