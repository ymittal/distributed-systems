public class GNodeThread extends Thread {

    private static final int DELAY_UNTIL_GOSSIP_AGAIN = 1000;

    private GNode node;
    private NetworkThread networkThread;

    private volatile boolean canGossip;

    public GNodeThread(GNode node, NetworkThread networkThread) {
        this.node = node;
        this.networkThread = networkThread;
        canGossip = false;
    }

    @Override
    public void run() {
        System.out.println("GNodeThread " + node.getId() + " running...");
        while (true) {
            if (canGossip) {
                int neighborId = this.node.getRandomNeighborId();
                this.networkThread.addToRecipientQueue(neighborId);
                canGossip = false;
                try {
                    sleep(DELAY_UNTIL_GOSSIP_AGAIN);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void spreadGossip() {
        this.canGossip = true;
    }
}
