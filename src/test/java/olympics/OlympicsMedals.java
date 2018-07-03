package olympics;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.google.common.collect.Ordering;
import io.github.bonigarcia.wdm.WebDriverManager;

public class OlympicsMedals {
	WebDriver driver;

	@BeforeMethod
	public void bMethod() {
		driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		System.out.println("--Test Starting--");
	}

	@AfterMethod
	public void aMethod() {
		System.out.println("--End of Test--");
		System.out.println();
	}

	@BeforeClass
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test(priority = 1)
	public void firstTest() {
		List<Integer> intList = elementsToInteger("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/td[1]");
		TreeSet<Integer> rTree = new TreeSet<Integer>(intList);
		System.out.println(rTree);
		//2
		Assert.assertEquals(intList, rTree);
		driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/thead/tr/th[2]")).click();
		List<String> cStrList = countriesString("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/th[1]");
		TreeSet<String> sTree = new TreeSet<String>(cStrList);
		System.out.println(sTree);
		//4
		Assert.assertEquals(cStrList, sTree);
		intList = elementsToInteger("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/td[1]");
		//5
		Assert.assertNotEquals(intList, rTree);
	}
	
	@Test(priority = 2)
	public void secondTest() {
		String mostCountry = "United States";
		System.out.println("Most Gold Medals: " + goldMost());
		System.out.println("Most Silver Medals: " + silverMost());
		System.out.println("Most Bronze Medals: " + bronzeMost());
		System.out.println("Most Medals: " + totalMost());
		Assert.assertEquals(goldMost(), mostCountry);
		Assert.assertEquals(silverMost(), mostCountry);
		Assert.assertEquals(bronzeMost(), mostCountry);
		Assert.assertEquals(totalMost(), mostCountry);
	}
	
	@Test(priority = 3)
	public void thirdTest() {
		System.out.println("Silver 18 Countries: ");
		System.out.println(silver18());
		Assert.assertTrue(silver18().contains(" China (CHN)") && silver18().contains(" France (FRA)"));
	}
	
	@Test(priority = 4)
	public void fourthTest() {
		String country = " Japan (JPN)";
		int idx[] = {4, 1};
		System.out.println("Index of" + country + " is: " + getIndex(country)[0] + " , " + getIndex(country)[1]);
		Assert.assertEquals(idx, getIndex(country));
	}
	
	@Test(priority = 5)
	public void fifthTest() {
		List<String> strList = sum18();
		for(int i = 0; i < strList.size(); i++){
			System.out.print(strList.get(i) + " - ");
			System.out.println(strList.get(i+1));
			i++;
		}
		Assert.assertTrue(sum18().contains(" Australia (AUS)") && sum18().contains(" Italy (ITA)"));
	}
	
	@AfterClass
	public void tearDown() {
		System.out.println("This is after class");
	}
	
	public List<String> countriesString(String xpath) {
		List<WebElement> cList = driver.findElements(By.xpath(xpath));
		cList.remove(0);
		cList.remove(10);
		cList.remove(10);
		List<String> strList = new ArrayList<String>();
		for(WebElement e : cList) strList.add(e.getText());
		return strList;
	}	
	
	public List<String> elementsToString(String xpath) {
		List<WebElement> rankList = driver.findElements(By.xpath(xpath));
		rankList.remove(10);
		List<String> strList = new ArrayList<String>();
		for(WebElement e : rankList) strList.add(e.getText());
		return strList;
	}	
	
	public List<Integer> elementsToInteger(String xpath) {
		List<String> strList = elementsToString(xpath);
		List<Integer> intList = new ArrayList<Integer>();
		for(String str : strList) intList.add(Integer.valueOf(str));
		return intList;
	}
	
	public String most(String first, String second) {
		String xpath = "//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/td[" + first + "]";
		List<Integer> medalList = elementsToInteger(xpath);
		while(!Ordering.natural().reverse().isOrdered(medalList)) {
			driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/thead/tr/th[" + second + "]")).click();
			medalList = elementsToInteger(xpath);
		}
		return driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/tbody/tr[1]/th/a")).getText();
	}
	
	public String goldMost() {
		return most("2", "3");
	}
	
	public String silverMost() {
		return most("3", "4");
	}
	
	public String bronzeMost() {
		return most("4", "5");
	}
	
	public String totalMost() {
		return most("5", "6");
	}
	
	public List<String> silver18() {
		String xpath = "//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/td[3]";
		List<Integer> medalList = elementsToInteger(xpath);
		List<String> cStrList = countriesString("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/th[1]");
		List<String> silver18List = new ArrayList<String>();
		for(int i = 0; i < medalList.size(); i++){
			if(medalList.get(i) == 18)	silver18List.add(cStrList.get(i));
		}
		return silver18List;		
	}
	
	public int[] getIndex(String s) {
		int[] indx = {0, 1};
		String xpath = "//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/td[1]";
		List<Integer> rankList = elementsToInteger(xpath);
		while(!Ordering.natural().reverse().isOrdered(rankList)) {
			driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[8]/thead/tr/th[1]")).click();
			rankList = elementsToInteger(xpath);
		}
		List<String> cStrList = countriesString("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/th[1]");
		indx[0] = cStrList.indexOf(s);
		return indx;
	}
	
	public List<String> sum18() {
		String xpath = "//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/td[4]";
		List<Integer> medalList = elementsToInteger(xpath);
		List<String> cStrList = countriesString("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tr/th[1]");
		List<String> sum18List = new ArrayList<String>();
		for(int i = 0; i < medalList.size(); i++){
			for(int j = 0 + i; j < medalList.size(); j++){
				if(medalList.get(i) + medalList.get(j) == 18) {
					if(medalList.get(i) == medalList.get(j)) continue;
					sum18List.add(cStrList.get(i));
					sum18List.add(cStrList.get(j));
				}
			}
		}
		return sum18List;	
	}
}