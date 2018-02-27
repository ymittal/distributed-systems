import java.util.List;
import java.util.Random;

public class GossipNode {

    private static Random random = new Random();

    private final int id;
    private List<Integer> neighbors;
    private boolean hasGossip;

    GossipNode(int id, List<Integer> neighbors) {
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

    public int getRandomNeighborId() {
        int idx = random.nextInt(this.neighbors.size());
        return this.neighbors.get(idx);
    }

    public boolean hasReceivedGossip() {
        return this.hasGossip;
    }

    public void setGossipReceived() {
        this.hasGossip = true;
    }
}
