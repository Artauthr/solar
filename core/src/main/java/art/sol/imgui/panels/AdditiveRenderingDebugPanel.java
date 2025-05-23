package art.sol.imgui.panels;

import art.sol.API;
import art.sol.SolarSystem;
import art.sol.display.render.ARenderer;
import art.sol.display.render.AdditiveBlendingFbRenderer;
import art.sol.display.render.GraphicsUtils;
import imgui.ImGui;

public class AdditiveRenderingDebugPanel extends ADebugPanel {

    @Override
    public void renderContent () {
        ARenderer renderer = API.get(GraphicsUtils.class).getRenderer();
        if (!(renderer instanceof AdditiveBlendingFbRenderer)) {
            return;
        }

        AdditiveBlendingFbRenderer renddd = (AdditiveBlendingFbRenderer) renderer;

        SolarSystem solarSystem = API.get(SolarSystem.class);

        if (ImGui.checkbox("light", renddd.isDrawLightPass())) {
            renddd.setDrawLightPass(!renddd.isDrawLightPass());
        }

        if (ImGui.checkbox("trace", renddd.isDrawTracePass())) {
            renddd.setDrawTracePass(!renddd.isDrawTracePass());
        }
        if (ImGui.checkbox("planets", renddd.isDrawPlanetsPass())) {
            renddd.setDrawPlanetsPass(!renddd.isDrawPlanetsPass());
        }
    }
}
