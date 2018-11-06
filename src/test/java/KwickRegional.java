import rice.p2p.commonapi.*;
import rice.p2p.scribe.*;
import rice.pastry.commonapi.PastryIdFactory;


public class KwickRegional implements Application, ScribeClient
{
    protected Endpoint endpoint;
    protected Node node;
    public int ref;
    public Id zoneId;
    public Scribe myScribe;
    public Topic myTopic;

    public KwickRegional(Node node, int ref, Id zoneId)
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
        TestScribeContent tsc = new TestScribeContent(endpoint.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis(), "im not so sure");
        myScribe.publish(myTopic, tsc);
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
        System.out.println(this + " received a ScribeContent from " + ((TestScribeContent)scribeContent).owner + ". The content took " + (curr_time - sent_time) + " ms to arrive.");
    }

    public void childAdded(Topic topic, NodeHandle nodeHandle) {

    }

    public void childRemoved(Topic topic, NodeHandle nodeHandle) {

    }

    public void subscribeFailed(Topic topic) {

    }
}

