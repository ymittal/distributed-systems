import java.util.List;
import java.util.Random;

public class GNode {

    private static Random random = new Random();

    private final int id;
    private List<Integer> neighbors;
    private boolean hasGossip;

    GNode(int id, List<Integer> neighbors) {
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

    public boolean hasGossip() {
        return this.hasGossip;
    }

    public void receiveGossip() {
        System.out.println(":Gossip received by node " + id);
        this.hasGossip = true;
    }
}
