import java.io.IOException;
import java.nio.ByteBuffer;
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
    static protected Kwick_Reigonal child_Rf;

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

    public void setApplicationExtentsion(Kwick_Reigonal test)
    {
        child_Rf = test;
    }

    public void routeMyMsgDirect(NodeHandle nh) {
        Message msg = new TestMessage(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
        endpoint.route(null, msg, nh);
    }





    public void routeAmbulanceRequest(NodeHandle nh)
    {
        Message msg = new Ambulance_Request(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis(),"Edinburgh","-Major Trauma");
        endpoint.route(null, msg, nh);
    }

    /**
     * communication protocol for any application sending a patient record
     * @param nh - node header to send the record to
     * @param Name - Name of the patient you request is regarding
     */
    public void routePatentRecord(NodeHandle nh, String Name)
    {
        Message msg = new P_Record_Message(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis(),getPatientRecord(Name));
        endpoint.route(null, msg, nh);
    }
    public String getPatientRecord(String Name)
    {
        //retrieving the patient record from database
        String[] patient;
        DBAccsess db = new DBAccsess();
        db.dbConnect();
        patient = db.queryDb("SELECT * FROM PDB WHERE PatientName ="+ "'" + Name + "'");

        Gson gson = new Gson();

        String patientJson = gson.toJson(patient);

        return patientJson;
    }




    /**
     * Called when we receive a message.
     */

    public void deliver(Id id, Message message)
    {
       // System.out.println(this+" received "+message);
        if(((Ambulance_Request)message).Message_type == "Ambulance Request")
        {
            child_Rf.receiveRequest(message.toString());
            System.out.println(child_Rf.toString());
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