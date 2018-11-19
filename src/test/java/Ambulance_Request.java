import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

public class Ambulance_Request implements Message {

    String Message_type = "Ambulance Request";
    private String location;
    private String accident_description;

    public Ambulance_Request(String location, String acd) {

        this.location = location;
        this.accident_description = acd;

    }

    public int getPriority() {
        return Message.LOW_PRIORITY;
    }

    public String toString() {
        return "REQUEST:   "+"Location:  "+location+" / "+"Accident Description:  "+accident_description;
    }
}

