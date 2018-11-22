import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

public class Ambulance_Request implements Message {

    private String location;
    private String accident_description;
    private NodeHandle sender;
    private String[] patientRecord;

    public Ambulance_Request(String location, String acd, NodeHandle nh,String[] patientRecord) {

        this.location = location;
        this.accident_description = acd;
        this.sender = nh;
        this.patientRecord =patientRecord;

    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public NodeHandle getSender()
    {
        return sender;
    }
    public String[] getPatientRecord()
    {
        return patientRecord;
    }

    public String toString() {
        return "REQUEST:   "+"Location:  "+location+" / "+"Accident Description:  "+accident_description;
    }
}

