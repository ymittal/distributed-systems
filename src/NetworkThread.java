import java.util.*;
import java.util.logging.Logger;

public class NetworkThread extends Thread {

    private static Logger LOGGER = Logger.getLogger(NetworkThread.class.getName());
    private static final int MAX_DELAY = 1100;
    private static final int MIN_DELAY = 900;

    private volatile Queue<Integer> recipientQueue;
    private Map<Integer, GNode> gossipNodeMap;
    private boolean hasStartedGossiping;

    private Random rand;

    public NetworkThread(List<GNode> gNodes, int startNodeId) {
        this.recipientQueue = new LinkedList<>();
        this.recipientQueue.add(startNodeId);

        this.gossipNodeMap = new HashMap<>();
        for (GNode node : gNodes) {
            this.gossipNodeMap.put(node.getId(), node);
        }
        this.hasStartedGossiping = false;
        rand = new Random();
    }

    @Override
    public void run() {
        // System.out.println("NetworkThread running...");

        while (true) {
            if (!this.recipientQueue.isEmpty()) {
                int nodeId = this.recipientQueue.remove();
                GNode node = this.gossipNodeMap.get(nodeId);

                if (!hasStartedGossiping) {
                    node.receiveGossip();
                    hasStartedGossiping = true;
                } else {
                    LOGGER.info("Gossip scheduled for node " + nodeId);

                    int delay = rand.nextInt((MAX_DELAY - MIN_DELAY) + 1) + MIN_DELAY;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            node.receiveGossip();
                        }
                    }, delay);
                }
            }
        }
    }

    public void addToRecipientQueue(int recipientId) {
        this.recipientQueue.add(recipientId);
    }
}
