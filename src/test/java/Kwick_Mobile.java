import rice.p2p.commonapi.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Kwick_Mobile {

    protected Communicating_Application parent;
    private JLabel Name_lbl;
    private JLabel Age_lbl;
    private JLabel Acident_Info_lbl;
    private JLabel Name_txt;
    private JLabel Age_txt;
    private JLabel Acident_Information_lbl;
    private JLabel Pre_Existing_lbl;
    private JLabel Pre_Existing_txt;
    private JPanel pannel1;
    private JLabel Logo;
    private JLabel Append_Record_lbl;
    private JTextPane Found_Conditions_txt;
    private JTextPane Callout_Details_txt;
    private JButton Update_Btn;
    private String[] RecordBuffer;


    public Kwick_Mobile(Communicating_Application app)
    {
        this.parent = app;
        parent.setApplicationExtentsion(this);

        Logo.setIcon(new ImageIcon("KwickMobile.png"));

        Update_Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                parent.appendPatentRecord(RecordBuffer,Found_Conditions_txt.getText(),Callout_Details_txt.getText());
            }
        });

        JFrame frame = new JFrame("Kwick_Mobile");
        frame.setContentPane(this.pannel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public void reciveRecord(P_Record_Message record)
    {
       RecordBuffer = record.getRecord();

        Name_txt.setText(RecordBuffer[0]);
        Age_txt.setText(RecordBuffer[1]);
        Acident_Information_lbl.setText(record.getAcidentDescription().replace("Accident Description:",""));
        Pre_Existing_txt.setText(RecordBuffer[4]);
    }


}
