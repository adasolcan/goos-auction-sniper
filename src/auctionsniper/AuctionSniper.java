package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;
    private final String itemId;
    private boolean isWinning = false;
    private SniperSnapshot snapshot;

    public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
        this.itemId = itemId;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
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
        sniperListener.sniperStateChanged(snapshot);
    }

    private void notifyChange() {
        sniperListener.sniperStateChanged(snapshot);
    }
}
