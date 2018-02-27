import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {

    public static void main(String[] args) throws InterruptedException {
        Graph graph = new SingleGraph("test");

        int count = args.length;
        if (count < 2) {
            throw new IllegalArgumentException("At least two args required");
        }
        String edgeFilename = args[0];
        List<GossipNode> gossipNodes = EdgeFileParser.parseFile(edgeFilename);

        int startNodeId = Integer.parseInt(args[1]);
        // TODO: validate startNodeId
        NetworkThread networkThread = new NetworkThread(gossipNodes, startNodeId);

        Map<GossipNode, GossipNodeThread> gossipNodeThreadMap = new HashMap<>();
        for (GossipNode node : gossipNodes) {
            GossipNodeThread nodeThread = new GossipNodeThread(node, networkThread);
            gossipNodeThreadMap.put(node, nodeThread);
        }

        networkThread.start();

        while (true) {
            for (GossipNode node : gossipNodes) {
                if (node.hasReceivedGossip()) {
                    GossipNodeThread nodeThread = gossipNodeThreadMap.get(node);
                    nodeThread.propagateGossip();
                }
            }
        }
    }
}
