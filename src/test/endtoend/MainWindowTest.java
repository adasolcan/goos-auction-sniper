package test.endtoend;

import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import auctionsniper.ui.MainWindow;
import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {
    private final MainWindow mainWindow = new MainWindow(new SniperPortfolio());
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<String> buttonProbe =
                new ValueMatcherProbe<String>(equalTo("someItem"), "join request");

        mainWindow.addUserRequestListener(
                new UserRequestListener() {
                    public void joinAuction(String itemId) {
                        buttonProbe.setReceivedValue(itemId);
                    }
                }
        );

        driver.startBiddingFor("someItem");
        driver.check(buttonProbe);

    }
}
