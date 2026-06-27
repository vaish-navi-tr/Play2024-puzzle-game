package game_automation;


import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Gamepuzzle {

	public static void main(String[] args) 
			throws InterruptedException  {
	 WebDriverManager.chromedriver().setup();
	 WebDriver driver = new ChromeDriver();
	 driver.get("https://gabrielecirulli.github.io/2048");
	 driver.manage().window().maximize();
	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
     wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".shrink-1.truncate")));
     Thread.sleep(2000);
     System.out.println("✅ Game Opened!");
     System.out.println("🌐 Current URL: " + driver.getCurrentUrl());

     driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
     Thread.sleep(1000);

     String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"};
     Random random = new Random();

     for (int i = 0; i < 1000; i++) {

         // Check game over
         String pageText = driver.findElement(By.tagName("body")).getText();
         if (pageText.contains("Game Over")) {
             String finalScore = getScore(driver, pageText);
             System.out.println("🎮 Game Over! at move " + i);
             System.out.println("🏆 Final Score = " + finalScore);
             break;
         }

         // Send random arrow key
         WebElement body = driver.findElement(By.tagName("body"));
         String direction = directions[random.nextInt(4)];

         switch (direction) {
             case "UP":    body.sendKeys(Keys.ARROW_UP);    break;
             case "DOWN":  body.sendKeys(Keys.ARROW_DOWN);  break;
             case "LEFT":  body.sendKeys(Keys.ARROW_LEFT);  break;
             case "RIGHT": body.sendKeys(Keys.ARROW_RIGHT); break;
         }

         Thread.sleep(150);
     }

     Thread.sleep(3000);
     driver.quit();
 }

 private static String getScore(WebDriver driver, String pageText) {
     try {
         // Method 1: Direct CSS selector from DevTools screenshot
         // Score is in <span class="shrink-1 truncate">8</span>
         WebElement scoreEl = driver.findElement(
             By.cssSelector("span.shrink-1.truncate")
         );
         String score = scoreEl.getText().trim();
         if (score != null && score.matches("\\d+")) {
             System.out.println("✅ Score found via CSS selector: " + score);
             return score;
         }

     } catch (Exception e1) {
         System.out.println("⚠️ Method 1 failed: " + e1.getMessage());
     }

     try {
         // Method 2: Parse "964 points scored" from page body text
         String[] lines = pageText.split("\\n");
         for (String line : lines) {
             line = line.trim();
             if (line.matches("\\d+ points scored.*")) {
                 System.out.println("✅ Score found via page text: " + line);
                 return line.split(" ")[0];
             }
         }
     } catch (Exception e2) {
         System.out.println("⚠️ Method 2 failed: " + e2.getMessage());
     }

     try {
         // Method 3: JavaScript fallback — scan text nodes
         JavascriptExecutor js = (JavascriptExecutor) driver;
         String score3 = (String) js.executeScript(
             "var all = document.querySelectorAll('*');" +
             "for (var i = 0; i < all.length; i++) {" +
             "    var nodes = all[i].childNodes;" +
             "    for (var j = 0; j < nodes.length; j++) {" +
             "        if (nodes[j].nodeType === 3) {" +
             "            var t = nodes[j].textContent.trim();" +
             "            if (/^\\d+ points scored/.test(t)) {" +
             "                return t.split(' ')[0];" +
             "            }" +
             "        }" +
             "    }" +
             "}" +
             "return null;"
         );
         if (score3 != null) {
             System.out.println("✅ Score found via JavaScript: " + score3);
             return score3;
         }

     } catch (Exception e3) {
         System.out.println("⚠️ Method 3 failed: " + e3.getMessage());
     }

     return "0";
 }
}