package utilities;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestBase {

    protected WebDriver driver;

    protected SoftAssert softAssert;
    protected Faker faker = new Faker();

    private Random random = new Random();


    @BeforeClass
    public void setUpBeforeClass() {
        WebDriverManager.chromedriver().setup();
        softAssert = new SoftAssert();
    }


    @BeforeMethod
    public void setUpBeforeMethod() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
    }

    @AfterMethod
    public void tearDownAfterMethod() {
        driver.quit();
        softAssert.assertAll();
    }


    public void sleep() {
        sleep(1500);
    }

    public void sleep(long milisecond) {
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    protected boolean isSortedString(List<WebElement> rankListWE) {
        boolean isSorted = true;
        for (int i = 0; i < rankListWE.size() - 1; i++) {
            String above = rankListWE.get(i).getText().trim();
            String below = rankListWE.get(i + 1).getText().trim();
            if (above.compareTo(below) > 0) {
                isSorted = false;
                break;
            }
        }
        return isSorted;
    }

    protected boolean isSortedNumbers(List<WebElement> rankListWE) {
        boolean isSorted = true;
        for (int i = 0; i < rankListWE.size() - 1; i++) {
            int above = Integer.parseInt(rankListWE.get(i).getText().trim());
            int below = Integer.parseInt(rankListWE.get(i + 1).getText().trim());
            if (above > below) {
                isSorted = false;
                break;
            }
        }
        return isSorted;
    }


    protected void isTableSortedByRank(boolean Expected) {
        List<WebElement> rankListWE = driver.findElements(By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//tr//td[1])[position()<11]"));
        if (Expected) {
            softAssert.assertTrue(isSortedNumbers(rankListWE), "Rank Sort Verification FAILED, it is NOT sorted !!!");
        } else {
            softAssert.assertFalse(isSortedNumbers(rankListWE), "Rank Sort Verification FAILED, it IS sorted !!!");
        }
    }

    protected void isTableSortedByCountryNames(boolean Expected) {
        List<WebElement> countryList = driver.findElements(By.xpath("((//th[@scope='row']//a)[position()>5] )[position() < 11]"));
        if (Expected) {
            softAssert.assertTrue(isSortedString(countryList), "Country Sort Verification FAILED, it is NOT sorted !!!");
        } else {
            softAssert.assertFalse(isSortedString(countryList), "Country Sort Verification FAILED, it IS sorted !!!");
        }
    }
}