package test.unit.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.Item;
import auctionsniper.xmpp.XMPPAuctionHouse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.endtoend.ApplicationRunner;
import test.endtoend.FakeAuctionServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

public class XMPPAuctionHouseTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item54321");
    private XMPPAuctionHouse auctionHouse;

    @Before
    public void openConnection() throws Exception {
        auctionHouse = XMPPAuctionHouse.connect(
                FakeAuctionServer.XMPP_HOSTNAME,
                ApplicationRunner.SNIPER_ID,
                ApplicationRunner.SNIPER_PASSWORD
        );
    }

    @Before
    public void startAuction() throws Exception {
        auctionServer.startSellingItem();
    }

    @After
    public void closeConnection() {
        if (auctionHouse != null) {
            auctionHouse.disconnect();
        }
    }

    @After
    public void stopAuction() {
        auctionServer.stop();
    }

    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {

        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = auctionHouse.auctionFor(new Item(auctionServer.getItemId(), 567));
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
    }


    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {
            }

            @Override
            public void auctionFailed() {
            }
        };
    }
}
