package art.sol.ui.widgets;

import com.kotcrab.vis.ui.widget.VisTable;

public class BodyInfoWidget extends VisTable {
    private NameValueWidget nameWidget;
    private NameValueWidget massWidget;
    private NameValueWidget positionWidget;

    public BodyInfoWidget () {
        nameWidget = new NameValueWidget();
        nameWidget.set("GayNigga", 420f);

        add(nameWidget).grow();
    }
}
