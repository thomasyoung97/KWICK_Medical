import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private JList list1;
    private JTextField Description_txt;
    private JLabel Description_lbl;
    private JTextField Nhs_Number;
    private JLabel Nhs_Number_lbl;
    private JTextField Postcode_txt;
    private JLabel Postcode_lbl;
    private DefaultListModel model;

    public Kwick_Hq(final Communicating_Application parent)
    {

        this.parent = parent;
        parent.setApplicationExtentsion(this);

        model = new DefaultListModel();
        list1.setModel(model);

        Send_Message.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                parent.routeAmbulanceRequest(parent.getNodeLeafset().get(2),Incident_txt.getText(),Description_txt.getText());

            }
        });
        Name_txt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String patiens = parent.getPatientRecord(Name_txt.getText());

                String[] tokens = patiens.split("],\\[");

                for(int i = 0; i < tokens.length; i++)
                {
                    model.addElement("MATCH: "+tokens[i].replace("[","").replace("]","")
                            .replace("\"", ""));
                }

            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                String[] tokens = list1.getSelectedValue().toString().split(",");
                Nhs_Number.setText(tokens[0].replace("MATCH:",""));
                Age_txt.setText(tokens[2]);
                Postcode_txt.setText(tokens[3]);
                Adress_txt.setText(tokens[4]);

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
