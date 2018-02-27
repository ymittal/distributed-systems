public class GossipNodeThread extends Thread {

    private GossipNode gossipNode;
    private NetworkThread networkThread;

    public GossipNodeThread(GossipNode gossipNode,
                            NetworkThread networkThread) {
        this.gossipNode = gossipNode;
        this.networkThread = networkThread;
    }

    @Override
    public void run() {
        System.out.println(String.format("GossipNodeThread %s running...",
                this.gossipNode.getId()));
    }

    public void propagateGossip() {
        int neighborId = this.gossipNode.getRandomNeighborId();
        this.networkThread.addToRecipientQueue(neighborId);
    }
}
