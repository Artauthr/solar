package art.sol.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShaderLoader {
    static {
        ShaderProgram.pedantic = false;
    }

    public static ShaderProgram load (String name) {
        String fragPath = "shaders/" + name + ".frag";
        String vertexPath = "shaders/" + name + ".vert";

        ShaderProgram shaderProgram = new ShaderProgram(Gdx.files.internal(vertexPath), Gdx.files.internal(fragPath));

        if (!shaderProgram.isCompiled()) {
            log.error("Failed to compile shader: {}", name);
            log.info(shaderProgram.getLog());
            System.exit(0);
        }

        log.info("Compiled shader: {}", name);

        return shaderProgram;
    }
}
