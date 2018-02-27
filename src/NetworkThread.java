import java.util.*;

public class NetworkThread extends Thread {

    public static final int GOSSIP_TRANSMISSION_DELAY = 1000;

    private volatile Queue<Integer> recipientQueue;
    private Map<Integer, GossipNode> gossipNodeMap;

    public NetworkThread(List<GossipNode> gossipNodes, int startNodeId) {
        this.recipientQueue = new LinkedList<>();
        this.recipientQueue.add(startNodeId);

        this.gossipNodeMap = new HashMap<>();
        for (GossipNode node : gossipNodes) {
            this.gossipNodeMap.put(node.getId(), node);
        }
    }

    @Override
    public void run() {
        System.out.println("NetworkThread running...");

        while (true) {
            if (!this.recipientQueue.isEmpty()) {
                int nodeId = this.recipientQueue.remove();
                GossipNode node = this.gossipNodeMap.get(nodeId);

                System.out.println(String.format("Gossip for %s scheduled", nodeId));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        node.setGossipReceived();
                        System.out.println(String.format("Gossip received by %s",
                                nodeId));
                    }
                }, GOSSIP_TRANSMISSION_DELAY);
            }
        }
    }

    public void addToRecipientQueue(int recipientId) {
        this.recipientQueue.add(recipientId);
    }
}
