import rice.p2p.commonapi.*;
import rice.p2p.scribe.*;
import rice.pastry.commonapi.PastryIdFactory;

public class KwickRegional implements Application{

    protected Endpoint endpoint;
    protected Node node;
    public int ref;
    public Id zoneId;

    public KwickRegional(Node node, int ref, Id zoneId) {
        this.ref = ref;
        this.zoneId = zoneId;
        this.node = node;
        this.endpoint = node.buildEndpoint(this, "KWICK_MEDICAL");

        this.endpoint.register();
    }


    public Node getNode() {
        return node;
    }

    public void routeMyMsg(Id id) {
        Message msg = new TestMessage(endpoint.getId(), id, this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
        endpoint.route(id, msg, null);
    }

    public void confirmAmbulance(Id id)
    {
        Message msg = new AmbulanceConfirmation(endpoint.getId(), id, this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis(), "CONFIRM");
        endpoint.route(id, msg, null);
    }

    public void routeMyMsgDirect(NodeHandle nh) {
        Message msg = new TestMessage(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
        endpoint.route(null, msg, nh);
    }


    public void deliver(Id id, Message message) {
        long curr_time = node.getEnvironment().getTimeSource().currentTimeMillis();
        long sent_time = ((AmbulanceRequest)message).time;
        System.out.println(this + " received an ambulance request from " + ((AmbulanceRequest)message).owner + ". The message took " + (curr_time - sent_time) + " ms to arrive.");
        this.confirmAmbulance(((AmbulanceRequest) message).from);
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
