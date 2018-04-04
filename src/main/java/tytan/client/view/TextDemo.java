package tytan.client.view;

import tytan.client.controller.AbstractController;
import tytan.client.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class TextDemo extends JPanel {
    private final static String newline = "\n";
    protected JTextArea textArea;
    private Controller controller;

    public TextDemo(Controller controller) {
        super(new GridBagLayout());
        this.controller = controller;
        this.controller.setDemoView(this);

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;


        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }

    public static void createAndShowGUI(Controller controller) {
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new TextDemo(controller));

        frame.pack();
        frame.setVisible(true);
    }

    public static void createAndShowGUI(AbstractController controller2) {
        // TODO Auto-generated method stub

    }

    public void printMessage(String text) {
        textArea.append(text + newline);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}
