import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class View {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Optimizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        String[] imageNames = {"Club", "Copper_sword", "Iron_sword", "Armored_horse", "Horse", "Bow", "Longbow", "Crossbow", "Cannon"};
        SliderPanel orcSliders = new SliderPanel("icons", "png", imageNames);

        JLabel report = new JLabel("Adjust the sliders to get a team composition");

        for (UnitSlider s : orcSliders.sliders) {
            s.addChangeListener(e -> report.setText(Optimizer.processOrcTeam(orcSliders.getValues())));
        }

        frame.add(orcSliders, BorderLayout.PAGE_START);
        frame.add(report, BorderLayout.PAGE_END);

        frame.setVisible(true);
    }
}
