package art.sol.ui.widgets;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class NameValueWidget extends VisTable {
    private final VisLabel nameLabel;
    private final VisLabel valueLabel;

    public NameValueWidget () {
        nameLabel = new VisLabel();
        valueLabel = new VisLabel();

        nameLabel.setAlignment(Align.left);
        valueLabel.setAlignment(Align.right);

        add(nameLabel).grow();
        add(valueLabel).grow();
    }

    public void set (String name, float value) {
        nameLabel.setText(name);
        valueLabel.setText(String.valueOf(value));
    }
}
