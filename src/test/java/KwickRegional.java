import rice.p2p.commonapi.*;
import rice.p2p.scribe.*;
import rice.pastry.commonapi.PastryIdFactory;

public class KwickRegional implements Application, ScribeClient {

    protected Endpoint endpoint;
    protected Node node;
    public int ref;
    public Id zoneId;
    public Scribe myScribe;
    public Topic myTopic;

    public KwickRegional(Node node, int ref, Id zoneId) {
        this.ref = ref;
        this.zoneId = zoneId;
        this.node = node;
        this.endpoint = node.buildEndpoint(this, "KWICK_MEDICAL");

        myScribe = new ScribeImpl(node, "myScribe");
        myTopic = new Topic(new PastryIdFactory(node.getEnvironment()), this.zoneId.toString());

        this.endpoint.register();
    }

    public void subscribe() {
        myScribe.subscribe(myTopic, this);
    }


    public void deliver(Topic topic, ScribeContent content) {
        long curr_time = node.getEnvironment().getTimeSource().currentTimeMillis();
        long sent_time = ((TestScribeContent)content).time;
        System.out.println(this + " received a ScribeContent from " + ((TestScribeContent)content).owner + ". The content took " + (curr_time - sent_time) + " ms to arrive.");
    }

    public boolean anycast(Topic topic, ScribeContent content) {
        // one third of the nodes will return true
        boolean returnValue = ref%3 == 1;
        if(returnValue)
            System.out.println(this + " is interested in the anycast!" + '\n');
        else
            System.out.println(this + " has passed the anycast on to others." + '\n');
        return returnValue;
    }

    public void childAdded(Topic topic, NodeHandle child) {
    }

    public void childRemoved(Topic topic, NodeHandle child) {
    }

    public void subscribeFailed(Topic topic) {
    }

    public Node getNode() {
        return node;
    }

    public void routeMyMsg(Id id) {
        Message msg = new TestMessage(endpoint.getId(), id, this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
        endpoint.route(id, msg, null);
    }

    public void routeMyMsgDirect(NodeHandle nh) {
        Message msg = new TestMessage(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
        endpoint.route(null, msg, nh);
    }


    public void deliver(Id id, Message message) {
        long curr_time = node.getEnvironment().getTimeSource().currentTimeMillis();
        long sent_time = ((TestMessage)message).time;
        System.out.println(this + " received a message from " + ((TestMessage)message).owner + ". The message took " + (curr_time - sent_time) + " ms to arrive.");
    }

    public void update(NodeHandle handle, boolean joined) {
    }

    public boolean forward(RouteMessage message) {
        return true;
    }

    public String toString() {
        return "KWICK_REGIONAL" + " " + ref;
    }
}
