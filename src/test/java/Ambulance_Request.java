import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

public class Ambulance_Request implements Message {

    String Message_type = "Ambulance Request";
    private String location;
    private String accident_description;
    private NodeHandle sender;

    public Ambulance_Request(String location, String acd, NodeHandle nh) {

        this.location = location;
        this.accident_description = acd;
        this.sender = nh;

    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public NodeHandle getSender()
    {
        return sender;
    }

    public String toString() {
        return "REQUEST:   "+"Location:  "+location+" / "+"Accident Description:  "+accident_description;
    }
}

