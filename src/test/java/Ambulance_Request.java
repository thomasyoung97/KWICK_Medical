import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

public class Ambulance_Request implements Message {

    Id from;
    Id to;
    String Message_type = "Ambulance Request";
    String owner;
    String location;
    String accident_description;

    public Ambulance_Request(Id from, Id to, String owner, long time,String location, String acd) {
        this.from = from;
        this.to = to;
        this.owner = owner;
        this.location = location;
        this.accident_description = acd;

    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String toString() {
        return "Message (owned by " + owner + ") was sent from " + from + " to " + to + " at " + accident_description;
    }
}

