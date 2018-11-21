import rice.p2p.commonapi.Message;

import javax.swing.*;

public class Kwick_Reigonal {

    protected String[] ReqBackLog;
    private JLabel Incident_location_lbl;
    private JPanel pannel_kwick_R;
    private JList Requests_list;
    private  DefaultListModel Request_list_content;

    protected Communicating_Application parent;


    public Kwick_Reigonal(Communicating_Application parent)
    {
        this.parent = parent;
        this.parent.setApplicationExtentsion(this);


        JFrame frame = new JFrame("Kwick_Regional");
        frame.setContentPane(this.pannel_kwick_R);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        DefaultListModel model = new DefaultListModel();
        Request_list_content = model;
        Requests_list.setModel(Request_list_content);


    }

    public void receiveRequest(String request, Ambulance_Request message)
    {
        Request_list_content.addElement(request);
        System.out.println(request);

        try
        {
            Request_list_content.addElement("...Processing request");
            Thread.sleep(1500);
            Request_list_content.addElement("...Dispatching ambulance");
            Thread.sleep(1500);
            Request_list_content.addElement("...Sending Patient Report");
            Thread.sleep(1000);
            dispatchAmbulance(message);
            Request_list_content.addElement("Ambulance Dispatched");
        }
        catch(InterruptedException e)
        {

        }

        parent.confirmAmbulanceRequest(message.getSender());
    }


    public void dispatchAmbulance(Ambulance_Request req)
    {
        String[] tokens = Request_list_content.getElementAt(0).toString().split("/"); // accident description.
        parent.routePatentRecord(parent.getNodeLeafset().get(3),req.getPatientRecord(),tokens[1]);
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
