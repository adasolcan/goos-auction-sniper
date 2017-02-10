package auctionsniper.ui;

import auctionsniper.AuctionSniper;
import auctionsniper.SniperListener;
import auctionsniper.SniperPortfolio.PortfolioListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;
import com.objogate.exception.Defect;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, PortfolioListener {
    public final static String[] STATUS_TEXT = {
            "Joining", "Bidding", "Winning", "Losing", "Lost", "Won", "Failed"
    };
    private static ArrayList<SniperSnapshot> snapshots;
    private final ArrayList<AuctionSniper> notToBeGCd = new ArrayList<AuctionSniper>();

    public SnipersTableModel() {
        snapshots = new ArrayList<SniperSnapshot>();
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return snapshots.size();
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        int row = rowMatching(newSnapshot);
        snapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }

    public void sniperAdded(AuctionSniper sniper) {
        addSniperSnapshot(sniper.getSnapshot());
        sniper.addSniperListener(new SwingThreadSniperListener(this));
    }

    private void addSniperSnapshot(SniperSnapshot newSniper) {
        snapshots.add(newSniper);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }

    private int rowMatching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (snapshot.isForSameItemAs(snapshots.get(i))) {
                return i;
            }
        }
        throw new Defect("Cannot find match for " + snapshot);
    }
}
