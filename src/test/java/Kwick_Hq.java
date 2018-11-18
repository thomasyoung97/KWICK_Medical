import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Kwick_Hq
{
    protected Communicating_Application parent;
    private JButton Send_Message;
    private JPanel panel1;
    private JLabel Name_lbl;
    private JTextField Name_txt;
    private JTextField Age_txt;
    private JLabel Age_lbl;
    private JTextField Adress_txt;
    private JLabel Adress_lbl;
    private JLabel Incident_lbl;
    private JTextField Incident_txt;

    public Kwick_Hq(final Communicating_Application parent)
    {

        this.parent = parent;
        Send_Message.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                parent.routeAmbulanceRequest(parent.getNodeLeafset().get(2));

            }
        });

        JFrame frame = new JFrame("Kwick_Hq");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        System.out.print("Kwick_Hq_Built\n");
    }


}
