package auctionsniper.xmpp;

import auctionsniper.Announcer;
import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {
    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

    private final Announcer<AuctionEventListener> auctionEventListeners;
    private final Chat chat;

    public XMPPAuction(XMPPConnection connection, String auctionJID) {
        this.auctionEventListeners = Announcer.to(AuctionEventListener.class);
        this.chat = connection.getChatManager().createChat(
                auctionJID,
                new AuctionMessageTranslator(connection.getUser(), auctionEventListeners.announce())
        );
        addAuctionEventListener(chatDisconnectorFor());
    }

    public void bid(int amount) {
        sendMessage(String.format(BID_COMMAND_FORMAT, amount));
    }

    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    public void addAuctionEventListener(AuctionEventListener listener) {
        auctionEventListeners.addListener(listener);
    }

    private AuctionEventListener chatDisconnectorFor() {
        return new AuctionEventListener() {
            public void auctionClosed() {
            }

            public void currentPrice(int price, int increment, PriceSource priceSource) {
            }
        };
    }

    private void sendMessage(final String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}