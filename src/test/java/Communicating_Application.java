import java.awt.geom.PathIterator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import rice.pastry.PastryNode;
import rice.p2p.commonapi.*;
import rice.p2p.commonapi.appsocket.*;
import rice.pastry.leafset.LeafSet;
import com.google.gson.Gson;


public class Communicating_Application implements Application {
    /**
     * The Endpoint represents the underlieing node.  By making calls on the
     * Endpoint, it assures that the message will be delivered to a Communicating_Application on whichever
     * node the message is intended for.
     */

    /**
     * This
     */

    /**
     * This acts effectively as an approved applications list. this would be done differently in a non simulated environment.
     */
    static protected Kwick_Reigonal child_Rf=null;
    static protected Kwick_Hq child_Hq=null;
    static  protected Kwick_Mobile child_M = null;


    String Application_Extention;

    protected Endpoint endpoint;

    /**
     * The node we were constructed on.
     */
    protected Node node;

    /**
     * The message to send.
     */
    ByteBuffer out;

    /**
     * The message to receive.
     */
    ByteBuffer in;


    int MSG_LENGTH;





    public Communicating_Application(Node node, final IdFactory factory) {
        // register the endpoint
        this.endpoint = node.buildEndpoint(this, "Comm-App");
        this.node = node;

        // create the output message
        MSG_LENGTH = node.getLocalNodeHandle().getId().toByteArray().length;
        out = ByteBuffer.wrap(node.getLocalNodeHandle().getId().toByteArray());

        // create a buffer for the input message
        in = ByteBuffer.allocate(MSG_LENGTH);

        // example receiver interface
        endpoint.accept(new AppSocketReceiver() {
            /**
             * When we accept a new socket.
             */
            public void receiveSocket(AppSocket socket) {
                // this code reuses "this" AppSocketReceiver, and registers for reading only, and a timeout of 30000.
                socket.register(true, false, 30000, this);

                // it's critical to call this to be able to accept multiple times
                endpoint.accept(this);
            }

            /**
             * Called when the socket is ready for reading or writing.
             */
            public void receiveSelectResult(AppSocket socket, boolean canRead, boolean canWrite) {
                in.clear();
                try {
                    // read from the socket into ins
                    long ret = socket.read(in);

                    if (ret != MSG_LENGTH) {
                        // if you sent any kind of long message, you would need to handle this case better
                        System.out.println("Error, we only received part of a message."+ret+" from "+socket);
                        return;
                    }

                    System.out.println(Communicating_Application.this.node.getLocalNodeHandle()+" Received message from "+factory.buildId(in.array()));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                // only need to do this if expecting more messages
//        socket.register(true, false, 3000, this);
            }

            /**
             * Called if we have a problem.
             */
            public void receiveException(AppSocket socket, Exception e) {
                e.printStackTrace();
            }
        });

        // register after we have set the AppSocketReceiver
        endpoint.register();
    }


    public Node getNode() {
        return node;
    }

    public void setApplicationExtentsion(Kwick_Reigonal KR)
    {
        child_Rf = KR;
    }
    public void setApplicationExtentsion(Kwick_Hq KHQ)
    {
        child_Hq = KHQ;
    }
    public void setApplicationExtentsion(Kwick_Mobile KM)
    {
        child_M = KM;
    }

    public void routeMyMsgDirect(NodeHandle nh) {
        Message msg = new TestMessage(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
        endpoint.route(null, msg, nh);
    }


    public void routeAmbulanceRequest(NodeHandle nh,String location,String description,String[] patientRecord)
    {
        Message msg = new Ambulance_Request(location,description,this.node.getLocalNodeHandle(),patientRecord);
        endpoint.route(null, msg, nh);
    }
    public void confirmAmbulanceRequest(NodeHandle nh)
    {
        Message msg = new Ambulance_Confirmation(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
        endpoint.route(null, msg, nh);
    }

    public void appendPatentRecord(String[] patientRecord,String Existing_ap, String Callout_ap)
    {
        DBAccsess db = new DBAccsess();
        db.dbConnect();
        db.appendRecord(patientRecord,Existing_ap,Callout_ap);


    }
    /**
     * communication protocol for any application sending a patient record
     * @param nh - node header to send the record to
     *
     */
    public void routePatentRecord(NodeHandle nh, String[] patient,String acidentDescription)
    {
        Message msg = new P_Record_Message(patient,acidentDescription);
        endpoint.route(null, msg, nh);
    }

    public String getPatientRecord(String Name)
    {
        //retrieving the patient record from database
        ArrayList<ArrayList<String>> patient;
        DBAccsess db = new DBAccsess();
        db.dbConnect();

        patient = db.queryDb("SELECT * FROM PDB WHERE PatientName ="+ "'" + Name + "'");

        Gson gson = new Gson();

        String patientJson = gson.toJson(patient);
        System.out.println(patientJson);
        return patientJson;
    }

    /**
     * Called when we receive a message.
     */
    public void deliver(Id id, Message message)
    {
        if(message.toString().contains("REQUEST:   "))
        {
            child_Rf.receiveRequest(message.toString(),(Ambulance_Request) message);
            System.out.println(child_Rf.toString());
        }
        else if (message.toString().contains("Confirmation"))
        {
            child_Hq.confirmaionRecived();
        }
        else if(message.toString().contains("Patient Record"))
        {
            child_M.reciveRecord((P_Record_Message)message);
        }
    }


    public void update(NodeHandle handle, boolean joined) {
    }


    public boolean forward(RouteMessage message) {
        return true;
    }


    /**
     * returns the leafset of the node this application is built on
     * @return
     */
    public LeafSet getNodeLeafset()
    {
        PastryNode node = (PastryNode)this.getNode();
        LeafSet leafset = node.getLeafSet();

        return leafset;
    }

    public String toString() {
        return "Communicating_Application "+endpoint.getId();
    }

}