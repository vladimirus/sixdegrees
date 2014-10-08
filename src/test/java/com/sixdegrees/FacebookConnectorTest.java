package com.sixdegrees;

import static com.sixdegrees.BrowserUtils.firefoxDriver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by vladimir.
 */
@RunWith(MockitoJUnitRunner.class)
public class FacebookConnectorTest {
    private FacebookConnector connector;

    @Before
    public void before() {
        this.connector = new FacebookConnector(firefoxDriver());
    }

    @Test
    public void connect() throws Exception {
        connector.login();
        connector.makeFriends();
        connector.quit();
    }
}
