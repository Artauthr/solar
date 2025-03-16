package art.sol.display;

public enum Shaders {
    ATMOSPHERE("atmosphere"),
    GEOMETRY("geometry"),
    MULTI_LIGHT_CAST("multi_light_cast"),
    TRACE("trace");

    private final String name;

    Shaders(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
