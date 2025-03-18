package art.sol.valueproviders;

public interface ValueProvider<T extends Number> {
    T get();
}
