import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {

    public static void main(String[] args) throws InterruptedException {
        int count = args.length;
        if (count < 2) {
            throw new IllegalArgumentException("At least two args required");
        }
        String edgeFilename = args[0];
        int startNodeId = Integer.parseInt(args[1]);

        // parse input file containing edge data
        List<GNode> gNodes = EdgeFileParser.parseFile(edgeFilename);
        // TODO: validate startNodeId
        NetworkThread networkThread = new NetworkThread(gNodes, startNodeId);

        Map<GNode, GNodeThread> gossipNodeThreadMap = new HashMap<>();
        for (GNode node : gNodes) {
            GNodeThread nodeThread = new GNodeThread(node, networkThread);
            gossipNodeThreadMap.put(node, nodeThread);
            nodeThread.start();
        }

        // start network thread
        networkThread.start();
        while (true) {
            for (GNode node : gNodes) {
                if (node.hasGossip()) {
                    GNodeThread nodeThread = gossipNodeThreadMap.get(node);
                    nodeThread.spreadGossip();
                }
            }
        }
    }
}
