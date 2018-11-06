import rice.p2p.commonapi.*;
import rice.p2p.scribe.*;
import rice.pastry.commonapi.PastryIdFactory;
import com.google.gson.Gson;
import java.util.Scanner;

public class KwickHq implements Application, ScribeClient
{
    protected Endpoint endpoint;
    protected Node node;
    public int ref;
    public Id zoneId;
    public Scribe myScribe;
    public Topic myTopic;


    public KwickHq(Node node, int ref, Id zoneId)
    {
        this.ref = ref;
        this.zoneId = zoneId;
        this.node = node;
        this.endpoint = node.buildEndpoint(this, "KwicMedical");

        myScribe = new ScribeImpl(node, "myScribe");
        myTopic = new Topic(new PastryIdFactory(node.getEnvironment()), "KwickHq");


        this.endpoint.register();


    }

    public void subscribe()
    {
        myScribe.subscribe(myTopic, this);
    }


    public void sendMulticast() {
        TestScribeContent tsc = new TestScribeContent(endpoint.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis(),this.getPatientRecord());
        myScribe.publish(new Topic(new PastryIdFactory((node.getEnvironment())),"kwickRegional"), tsc);
    }

    public void routeMyMsgDirect(NodeHandle nh) {
        Message msg = new TestMessage(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
        endpoint.route(null, msg, nh);
    }


    public boolean forward(RouteMessage routeMessage) {
        return false;
    }

    public void deliver(Id id, Message message) {

    }

    public void update(NodeHandle nodeHandle, boolean b) {

    }

    public boolean anycast(Topic topic, ScribeContent scribeContent) {
        return false;
    }


    public void deliver(Topic topic, ScribeContent scribeContent)
    {
        long curr_time = node.getEnvironment().getTimeSource().currentTimeMillis();
        long sent_time = ((TestScribeContent)scribeContent).time;
        System.out.println(this + " received a ScribeContent from " + ((TestScribeContent)scribeContent).owner + ". The content took " + (curr_time - sent_time) + " ms to arrive." + "    " + ((TestScribeContent)scribeContent).message);
    }

    public void childAdded(Topic topic, NodeHandle nodeHandle) {

    }

    public void childRemoved(Topic topic, NodeHandle nodeHandle) {

    }

    public void subscribeFailed(Topic topic) {

    }






    public String getPatientRecord()
    {
        //retrieving the patient record from database
        String[] patient;
        DBAccsess db = new DBAccsess();
        db.dbConnect();
        patient = db.queryDb("SELECT * FROM PDB WHERE PatientName ="+ "'" + "Tom" + "'");

        Gson gson = new Gson();

        String patientJson = gson.toJson(patient);

        return patientJson;
    }






    public void UI ()
    {

    }

}

class NewThread extends Thread
{

    KwickHq parent;
    NodeHandle to;
    int t;


    public NewThread(KwickHq parent, NodeHandle to, int t){
        this.parent = parent;
        this.to = to;
        this.t = t;
    }

    public void run(){
        try{
            sleep(t*1000);
            parent.routeMyMsgDirect(to);
        }catch(Exception e){}
    }
}