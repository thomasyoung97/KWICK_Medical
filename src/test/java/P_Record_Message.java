import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.scribe.ScribeContent;

public class P_Record_Message implements Message {

    Id from;
    Id to;
    String owner;
    String record;
    long time;

    public P_Record_Message(Id from, Id to, String owner, long time, String record) {
        this.from = from;
        this.to = to;
        this.owner = owner;
        this.time = time;
        this.record = record;
    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String toString() {
        return "Patient Record (sent from application " + owner + ") was sent from " + from + " to " + to + " at " + time + record;
    }
}

