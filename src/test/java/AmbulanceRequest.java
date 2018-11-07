import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.scribe.ScribeContent;

public class AmbulanceRequest implements Message {

    Id from;
    Id to;
    String owner;
    long time;

    public AmbulanceRequest(Id from, Id to, String owner, long time) {
        this.from = from;
        this.to = to;
        this.owner = owner;
        this.time = time;
    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String toString() {
        return "Message (owned by " + owner + ") was sent from " + from + " to " + to + " at " + time;
    }
}




