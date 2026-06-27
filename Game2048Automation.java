package automation;

// Selenium imports for browser automation
import org.openqa.selenium.*;               // Core Selenium classes (WebDriver, By, Keys)
import org.openqa.selenium.chrome.ChromeDriver; // Chrome browser driver
import io.github.bonigarcia.wdm.WebDriverManager; // Auto-downloads ChromeDriver
import java.util.Random;                    // For picking random directions

public class Game2048Automation {

    public static void main(String[] args) throws InterruptedException {
        /*
         * throws InterruptedException is needed because
         * we use Thread.sleep() to add delays in the code.
         * Without this, Java will show a compile error.
         */

        // ─────────────────────────────────────────────
        // STEP 1: SETUP CHROMEDRIVER
        // ─────────────────────────────────────────────
        /*
         * WebDriverManager automatically downloads and sets up
         * the correct version of ChromeDriver for your Chrome browser.
         * Without this, you would need to manually download ChromeDriver
         * and set the path yourself.
         */
        WebDriverManager.chromedriver().setup();

        // ─────────────────────────────────────────────
        // STEP 2: OPEN CHROME BROWSER
        // ─────────────────────────────────────────────
        /*
         * WebDriver is the main interface to control the browser.
         * new ChromeDriver() opens a new Chrome browser window.
         */
        WebDriver driver = new ChromeDriver();

        // ─────────────────────────────────────────────
        // STEP 3: OPEN THE 2048 GAME WEBSITE
        // ─────────────────────────────────────────────
        /*
         * driver.get() opens the given URL in the browser.
         * This is like typing the URL in the address bar.
         */
        driver.get("https://gabrielecirulli.github.io/2048");

        // ─────────────────────────────────────────────
        // STEP 4: WAIT FOR PAGE TO LOAD
        // ─────────────────────────────────────────────
        /*
         * Thread.sleep(2000) pauses the program for 2000 milliseconds (2 seconds).
         * This gives the browser enough time to fully load the game
         * before we start interacting with it.
         */
        Thread.sleep(2000);
        System.out.println("✅ Game Opened!");

        // ─────────────────────────────────────────────
        // STEP 5: DEFINE POSSIBLE MOVE DIRECTIONS
        // ─────────────────────────────────────────────
        /*
         * In 2048, you can move tiles in 4 directions using arrow keys.
         * We store these directions in an array to pick from randomly.
         */
        String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"};

        // ─────────────────────────────────────────────
        // STEP 6: CREATE RANDOM OBJECT
        // ─────────────────────────────────────────────
        /*
         * Random class is used to pick a random direction
         * from the directions array on each move.
         */
        Random random = new Random();

        // ─────────────────────────────────────────────
        // STEP 7: PLAY 200 MOVES IN A LOOP
        // ─────────────────────────────────────────────
        /*
         * We loop 200 times. Each loop = one move in the game.
         * You can increase or decrease this number as needed.
         */
        for (int i = 0; i < 200; i++) {

            // ── FIND THE BODY ELEMENT ──────────────────
            /*
             * We send arrow key presses to the <body> element of the page.
             * driver.findElement(By.tagName("body")) finds the body tag in the HTML.
             * The game listens to keyboard events on the body.
             */
            WebElement body = driver.findElement(By.tagName("body"));

            // ── PICK A RANDOM DIRECTION ────────────────
            /*
             * random.nextInt(4) returns a random number: 0, 1, 2, or 3.
             * We use this to pick a random direction from the array.
             * e.g. 0 = "UP", 1 = "DOWN", 2 = "LEFT", 3 = "RIGHT"
             */
            String direction = directions[random.nextInt(4)];

            // ── SEND ARROW KEY TO BROWSER ──────────────
            /*
             * body.sendKeys() simulates a keyboard key press in the browser.
             * Keys.ARROW_UP / DOWN / LEFT / RIGHT are Selenium's built-in
             * constants for arrow keys.
             * This moves the tiles in the chosen direction in the game.
             */
            switch (direction) {
                case "UP":    body.sendKeys(Keys.ARROW_UP);    break;
                case "DOWN":  body.sendKeys(Keys.ARROW_DOWN);  break;
                case "LEFT":  body.sendKeys(Keys.ARROW_LEFT);  break;
                case "RIGHT": body.sendKeys(Keys.ARROW_RIGHT); break;
            }

            // ── WAIT BETWEEN MOVES ─────────────────────
            /*
             * Thread.sleep(100) waits 100 milliseconds (0.1 second) between moves.
             * This prevents moves from happening too fast,
             * giving the game time to animate and update the board.
             */
            Thread.sleep(100);

            // ── PRINT SCORE EVERY 50 MOVES ─────────────
            /*
             * i % 50 == 0 means: every time i is a multiple of 50
             * (i.e. move 0, 50, 100, 150, 200).
             * We find the score element by its CSS class name "score-container"
             * and read its text. We split by "\n" because the element text
             * contains "SCORE\n1234" — we only want the number part (index 0).
             */
            if (i % 50 == 0) {
                String score = driver.findElement(By.className("score-container"))
                                     .getText().split("\n")[0];
                System.out.println("Move " + i + " | Score: " + score);
            }
        }

        // ─────────────────────────────────────────────
        // STEP 8: PRINT FINAL SCORE
        // ─────────────────────────────────────────────
        /*
         * After all 200 moves are done, we read and print the final score.
         * Same logic as above — find score-container and get its text.
         */
        String finalScore = driver.findElement(By.className("score-container"))
                                  .getText().split("\n")[0];
        System.out.println("✅ Done! Final Score: " + finalScore);

        // ─────────────────────────────────────────────
        // STEP 9: WAIT AND CLOSE BROWSER
        // ─────────────────────────────────────────────
        /*
         * Thread.sleep(3000) waits 3 seconds so you can see the final result
         * before the browser closes.
         * driver.quit() closes the browser and ends the WebDriver session.
         */
        Thread.sleep(3000);
        driver.quit();
        System.out.println("🔒 Browser closed.");
    }
}
