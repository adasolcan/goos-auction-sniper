package auctionsniper.ui;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Losing";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPERS_TABLE_NAME = "Auction table";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    private SnipersTableModel snipers = new SnipersTableModel();

    public MainWindow(SniperListener snipers) {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        fillContentPanel(makeSniperTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void fillContentPanel(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        snipers.sniperStateChanged(sniperSnapshot);
    }

    private JTable makeSniperTable() {
        snipers = new SnipersTableModel();
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }
}