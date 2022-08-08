import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.net.URL;

public class UnitSlider extends JPanel {

    private ImageIcon icon;
    private JLabel imageLabel;
    private JSlider slider;
    private JLabel numDisplay;

    public UnitSlider(String imagePath) {
        super();

        URL imgURL = UnitSlider.class.getResource(imagePath);
        assert imgURL != null;
        icon = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        imageLabel = new JLabel(icon);
        add(imageLabel);

        slider = new JSlider(0, 250);
        slider.setPreferredSize(new Dimension(350, 35));
        slider.setValue(0);
        add(slider);

        numDisplay = new JLabel(String.format("%03d", slider.getValue()));
        add(numDisplay);

        slider.addChangeListener(e -> numDisplay.setText(String.format("%03d", slider.getValue())));
    }

    public void addChangeListener(ChangeListener l) {
        slider.addChangeListener(l);
    }

    public int getValue() {
        return slider.getValue();
    }
}
