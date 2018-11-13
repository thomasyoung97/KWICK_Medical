import java.io.*;

import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.direct.*;
import rice.pastry.standard.RandomNodeIdFactory;


public class NetworkBoot {

    KwickHQ[] kwickHQS;
    KwickRegional[] regApps;
    KwickMobile[] mobileApps;

    Id zoneId;


    private int findFirstEmpty(KwickHQ[] a)
    {
        int i;
        for (i = 0; i < a.length; i++)
        {
            if (a[i] == null)
            {
                break;
            }
        }
        return i;
    }


    private int findFirstEmpty(KwickRegional[] a)
    {
        int i;
        for (i = 0; i < a.length; i++)
        {
            if (a[i] == null)
            {
                break;
            }
        }
        return i;
    }

    private int findFirstEmpty(KwickMobile[] a)
    {
        int i;
        for (i = 0; i < a.length; i++)
        {
            if (a[i] == null)
            {
                break;
            }
        }
        return i;
    }

    private int findFirstEmpty(TestApp[] a)
    {
        int i;
        for (i = 0; i < a.length; i++)
        {
            if (a[i] == null)
            {
                break;
            }
        }
        return i;
    }

    public NetworkBoot(Environment env, int numNodes, String fileName) throws Exception {
        // Generate NodeIds Randomly
        NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
        // construct the PastryNodeFactory
        PastryNodeFactory factory = new DirectPastryNodeFactory(nidFactory, new GenericNetwork(env, new File(fileName)), env);
        // create the NodeHandle to boot off of
        NodeHandle bootHandle = null;


        // construct the nodes and apps
        kwickHQS = new KwickHQ[numNodes];
        regApps = new KwickRegional[numNodes];
        mobileApps = new KwickMobile[numNodes];
        zoneId = nidFactory.generateNodeId();


        // creates the nodes.
        for (int curNode = 0; curNode < numNodes; curNode++)
        {
            // passing the null boothandle will cause the first node to start its own Pastry ring
            PastryNode node = factory.newNode(bootHandle);
            bootHandle = node.getLocalHandle();

            // a node may need to send several messages to fully boot into the ring
            synchronized (node) {
                while (!node.isReady() && !node.joinFailed()) {
                    node.wait(500);
                    // abort if can't join
                    if (node.joinFailed()) {
                        throw new IOException("Could not join the FreePastry ring.  Reason:" + node.joinFailedReason());
                    }
                }
            }

            System.out.println("Finished creating new node " + node);

            // constructing KqickHq appllications on some of the nodes.
            if(curNode < 5)
            {
                KwickMobile mobApp = new KwickMobile(node, curNode, zoneId);
                mobileApps[findFirstEmpty(mobileApps)] = mobApp;
            }
            else if (curNode >= 5 && curNode < 8)
            {
                //constructing Kwick regional on some of the nodes.
                KwickRegional regApp = new KwickRegional(node, curNode, zoneId);
                regApps[findFirstEmpty(regApps)] = regApp;
            }
            else if (curNode >= 8 && curNode < 12)
            {
                KwickHQ Hqapp = new KwickHQ(node, curNode, zoneId);
                kwickHQS[findFirstEmpty(kwickHQS)] = Hqapp;
            }

        }

        ///TESTING ZONE
       // kwickHQS[0].routeAmbulanceReq(regApps[0].getNode().getLocalNodeHandle());

        try
        {
            kwickHQS[0].routeMyMsg(mobileApps[0].getNode().getId());
        }
       catch(Exception e)
        {
            System.out.println(e);
        }


        // wait for 30 seconds to make sure that all the messages have been sent/received
        env.getTimeSource().sleep(30000);
        System.out.println('\n' + "Finishing the test...");
        env.destroy();
        System.out.println("Done!");

    }


    public static void main(String[] args) throws Exception {
        Environment env = new Environment();
        //the number of nodes
        int numNodes = 15;
        String fileName = "LatencyMatrix";
        NetworkBoot boot = new NetworkBoot(env, numNodes, fileName);
    }
}
