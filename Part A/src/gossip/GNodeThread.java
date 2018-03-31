package gossip;

import java.util.Random;
import java.util.logging.Logger;

public class GNodeThread extends Thread {

    private static final int DELAY_UNTIL_GOSSIP_AGAIN = 500;
    private static Logger LOGGER = Logger.getLogger(GNodeThread.class.getName());

    private final float dropProb;
    private GNode node;
    private NetworkThread networkThread;

    private static Random random = new Random();

    public GNodeThread(GNode node, NetworkThread networkThread) {
        this.node = node;
        this.networkThread = networkThread;
        this.dropProb = 0f;
    }

    public GNodeThread(GNode node, NetworkThread networkThread, float dropProb) {
        this.node = node;
        this.networkThread = networkThread;
        this.dropProb = dropProb;
    }

    @Override
    public void run() {
        LOGGER.info("gossip.GNodeThread " + node.getId() + " running...");
        while (true) {
            if (node.hasGossip() && random.nextFloat() > this.dropProb) {
                int neighborId = this.node.getRandomNeighborId();
                this.networkThread.addToRecipientQueue(neighborId);
            }

            // delay before node can "attempt to" propagate gossip again
            try {
                sleep(DELAY_UNTIL_GOSSIP_AGAIN);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
