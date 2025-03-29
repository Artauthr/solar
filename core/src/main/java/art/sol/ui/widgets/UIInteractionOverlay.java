package art.sol.ui.widgets;

import art.sol.Body;
import art.sol.display.render.GraphicsUtils;
import art.sol.input.BodyInputListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisTable;
import lombok.Getter;

public class UIInteractionOverlay extends VisTable {
    private final ObjectMap<Body, BodyInfoWidget> bodyWidgetMap = new ObjectMap<>();
    private final Image hoverImage;

    public UIInteractionOverlay() {
        setFillParent(true);

        hoverImage = new Image(new Texture("sprites/glow-circle.png"));
        hoverImage.setScaling(Scaling.fit);
    }

    @Getter
    private final BodyInputListener bodyInputListener = new BodyInputListener() {
        @Override
        public void onTouchUp (Body body) {

        }

        @Override
        public void onHoverEnter (Body body) {
            BodyInfoWidget bodyInfoWidget = obtainInfoWidget(body);
            if (!bodyInfoWidget.hasParent()) {
                popup(bodyInfoWidget);
            }

            if (!hoverImage.hasParent()) {

            }
        }

        @Override
        public void onHoverExit (Body body) {
            BodyInfoWidget bodyInfoWidget = obtainInfoWidget(body);
            bodyInfoWidget.remove();
        }
    };

    private void attachHoverImage (Body body) {
        hoverImage.setSize(body.getRadius(), body.getRadius());
        addActor(hoverImage);
    }

    private void popup (BodyInfoWidget infoWidget) {
        resetAnimationState(infoWidget);
        addActor(infoWidget);

        infoWidget.setTransform(true);

        ScaleToAction scaleAction = Actions.scaleTo(1f, 1f, 0.3f, Interpolation.smoother);
        RunnableAction toggleTransformAction = Actions.run(() -> infoWidget.setTransform(false));

        infoWidget.addAction(Actions.sequence(scaleAction, toggleTransformAction));
    }


    public void resetAnimationState (BodyInfoWidget bodyInfoWidget) {
        bodyInfoWidget.clearActions();
//        bodyInfoWidget.getColor().a = 0f;
        bodyInfoWidget.setScale(0f, 0f);
        bodyInfoWidget.setOrigin(Align.left);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (ObjectMap.Entry<Body, BodyInfoWidget> entry : bodyWidgetMap) {
            BodyInfoWidget widget = entry.value;
            if (!widget.hasParent()) {
                continue;
            }

            Body body = entry.key;

            float x = body.getPosition().x + body.getRadius();
            float y = body.getPosition().y + body.getRadius();

            Vector3 gameToUiCoords = GraphicsUtils.worldToUi(x, y, 0f);
            widget.setPosition(gameToUiCoords.x, gameToUiCoords.y);
        }

//        if (hoverImage.hasParent()) {
//            float x = body.getPosition().x + body.getRadius();
//            float y = body.getPosition().y + body.getRadius();
//            Vector2 gameToUiCoords = GraphicsUtils.gameToUi(x, y, 0f);
//            hoverImage.setPosition(gameToUiCoords.x, gameToUiCoords.y);
//        }
    }

    private BodyInfoWidget obtainInfoWidget (Body body) {
        if (!bodyWidgetMap.containsKey(body)) {
            BodyInfoWidget widget = new BodyInfoWidget();
            widget.pack();
            bodyWidgetMap.put(body, widget);
        }

        return bodyWidgetMap.get(body);
    }
}
