import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.scribe.ScribeContent;

public class P_Record_Message implements Message {

    String[] record;
    String acidentDescription;


    public P_Record_Message(String[] record,String acidentDescription) {

        this.record = record;
        this.acidentDescription = acidentDescription;
    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String[] getRecord()
    {
        return record;
    }
    public String getAcidentDescription()
    {
        return acidentDescription;
    }

    public String toString() {
        return "Patient Record " + record;
    }
}

