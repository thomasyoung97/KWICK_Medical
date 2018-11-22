import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.scribe.ScribeContent;

public class Ambulance_Confirmation implements Message {

    String owner;


    public Ambulance_Confirmation() {
        this.owner = owner;

    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String toString() {
        return "Confirmation";
    }
}


