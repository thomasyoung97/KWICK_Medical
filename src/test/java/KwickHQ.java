import com.google.gson.Gson;
import rice.p2p.commonapi.*;



public class KwickHQ implements Application{

        protected Endpoint endpoint;
        protected Node node;
        public int ref;
        public Id zoneId;

        public KwickHQ(Node node, int ref, Id zoneId) {
            this.ref = ref;
            this.zoneId = zoneId;
            this.node = node;
            this.endpoint = node.buildEndpoint(this, "KWICK_MEDICAL");

            this.endpoint.register();

        }


        public Node getNode() {
            return node;
        }

        public void routeMyMsg(Id id) {
            Message msg = new TestMessage(endpoint.getId(), id, this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
            endpoint.route(id, msg, null);
        }

        public void routeMyMsgDirect(NodeHandle nh) {
            Message msg = new TestMessage(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
            endpoint.route(null, msg, nh);
        }


        public void routeAmbulanceReq(NodeHandle nh)
        {
            Message msg = new AmbulanceRequest(endpoint.getId(), nh.getId(), this.toString(), node.getEnvironment().getTimeSource().currentTimeMillis());
            endpoint.route(null, msg, nh);
        }

        public  void routePatentRecord(NodeHandle nh)
        {
            Message msg = new PatentRecordMessage(endpoint.getId(), nh.getId(), this.toString(),
                    node.getEnvironment().getTimeSource().currentTimeMillis(),this.getPatientRecord());
            endpoint.route(null, msg, nh);
        }

        public void deliver(Id id, Message message) {
            long curr_time = node.getEnvironment().getTimeSource().currentTimeMillis();
            long sent_time = ((AmbulanceConfirmation)message).time;
            System.out.println(this + " received confirmation from" + " "+((AmbulanceConfirmation)message).owner + " " +((AmbulanceConfirmation)message).confirm + ". The message took " + (curr_time - sent_time) + " ms to arrive.");
        }

        public void update(NodeHandle handle, boolean joined) {
        }

        public boolean forward(RouteMessage message) {
            return true;
        }

        public String toString() {
            return "KWICK_HQ" + " " + ref;
        }


        public String getPatientRecord()
        {
        //retrieving the patient record from database
        String[] patient;
        DBAccsess db = new DBAccsess();
        db.dbConnect();
        patient = db.queryDb("SELECT * FROM PDB WHERE PatientName ="+ "'" + "Tom" + "'");

        Gson gson = new Gson();

        String patientJson = gson.toJson(patient);

        return patientJson;
    }
}





