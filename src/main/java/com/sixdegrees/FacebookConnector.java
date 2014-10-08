package com.sixdegrees;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Keys.RETURN;

import com.google.common.base.Optional;
import com.sixdegrees.model.Friend;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collection;

/**
 * Class that connects to fb.
 */
public class FacebookConnector {
    private final WebDriver driver;
    private final String email;
    private final String password;

    public FacebookConnector(WebDriver driver) {
        this.driver = driver;
        this.email = System.getProperty("email");
        this.password = System.getProperty("password");
    }

    public void login() {
        driver.get("https://www.facebook.com/");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("pass")).sendKeys(RETURN);
        sleepUninterruptibly(2, SECONDS);
    }

    public void makeFriends() {
        driver.get("https://www.facebook.com/friends/requests/?fcref=ffb");
        print(collectFriends());
    }

    private Iterable<Friend> collectFriends() {
        Collection<Friend> friends = newArrayList();
        for (int i = 0; i < 10; i++) {
            friends.addAll(parseFriendsWebElement(driver.findElements(By.className("friendBrowserListUnit"))));
        }

        return friends;
    }

    private Collection<Friend> parseFriendsWebElement(Collection<WebElement> webFriends) {
        Collection<Friend> friends = newArrayList();
        for (WebElement friend : webFriends) {
            try {
                scrollTo(friend);
                Optional<Friend> added = befriend(friend, parseFriendWebElement(friend));
                if (added.isPresent()) {
                    friends.add(added.get());
                }

            } catch (NumberFormatException ignoreCompletely) {

            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }
        return friends;
    }

    private Optional<Friend> befriend(WebElement element, Friend friend) {
        Friend clicked = null;
        if (friend.getNoOfMutualFrieds() >= 15) {
            WebElement button = element.findElement(By.cssSelector(friend.getAddLink()));
            if ("Add friend".equalsIgnoreCase(button.getText())) {
                System.out.println("Befriending " + friend.getName());
                button.click();
                clicked = friend;
            }
        }
        return fromNullable(clicked);
    }

    private Friend parseFriendWebElement(WebElement friendElement) {
        String mutualText = friendElement.findElement(By.className("friendBrowserMarginTopTiny")).getText();
        mutualText = mutualText.replaceAll("\\D+", "");

        String name = friendElement.findElement(By.className("friendBrowserNameTitle")).getText();

        int noOfFriends = Integer.valueOf(mutualText);

        WebElement friendButtonElement = friendElement.findElement(By.tagName("button"));
        String friendButton = friendButtonElement.getAttribute("class").replace(" ", ".");

        return new Friend(name, noOfFriends, "button." + friendButton);
    }

    private void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        sleepUninterruptibly(1, SECONDS);
    }

    private void print(Iterable<Friend> friends) {
        System.out.println("************************");
        System.out.println("Number of friends: " + size(friends));
        for (Friend friend : friends) {
            System.out.println(friend);
        }
    }

    public void quit() {
        driver.quit();
    }
}
