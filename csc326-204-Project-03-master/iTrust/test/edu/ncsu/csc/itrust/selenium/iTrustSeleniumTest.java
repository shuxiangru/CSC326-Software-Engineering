package edu.ncsu.csc.itrust.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 * There's nothing special about this class other than adding a few handy test utility methods and
 * variables. When extending this class, be sure to invoke super.setUp() first.
 */
abstract public class iTrustSeleniumTest extends TestCase {
	/*
	 * The URL for iTrust, change as needed
	 */
	/**ADDRESS*/
	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	/**gen*/
	protected TestDataGenerator gen = new TestDataGenerator();
	
	/** Default timeout for Selenium webdriver */
	public static final int DEFAULT_TIMEOUT = 2;
	
	/**
	 * Name of the value attribute of html tags.
	 * Used for getting the value from a form input
	 * with .getAttribute("value"). Was previously
	 * .getAttribute(VALUE) before being removed by s
	 * selenium.
	 */
	public static final String VALUE = "value";

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}

	/**
	 * Helper method for logging in to iTrust
	 * 
	 * Also creates an explicit WebDriverWait for optional use.
	 * 
	 * @param username username
	 * @param password password
	 * @return {@link WebConversation}
	 * @throws Exception
	 */
	public WebDriver login(String username, String password) throws Exception {
			// begin at the iTrust home page
			HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
			String baseUrl = "http://localhost:8080/iTrust/";
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.setJavascriptEnabled(true);
			driver.get(baseUrl);
			driver.findElement(By.id("j_username")).clear();
			driver.findElement(By.id("j_username")).sendKeys(username);
			driver.findElement(By.id("j_password")).clear();
			driver.findElement(By.id("j_password")).sendKeys(password);
			driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			if (driver.getTitle().equals("iTrust - Login")) {
				throw new IllegalArgumentException("Error logging in, user not in database?");
			}
			
			return driver;
	}
	
	public void userSearch(WebDriver driver, String MID, String firstName, String lastName) throws InterruptedException{
		driver.findElement(By.id("searchBox")).clear();
		driver.findElement(By.id("searchBox")).sendKeys(MID);
		boolean thing = false;
		for (int second = 0;; second++) {
			if (second >= 60){
				if(thing)
					fail("timeout");
				thing = true;
				second = 0;
				driver.findElement(By.id("searchBox")).sendKeys(Keys.RETURN);
			}
			try {
				if (firstName.equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[2]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		thing = false;
		for (int second = 0;; second++) {
			if (second >= 60){
				if(thing)
					fail("timeout");
				thing = true;
				second = 0;
				driver.findElement(By.id("searchBox")).sendKeys(Keys.RETURN);
			}
			try {
				if (lastName.equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\""+MID+"\"]")).click();
	}
	
	/**
	 * assertLogged
	 * @param code code
	 * @param loggedInMID loggedInMID
	 * @param secondaryMID secondaryMID
	 * @param addedInfo addedInfo
	 * @throws DBException
	 */
	public static void assertLogged(TransactionType code, long loggedInMID,
			long secondaryMID, String addedInfo)
			throws DBException, InterruptedException {
		
		
		// Selenium on jenkins sometimes has issues finding a log the first time.
		// The proper solution would be to add explicit waits, but it is easier 
		// to wait a second and try again.
		int i = 0;
		while (i < 3) {
			List<TransactionBean> transList = TestDAOFactory.getTestInstance().getTransactionDAO().getAllTransactions();
			for (TransactionBean t : transList) {	
				if( (t.getTransactionType() == code) &&
					(t.getLoggedInMID() == loggedInMID) &&
					(t.getSecondaryMID() == secondaryMID))
					{
						assertTrue(t.getTransactionType() == code);
						if(!t.getAddedInfo().trim().contains(addedInfo.trim()))
						{
							fail("Additional Information is not logged correctly.");
						}
						return;
					}
			}
			
			i++;
			Thread.sleep(1000);
		}
		
		
		fail("Event not logged as specified.");
	}
	
	/**
	 * assertLogged
	 * @param code code
	 * @param loggedInMID loggedInMID
	 * @param secondaryMID secondaryMID not used
	 * @param addedInfo addedInfo
	 * @throws DBException
	 */
	public static void assertLoggedNoSecondary(TransactionType code, long loggedInMID,
			long secondaryMID, String addedInfo)
			throws DBException {
		List<TransactionBean> transList = TestDAOFactory.getTestInstance().getTransactionDAO().getAllTransactions();
		for (TransactionBean t : transList)
		{	
			if( (t.getTransactionType() == code) &&
				(t.getLoggedInMID() == loggedInMID))
				{
					assertTrue(t.getTransactionType() == code);
					if(!t.getAddedInfo().trim().contains(addedInfo.trim()))
					{
						fail("Additional Information is not logged correctly.");
					}
					return;
				}
		}
		fail("Event not logged as specified.");
	}


	/**
	 * assertNotLogged
	 * @param code code
	 * @param loggedInMID loggedInMID
	 * @param secondaryMID secondaryMID
	 * @param addedInfo addedInfo
	 * @throws DBException
	 */
	public static void assertNotLogged(TransactionType code, long loggedInMID,
			long secondaryMID, String addedInfo)
			throws DBException {
		List<TransactionBean> transList = TestDAOFactory.getTestInstance().getTransactionDAO().getAllTransactions();
		for (TransactionBean t : transList)
		{	
			if( (t.getTransactionType() == code) &&
				(t.getLoggedInMID() == loggedInMID) &&
				(t.getSecondaryMID() == secondaryMID) &&
				(t.getAddedInfo().trim().contains(addedInfo)) )
				{
					fail("Event was logged, but should NOT have been logged");
					return;
				}
		}
	}
	
}
