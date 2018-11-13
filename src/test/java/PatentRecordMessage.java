import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.scribe.ScribeContent;

public class PatentRecordMessage implements Message {

    Id from;
    Id to;
    String owner;
    String record;
    long time;

    public PatentRecordMessage(Id from, Id to, String owner, long time, String record) {
        this.from = from;
        this.to = to;
        this.owner = owner;
        this.record = record;
        this.time = time;
    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String toString() {
        return "Message (owned by " + owner + ") was sent from " + from + " to " + to + " at " + time;
    }
}
