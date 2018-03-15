import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

public class Simulator {

    public static void main(String[] args) throws InterruptedException {
        // comment the following line to output log to console
        LogManager.getLogManager().reset();

        int count = args.length;
        if (count < 2) {
            throw new IllegalArgumentException("At least two args required");
        }
        String edgeFilename = args[0];
        int startNodeId = Integer.parseInt(args[1]);

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
                    GNodeThread nodeThread = gossipNodeThreadMap.get(node);
                    nodeThread.spreadGossip();
                }
            }
        }
        System.exit(0);
    }
}
