package test.endtoend;

import org.junit.After;
import org.junit.Test;

public class EndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBinddingIn(auction);
//        auction.hasReceivedJoinRequestFromSniper();
//        auction.announceClosed();
//        application.showsSniperHasLostAuction();
    }

    // Additional cleanup
    @After public void stopAuction() {
        auction.stop();
    }

    @After public void stopApplication() {
        application.stop();
    }
}