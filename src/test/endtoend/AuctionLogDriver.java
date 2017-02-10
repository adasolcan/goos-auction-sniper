package test.endtoend;

import org.hamcrest.Matcher;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

import static auctionsniper.xmpp.XMPPAuctionHouse.LOG_FILE_NAME;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertThat;

public class AuctionLogDriver {
    private final File logFile = new File(LOG_FILE_NAME);

    public void hasEntry(Matcher<String> matcher) throws IOException {
        assertThat(readFileToString(logFile), matcher);
    }

    public void clearLog() {
        logFile.delete();
        LogManager.getLogManager().reset();
    }
}