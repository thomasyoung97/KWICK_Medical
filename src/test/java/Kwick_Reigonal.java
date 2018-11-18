import javax.swing.*;

public class Kwick_Reigonal {

    protected String[] ReqBackLog;
    private JTextField Incident_location_txt;
    private JLabel Incident_location_lbl;
    private JList Available_Vehicles;
    private JPanel pannel_kwick_R;
    private JList Requests_list;
    private  DefaultListModel Request_list_content;

    protected Communicating_Application parent;


    public Kwick_Reigonal(Communicating_Application parent)
    {
        this.parent = parent;
        this.parent.setApplicationExtentsion(this);

        JFrame frame = new JFrame("Kwick_Hq");
        frame.setContentPane(this.pannel_kwick_R);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //creating list content (Intelij library plugin sparates content from the actual object for som reason)
        Request_list_content = new DefaultListModel();
        Requests_list = new JList(Request_list_content);
        Request_list_content.add(0,"WEIRD");

    }

    public void receiveRequest(String request)
    {
        Request_list_content.addElement(request); // list content not updating.
        System.out.println(request);

    }

    public int findnextempty(String[] array)
    {
        int firstempty = 0;
        for(int i = 0; i < array.length; i++)
        {
            if(array[i] != null)
            {
                firstempty = i;
                break;

            }
        }
        return firstempty;
    }
}
