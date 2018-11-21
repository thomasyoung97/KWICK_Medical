import com.google.gson.Gson;

import javax.imageio.ImageIO;
import javax.naming.Name;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Kwick_Hq
{
    protected Communicating_Application parent;
    private JButton Send_Message;
    private JPanel panel1;
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
    private JLabel Status_lbl;
    private JLabel Logo;
    private JLabel Name_lbl;
    private JButton Search_btn;
    private DefaultListModel model;
    private String[] RecordBuffer;
    private BufferedImage img;

    public Kwick_Hq(final Communicating_Application parent)
    {

        this.parent = parent;
        parent.setApplicationExtentsion(this);

        model = new DefaultListModel();
        list1.setModel(model);

        Send_Message.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                parent.routeAmbulanceRequest(parent.getNodeLeafset().get(2),Incident_txt.getText(),Description_txt.getText(),RecordBuffer);

                Send_Message.setEnabled(false);
                Status_lbl.setText("Awaiting Confirmation");
            }
        });

        Search_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if(Name_txt.getText() == "")
                {
                    JOptionPane.showMessageDialog(null,"Please Enter a Name");
                    return;
                }

                String patiens = parent.getPatientRecord(Name_txt.getText());

                String[] tokens = patiens.split("],\\[");

                for(int i = 0; i < tokens.length; i++)
                {

                    String a = "[]";
                    System.out.println(a);
                    System.out.println(patiens);
                    System.out.println((patiens == a));

                    if ( !tokens[0].contains(",")) // if no results found.
                    {
                        JOptionPane.showMessageDialog(null, "No Record Found under that name");
                        return;
                    }
                    else
                    {
                        model.addElement("MATCH: "+tokens[i].replace("[","").replace("]","")
                                .replace("\"", ""));
                    }
                }

            }
        });

        Name_txt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(Name_txt.getText() == "")
                {
                    JOptionPane.showMessageDialog(null,"Please Enter a Name");
                    return ;
                }

                String patiens = parent.getPatientRecord(Name_txt.getText());

                String[] tokens = patiens.split("],\\[");


                for(int i = 0; i < tokens.length; i++)
                {

                    if (!tokens[0].contains(","))// if not results found.
                    {
                        JOptionPane.showMessageDialog(null, "No Record Found under that name");
                       return;
                    }
                    else
                    {
                        model.addElement("MATCH: "+tokens[i].replace("[","").replace("]","")
                                .replace("\"", ""));
                    }
                }

            }
        });

        list1.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                String[] tokens = list1.getSelectedValue().toString().split(",");
                RecordBuffer = tokens;
                Nhs_Number.setText(tokens[5].replace("MATCH:",""));
                Age_txt.setText(tokens[1]);
                Postcode_txt.setText(tokens[2]);
                Adress_txt.setText(tokens[3]);
            }
        });

        Logo.setIcon(new ImageIcon("KwickMedical.png"));

        JFrame frame = new JFrame("Kwick_Hq");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public boolean checkDataEntered()
    {

        if(Name_txt.getText() == "")
        {
            JOptionPane.showMessageDialog(null,"Please Enter a Name");
            return false;
        }
        else if(Adress_txt.getText() == "")
        {
            JOptionPane.showMessageDialog(null,"Please Enter a Name");
            return false;
        }

      return true;
    }

    public void confirmaionRecived()
    {
        Status_lbl.setText("Confirmed");
        Send_Message.setEnabled(true);
    }

}
