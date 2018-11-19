import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.scribe.ScribeContent;

public class Ambulance_Confirmation implements Message {

    String message_type = "Ambulance_Confirmation";
    Id from;
    Id to;
    String owner;
    long time;

    public Ambulance_Confirmation(Id from, Id to, String owner, long time) {
        this.from = from;
        this.to = to;
        this.owner = owner;
        this.time = time;
    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String toString() {
        return "Confirmation";
    }
}


