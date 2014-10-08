package com.sixdegrees;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Utility class for a webbrowser.
 */
public class BrowserUtils {

    public static WebDriver firefoxDriver() {
        int domMaxChromeScriptRunTime = 4500;
        int domMaxScriptRunTime = 3500;
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("dom.max_chrome_script_run_time", domMaxChromeScriptRunTime);
        firefoxProfile.setPreference("dom.max_script_run_time", domMaxScriptRunTime);
        capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
        WebDriver firefoxDriver = new FirefoxDriver(capabilities);
        firefoxDriver.manage().timeouts().implicitlyWait(10, SECONDS);
        firefoxDriver.manage().timeouts().pageLoadTimeout(10, SECONDS);
        firefoxDriver.manage().timeouts().setScriptTimeout(10, SECONDS);
        return firefoxDriver;
    }
}
