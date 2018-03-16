package gossip;

import java.util.logging.Logger;

public class GNodeThread extends Thread {

    private static final int DELAY_UNTIL_GOSSIP_AGAIN = 500;
    private static Logger LOGGER = Logger.getLogger(GNodeThread.class.getName());
    private GNode node;
    private NetworkThread networkThread;

    private volatile boolean canSpreadGossip;

    public GNodeThread(GNode node, NetworkThread networkThread) {
        this.node = node;
        this.networkThread = networkThread;
        canSpreadGossip = false;
    }

    @Override
    public void run() {
        LOGGER.info("gossip.GNodeThread " + node.getId() + " running...");
        while (true) {
            if (canSpreadGossip) {
                int neighborId = this.node.getRandomNeighborId();
                this.networkThread.addToRecipientQueue(neighborId);
                canSpreadGossip = false;

                try {
                    sleep(DELAY_UNTIL_GOSSIP_AGAIN);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void spreadGossip() {
        this.canSpreadGossip = true;
    }
}
