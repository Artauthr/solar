package art.sol.ui.widgets;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.widget.VisWindow;

public abstract class AFloatingPanel extends VisWindow {
    public AFloatingPanel(String title) {
        super(title);
        setKeepWithinStage(true);
    }

    public AFloatingPanel () {
        this("");
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();

        Stage stage = getStage();
        if (stage == null) {
            return;
        }

        // prevent window from getting out of stage
        float x = getX();
        float stageWidth = getStage().getWidth();
        float panelWidth = getWidth();
        setX(MathUtils.clamp(x, 0, stageWidth - panelWidth));

        float y = getY();
        float stageHeight = getStage().getHeight();
        float panelHeight = getHeight();
        setY(MathUtils.clamp(y, 0, stageHeight - panelHeight));
    }
}
