package simulator;

import gossip.GNode;
import gossip.GNodeThread;
import gossip.NetworkThread;
import util.EdgeFileParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ProbSimulator {

    private static Logger LOGGER = Logger.getLogger(ProbSimulator.class.getName());
    private static Random random = new Random();

    public static void main(String[] args) {
        // comment the following line to output log to console
        LogManager.getLogManager().reset();

        int count = args.length;
        if (count < 3) {
            throw new IllegalArgumentException("At least three args required");
        }
        String edgeFilename = args[0];
        int startNodeId = Integer.parseInt(args[1]);
        float dropProb = Float.parseFloat(args[2]);

        // true for when drop probability needs to be used
        // (for efficiency, need not generate random floats)
        boolean doRandom = dropProb != 0.0;
        if (doRandom) LOGGER.info("using msg drop probability " + dropProb);

        // parse input file containing edge data
        List<GNode> gNodes = EdgeFileParser.parseFile(edgeFilename);
        NetworkThread networkThread = new NetworkThread(gNodes, startNodeId);

        Map<GNode, GNodeThread> gossipNodeThreadMap = new HashMap<>();
        for (GNode node : gNodes) {
            GNodeThread nodeThread = new GNodeThread(node, networkThread);
            gossipNodeThreadMap.put(node, nodeThread);
            nodeThread.start();
        }

        // start network thread
        networkThread.start();

        int numNodesReceived = 0;
        while (numNodesReceived < gNodes.size()) {
            numNodesReceived = 0;
            for (GNode node : gNodes) {
                if (node.hasGossip()) {
                    numNodesReceived += 1;
                    if (!doRandom || random.nextFloat() > dropProb) {
                        GNodeThread nodeThread = gossipNodeThreadMap.get(node);
                        nodeThread.spreadGossip();
                    }
                }
            }
        }
        System.exit(0);
    }
}
