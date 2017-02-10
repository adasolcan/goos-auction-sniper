package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);
    private final Auction auction;
    private final Item item;
    private SniperSnapshot snapshot;

    public AuctionSniper(Item item, Auction auction) {
        this.auction = auction;
        this.item = item;
        this.snapshot = SniperSnapshot.joining(item.identifier);
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    public void auctionFailed() {
        snapshot = snapshot.failed();
        notifyChange();
    }

    public void addSniperListener(SniperListener listener) {
        listeners.addListener(listener);
    }

    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                int bid = price + increment;
                if (item.allowsBid(bid)) {
                    auction.bid(bid);
                    snapshot = snapshot.bidding(price, bid);
                } else {
                    snapshot = snapshot.losing(price);
                }
                break;
        }
        notifyChange();
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    private void notifyChange() {
        listeners.announce().sniperStateChanged(snapshot);
    }
}
