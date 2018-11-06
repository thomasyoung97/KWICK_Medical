import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.scribe.ScribeContent;

public class TestMessage implements Message {

    Id from;
    Id to;
    String owner;
    long time;

    public TestMessage(Id from, Id to, String owner, long time) {
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




class TestScribeContent implements ScribeContent {

    Id from;
    String owner;
    long time;
    String message;


    public TestScribeContent(Id from, String owner, long time, String message){
        this.from = from;
        this.owner = owner;
        this.time = time;
        this.message = message;
    }

    public String toString() {
        return "Scribe message (owned by " + owner + ") was sent from " + from + " at " + time;
    }
}

class ScribePatientRecord implements ScribeContent {

        Id from;
        String owner;
        long time;
        String recordJson;


    public ScribePatientRecord(Id from, String owner, long time, String patientrecord){
        this.from = from;
        this.owner = owner;
        this.time = time;
        this.recordJson = patientrecord;
        }

    public String toString()
    {
        return "Scribe message (owned by " + owner + ") was sent from " + from + " at " + time;

    }
}