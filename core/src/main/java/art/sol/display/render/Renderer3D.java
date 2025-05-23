package art.sol.display.render;

import art.sol.Body;
import art.sol.display.ShaderManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Renderer3D extends ARenderer{
    private ModelBatch modelBatch;
    private ModelBuilder modelBuilder;
    private PerspectiveCamera perspectiveCamera;
    Material material = new Material();
    private Environment environment;

    private Model sphereModel;
    private ModelInstance sphereModelInstance;

    public Renderer3D (Viewport viewport) {
        super(viewport);

        modelBatch = new ModelBatch(Gdx.files.internal("assets/shaders/default.vert"), Gdx.files.internal("assets/shaders/default.frag"));
//        modelBatch = new ModelBatch(new Base)
        perspectiveCamera = new PerspectiveCamera();
        modelBuilder = new ModelBuilder();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
        environment.add(new DirectionalLight().set(Color.WHITE, -1f, -0.8f, -0.2f));
    }

    private Model createSphereModel (Body body) {
        return modelBuilder.createSphere(body.getRadius(), body.getRadius(), 3f, 30, 30, material, VertexAttributes.Usage.Position);
    }

    boolean ff = false;

    @Override
    public void drawBodies (Array<Body> bodies) {
        if (!ff) {
            Body body = bodies.get(0);
            sphereModel = createSphereModel(body);
            sphereModelInstance = new ModelInstance(sphereModel);
            sphereModelInstance.transform.setToTranslation(0, 0, -5); // move back

            ff = true;
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(perspectiveCamera);
        modelBatch.render(sphereModelInstance, environment);
        modelBatch.end();

    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        sphereModel.dispose();
    }
}
