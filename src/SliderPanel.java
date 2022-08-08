import javax.swing.*;

public class SliderPanel extends JPanel {
    public UnitSlider[] sliders;

    public SliderPanel(String path, String ext, String[] imageNames) {
        super();

        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);

        sliders = new UnitSlider[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            UnitSlider newSlider = new UnitSlider("./" + path + "/" + imageNames[i] + "." + ext);
            add(newSlider);
            sliders[i] = newSlider;
        }
    }

    public int[] getValues() {
        int[] result = new int[sliders.length];
        for (int i = 0; i < sliders.length; i++) {
            result[i] = sliders[i].getValue();
        }

        return result;
    }
}
