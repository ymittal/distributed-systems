package gossip;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class GNode {

    private static Logger LOGGER = Logger.getLogger(GNode.class.getName());
    private static Random random = new Random();

    private final int id;
    private List<Integer> neighbors;
    private boolean hasGossip;

    public GNode(int id, List<Integer> neighbors) {
        this.id = id;
        this.neighbors = neighbors;
        this.hasGossip = false;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getNeighborIds() {
        return neighbors;
    }

    /**
     * @return id of a random node neighbor
     */
    public int getRandomNeighborId() {
        int idx = random.nextInt(this.neighbors.size());
        return this.neighbors.get(idx);
    }

    public boolean hasGossip() {
        return this.hasGossip;
    }

    /**
     * Receives gossip
     */
    public void receiveGossip() {
        LOGGER.info("Gossip received by node " + id);
        if (!hasGossip) {
            System.out.println(String.format("node %s\t%s",
                    id,
                    new Date()));
        }
        this.hasGossip = true;
    }
}
