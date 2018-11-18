import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Vector;

import rice.environment.Environment;
import rice.p2p.commonapi.*;
import rice.p2p.commonapi.rawserialization.RawMessage;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.commonapi.PastryIdFactory;
import rice.pastry.direct.*;
import rice.pastry.leafset.LeafSet;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;



public class NetworkBoot {

    // this will keep track of our applications
    Vector<Communicating_Application> apps = new Vector<Communicating_Application>();

    /**
     * This constructor launches numNodes PastryNodes.  They will bootstrap
     * to an existing ring if one exists at the specified location, otherwise
     * it will start a new ring.
     *
     * @param bindport the local port to bind to
     * @param bootaddress the IP:port of the node to boot from
     * @param numNodes the number of nodes to create in this JVM
     * @param env the environment for these nodes
     * @param useDirect true for the simulator, false for the socket protocol
     */
    public NetworkBoot(int bindport, InetSocketAddress bootaddress, int numNodes, Environment env, boolean useDirect) throws Exception {

        // Generate the NodeIds Randomly
        NodeIdFactory nidFactory = new RandomNodeIdFactory(env);

        // construct the PastryNodeFactory
        PastryNodeFactory factory;
        if (useDirect) {
            NetworkSimulator<DirectNodeHandle,RawMessage> sim = new EuclideanNetwork<DirectNodeHandle,RawMessage>(env);
            factory = new DirectPastryNodeFactory(nidFactory, sim, env);
        } else {
            factory = new SocketPastryNodeFactory(nidFactory, bindport, env);
        }

        IdFactory idFactory = new PastryIdFactory(env);

        Object bootHandle = null;

        // loop to construct the nodes/apps
        for (int curNode = 0; curNode < numNodes; curNode++) {
            // This will return null if we there is no node at that location

            // construct a node, passing the null boothandle on the first loop will cause the node to start its own ring
            PastryNode node = factory.newNode();

            // construct a new Communicating_Application
            Communicating_Application app = new Communicating_Application(node, idFactory);

            apps.add(app);

            node.boot(bootHandle);

            if (bootHandle == null) {
                if (useDirect) {
                    bootHandle = node.getLocalHandle();
                } else {
                    // This will return null if we there is no node at that location
                    bootHandle = bootaddress;
                }
            }

            // the node may require sending several messages to fully boot into the ring
            synchronized(node) {
                while(!node.isReady() && !node.joinFailed()) {
                    // delay so we don't busy-wait
                    node.wait(500);

                    // abort if can't join
                    if (node.joinFailed()) {
                        throw new IOException("Could not join the FreePastry ring.  Reason:"+node.joinFailedReason());
                    }
                }
            }

            System.out.println("Finished creating new node "+node);

        }

        Kwick_Hq test = new Kwick_Hq(apps.get(0));
        Kwick_Reigonal regional = new Kwick_Reigonal(apps.get(1));

        // wait 10 seconds
        env.getTimeSource().sleep(1000);



//    Communicating_Application tempApp = (Communicating_Application)apps.get(0);
//    tempApp.sendMyMsgDirect(((PastryNode)tempApp.getNode()).getLeafSet().get(-1));
//    if (true) return;
        /*
        // for each app
        Iterator<Communicating_Application> appIterator = apps.iterator();
        while(appIterator.hasNext()) {
            Communicating_Application app = (Communicating_Application)appIterator.next();
            PastryNode node = (PastryNode)app.getNode();
            }
            // send directly to my leafset (including myself)
            LeafSet leafSet = node.getLeafSet();

            // this is a typical loop to cover your leafset.  Note that if the leafset
            // overlaps, then duplicate nodes will be sent to twice
            for (int i=-leafSet.ccwSize(); i<=leafSet.cwSize(); i++) {
                // select the item
                NodeHandle nh = leafSet.get(i);

                // send the message directly to the node
                app.sendMyMsgDirect(nh);

                // wait a bit
                env.getTimeSource().sleep(100);
            }
            */
        //}
    }

    /**
     * Usage:
     * java [-cp FreePastry-<version>.jar] rice.tutorial.appsocket.NetworkBoot localbindport bootIP bootPort numNodes
     *   or
     * java [-cp FreePastry-<version>.jar] rice.tutorial.appsocket.NetworkBoot -direct numNodes
     *
     * example java rice.tutorial.DistTutorial 9001 pokey.cs.almamater.edu 9001 10
     * example java rice.tutorial.DistTutorial -direct 10
     */
    public static void main(String[] args) throws Exception {
        try {


            boolean useDirect = true;


            // Loads pastry settings
            Environment env;
            if (useDirect) {
                env = Environment.directEnvironment();
            } else {
                env = new Environment();

                // disable the UPnP setting (in case you are testing this on a NATted LAN)
                env.getParameters().setString("nat_search_policy","never");
            }

            int bindport = 0;
            InetSocketAddress bootaddress = null;

            // the number of nodes to use is always the last param
            int numNodes =10;

            if (!useDirect) {
                // the port to use locally
                bindport = Integer.parseInt(args[0]);

                // build the bootaddress from the command line args
                InetAddress bootaddr = InetAddress.getByName(args[1]);
                int bootport = Integer.parseInt(args[2]);
                bootaddress = new InetSocketAddress(bootaddr,bootport);
            }

            // launch our node!
            NetworkBoot dt = new NetworkBoot(bindport, bootaddress, numNodes, env, useDirect);
        } catch (Exception e) {
            // remind user how to use
            System.out.println("Usage:");
            System.out.println("java [-cp FreePastry-<version>.jar] rice.tutorial.appsocket.NetworkBoot localbindport bootIP bootPort numNodes");
            System.out.println("  or");
            System.out.println("java [-cp FreePastry-<version>.jar] rice.tutorial.appsocket.NetworkBoot -direct numNodes");
            System.out.println();
            System.out.println("example java rice.tutorial.DistTutorial 9001 pokey.cs.almamater.edu 9001 10");
            System.out.println("example java rice.tutorial.DistTutorial -direct 10");
            throw e;
        }
    }
}