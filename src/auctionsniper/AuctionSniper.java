package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);
    private final Auction auction;
    private final String itemId;
    private boolean isWinning = false;
    private SniperSnapshot snapshot;

    public AuctionSniper(String itemId, Auction auction) {
        this.auction = auction;
        this.itemId = itemId;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    public void addSniperListener(SniperListener listener) {
        listeners.addListener(listener);
    }

    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == PriceSource.FromSniper;
        if (isWinning) {
            snapshot = snapshot.winning(price);
        } else {
            int bid = price + increment;
            auction.bid(bid);
            snapshot = snapshot.bidding(price, bid);
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
