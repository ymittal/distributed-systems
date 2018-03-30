package simulator;

import gossip.GNode;
import gossip.GNodeThread;
import gossip.NetworkThread;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import util.EdgeFileParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

public class GraphSimulator {

    private static final String RED = "red";
    private static final String GREEN = "green";

    private static final String UI_CLASS = "ui.class";
    private static final String UI_LABEL = "ui.label";

    private static org.graphstream.graph.Graph graph;

    public static void main(String[] args) throws InterruptedException {
        // comment the following line to output log to console
        LogManager.getLogManager().reset();

        int count = args.length;
        if (count < 2) {
            throw new IllegalArgumentException("At least two args required");
        }
        String edgeFilename = args[0];
        int startNodeId = Integer.parseInt(args[1]);

        initVisualization();
        // parse input file containing edge data
        List<GNode> gNodes = EdgeFileParser.parseFile(edgeFilename);
        Map<Integer, Node> visNodeMap = setupVisualizationNodes(gNodes, startNodeId);

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
                    Node visNode = visNodeMap.get(node.getId());
                    if (RED.equals(visNode.getAttribute(UI_CLASS))) {
                        visNode.setAttribute(UI_CLASS, GREEN);
                    }
                    numNodesReceived += 1;
                    GNodeThread nodeThread = gossipNodeThreadMap.get(node);
                    nodeThread.spreadGossip();
                }
            }
        }
    }

    private static void initVisualization() {
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        graph = new SingleGraph("PUSH Protocol Visualization");
        graph.display();
        graph.addAttribute("ui.antialias");
        graph.addAttribute("stylesheet", "graph { padding : 48px; }"
                + "node { size: 14px; fill-mode: plain; }"
                + "node.red { fill-color: red; }"
                + "node.green { fill-color: green; }");
    }

    private static Map<Integer, Node> setupVisualizationNodes(List<GNode> gNodes, int startNodeId) {
        Map<Integer, Node> visNodeMap = new HashMap<>();
        for (GNode node : gNodes) {
            visNodeMap.put(node.getId(),
                    graph.addNode(String.valueOf(node.getId())));
        }
        for (GNode node : gNodes) {
            String nodeId = String.valueOf(node.getId());
            for (Integer neighbor : node.getNeighborIds()) {
                String neighborId = String.valueOf(neighbor);
                try {
                    graph.addEdge(nodeId + neighborId, nodeId, neighborId);
                } catch (EdgeRejectedException ex) {
                    // I know! But better than having to create a set of
                    // (nodeId + neighborId) and check if the edge is
                    // already there
                }
            }

            Node visNode = visNodeMap.get(node.getId());
            visNode.addAttribute(UI_LABEL, node.getId());
            if (node.getId() == startNodeId) {
                visNode.addAttribute(UI_CLASS, GREEN);
            } else {
                visNode.addAttribute(UI_CLASS, RED);
            }
        }
        return visNodeMap;
    }
}
