import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

public class AmbulanceConfirmation implements Message
{

    Id from;
    Id to;
    String owner;
    long time;
    String confirm;

    public AmbulanceConfirmation(Id from, Id to, String owner, long time, String confirm) {
        this.from = from;
        this.to = to;
        this.owner = owner;
        this.time = time;
        this.confirm = confirm;

    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String toString() {
        return "Message (owned by " + owner + ") was sent from " + from + " to " + to + " at " + time;
    }
}
