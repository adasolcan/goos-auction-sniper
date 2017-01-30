package auctionsniper.ui;

import auctionsniper.SniperListener;

import javax.swing.*;

public class SniperStateDisplayer implements SniperListener {
    private final MainWindow ui;

    public SniperStateDisplayer(MainWindow ui) {
        this.ui = ui;
    }

    public void sniperBidding() {
        showStatus(MainWindow.STATUS_BINDING);
    }

    public void sniperLost() {
        showStatus(MainWindow.STATUS_LOST);
    }

    public void sniperWinning() {
        showStatus(MainWindow.STATUS_WINNING);
    }

    private void showStatus(final String status) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui.showStatus(status);
            }
        });
    }
}
