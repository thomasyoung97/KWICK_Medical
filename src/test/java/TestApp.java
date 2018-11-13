import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.scribe.Scribe;
import rice.p2p.scribe.ScribeClient;
import rice.p2p.scribe.ScribeContent;
import rice.p2p.scribe.ScribeImpl;
import rice.p2p.scribe.Topic;
import rice.pastry.commonapi.PastryIdFactory;

public class TestApp implements Application, ScribeClient {

    protected Endpoint endpoint;
    protected Node node;
    public int ref;
    public Id zoneId;
    public Scribe myScribe;
    public Topic myTopic;

    public TestApp(Node node, int ref, Id zoneId) {
        this.ref = ref;
        this.zoneId = zoneId;
        this.node = node;
        this.endpoint = node.buildEndpoint(this, "myApp");

        myScribe = new ScribeImpl(node, "myScribe");
        myTopic = new Topic(new PastryIdFactory(node.getEnvironment()), this.zoneId.toString());

        this.endpoint.register();
    }

    public void subscribe() {
        myScribe.subscribe(myTopic, this);
    }

    public void sendMulticast() {
        TestScribeContent tsc = new TestScribeContent(endpoint.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis(),"asdf");
        myScribe.publish(myTopic, tsc);
    }

    public void deliver(Topic topic, ScribeContent content) {
        long curr_time = node.getEnvironment().getTimeSource().currentTimeMillis();
        long sent_time = ((TestScribeContent)content).time;
        System.out.println(this + " received a ScribeContent from " + ((TestScribeContent)content).owner + ". The content took " + (curr_time - sent_time) + " ms to arrive.");
    }

    public void sendAnycast() {
        TestScribeContent tsc = new TestScribeContent(endpoint.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis(),"asdf");
        myScribe.anycast(myTopic, tsc);
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

    public void routeMyMsgLater(NodeHandle nh, int delay){
        NewThread nt = new NewThread(this, nh, delay);
        nt.start();
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
        return "MyApp" + ref;
    }
}

class NewThread extends Thread{

    TestApp parent;
    NodeHandle to;
    int t;

    public NewThread(TestApp parent, NodeHandle to, int t){
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