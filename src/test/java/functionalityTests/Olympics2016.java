package functionalityTests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import utilities.TestBase;

import java.util.ArrayList;
import java.util.List;

public class Olympics2016 extends TestBase {

    @Test(priority = 0)
    public void verifyMedalTableIsSortedByRank() {
        WebElement RankHeader = driver.findElement(By.xpath("//th[.='Rank']"));
        RankHeader.click();

        isTableSortedByRank(true);
    }


    @Test(priority = 1)
    public void verifyNOCSortsByCountryNames() {
        WebElement NOCHeader = driver.findElement(By.xpath("//th[.='NOC']"));
        NOCHeader.click();

        isTableSortedByCountryNames(true);
        isTableSortedByRank(false);
    }

    public String getCountryWithMost(String medalType) {
        int row = 1;
        switch (medalType) {
            case "Gold":
                row = 2;
                break;
            case "Silver":
                row = 3;
                break;
            case "Bronze":
                row = 4;
                break;
            case "Total":
                row = 5;
                break;
        }


        WebElement medalButton = driver.findElement(By.xpath("//th[.='" + medalType + "']"));
        medalButton.click();

        List<WebElement> liste = driver.findElements(By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//tr[position() <11])[position()>2]//td[" + row + "]"));

        if (isSortedNumbers(liste))
            medalButton.click();

        return driver.findElement(By.xpath("(//a[contains(text(),'Remaining NOCs')]//..//..//..//a)[2]")).getText();


    }

    @Test(priority = 2)
    public void getTheCuntryWithMostGold() {
        softAssert.assertEquals(getCountryWithMost("Silver"), "United States");
    }

    @Test(priority = 3)
    public void checkSilverCountryList(){
        String[][] liste = getTheSilverCountryList();

        for (String[] item : liste) {
            System.out.println(item[0] +" - "+ item[1]);
        }

    }

    public String[][] getTheSilverCountryList(){
        WebElement RankHeader = driver.findElement(By.xpath("//th[.='Rank']"));
        RankHeader.click();

        if(!driver.findElement(By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//tr[11]//td[1]")).getText().contains("86"))
            RankHeader.click();

        String[][] liste = new String[10][2];
        for(int i=0; i<10;i++){
            liste[i][1] =driver.findElement(By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//tr["+(i+1)+"]//td[3]")).getText();
            liste[i][0]=driver.findElement(By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//tr//th)["+(i+1)+"]//a")).getText();
        }
        return liste;
    }


    public String getCountryRank(String country) {
        clickRemainingNOC();
        List<WebElement> listCountries = driver.findElements(By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//tr//th[1]/a"));
        List<WebElement> listRanks = driver.findElements(By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//tr//th[1]/preceding-sibling::td"));
        for (int i=0;i<listCountries.size();i++){
            if(listCountries.get(i).getText().trim().equalsIgnoreCase(country)){
                return listRanks.get(i).getText()+" - "+ country;
            }
        }
        return "0";
    }

    @Test(priority = 4)
    public void checkGettingRankByCountryName(){
        softAssert.assertEquals("61 - Mexico",getCountryRank("Mexico"),"Verification of getting the rank by country name FAILED !");
    }


    @Test(priority = 5)
    public void checkBronzeMedalList(){
        List<String> list = getCountryListWithTotalBronzeMedalsOf(18);

        for (String countryMatch: list) {
            System.out.println(countryMatch);
        }

    }

    protected List<String> getCountryListWithTotalBronzeMedalsOf(int sum) {
        clickRemainingNOC();
        WebElement bronzeButton = driver.findElement(By.xpath("//th[.='Bronze']"));
        bronzeButton.click();
        List<WebElement> bronzeList = driver.findElements(By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//tr//td[4]"));
        List<WebElement> countryList = driver.findElements(By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//th//a"));

        int[] bronzeNumbers = new int[bronzeList.size()];
        String[] bronzeCountries = new String[countryList.size()];
        for (int i=0; i < bronzeList.size(); i++) {
            bronzeNumbers[i] = Integer.parseInt(bronzeList.get(i).getText());
            bronzeCountries[i] = countryList.get(i).getText();
        }



        List<String> countriesWithTotalCalculatedBronze = new ArrayList<>();

        for (int i = 0 ; i < bronzeNumbers.length-1; i++){
            for (int j=i+1; j < bronzeNumbers.length; j++){
                if(bronzeNumbers[i] + bronzeNumbers[j] == sum){
                    countriesWithTotalCalculatedBronze.add(bronzeCountries[i] + " - " +bronzeCountries[j]);
                }
            }
        }

        return countriesWithTotalCalculatedBronze;
    }

    private void clickRemainingNOC() {
        WebElement remainingNOCButton = driver.findElement(By.xpath("//a[contains(text(),'Remaining NOCs')]"));
        remainingNOCButton.click();
    }
}
