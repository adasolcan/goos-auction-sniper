package auctionsniper.ui;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
    public final static String[] STATUS_TEXT = {
            "Joining", "Bidding", "Winning", "Losing", "Lost", "Won", "Failed"
    };
    private final static SniperSnapshot STARTING_UP = SniperSnapshot.joining("");
    private static SniperSnapshot sniperSnapshot;

    public SnipersTableModel() {
        sniperSnapshot = STARTING_UP;
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return 1;
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return sniperSnapshot.itemId;
            case LAST_PRICE:
                return sniperSnapshot.lastPrice;
            case LAST_BID:
                return sniperSnapshot.lastBid;
            case SNIPER_STATE:
                return textFor(sniperSnapshot.state);
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.sniperSnapshot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }
}