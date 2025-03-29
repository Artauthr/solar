package art.sol.display;

import art.sol.API;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ObjectMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShaderManager {
    /*
    This class only loads shaders, you have to manage the disposal yourself
     */
    private final ObjectMap<String, ShaderProgram> loadedShaderMap = new ObjectMap<>();

    public ShaderManager () {
        ShaderProgram.pedantic = false;
    }

    public static ShaderProgram getOrCreateShader (String name) {
        ShaderManager shaderManager = API.get(ShaderManager.class);
        ObjectMap<String, ShaderProgram> shaderCache = shaderManager.loadedShaderMap;

        if (!shaderCache.containsKey(name)) {
            ShaderProgram shaderProgram = shaderManager.load(name);
            shaderCache.put(name, shaderProgram);
        }

        return shaderCache.get(name);
    }

    public ShaderProgram load (String name) {
        String fragPath = "shaders/" + name + ".frag";
        String vertexPath = "shaders/" + name + ".vert";

        ShaderProgram shaderProgram = new ShaderProgram(Gdx.files.internal(vertexPath), Gdx.files.internal(fragPath));

        if (!shaderProgram.isCompiled()) {
            log.error("Failed to compile shader: {}", name);
            log.info(shaderProgram.getLog());
            System.exit(0);
        }

        log.info("Compiled shader: {}", fragPath);
        log.info("Compiled shader: {}", vertexPath);

        return shaderProgram;
    }
}
