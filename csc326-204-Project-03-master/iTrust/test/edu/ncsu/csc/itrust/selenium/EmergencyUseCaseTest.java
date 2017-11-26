package edu.ncsu.csc.itrust.selenium;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.Test;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class EmergencyUseCaseTest extends iTrustSeleniumTest {
	  private String baseUrl;
	  private StringBuffer verificationErrors = new StringBuffer();

	  @Override
	  public void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	  }

	  @Test
	  public void testERViewEmergencyReport1() throws Exception {
		//set up for the start location and driver
		WebDriver driver = login("9000000006", "pw");
	    try {
	      //check that the login correctly as ER
	      assertEquals("iTrust - ER Home", driver.getTitle());
	    } catch (Error e) {
	      verificationErrors.append(e.toString());
	      fail();
	    }
	    assertLogged(TransactionType.HOME_VIEW, 9000000006L, 0L, "");
	    driver.findElement(By.cssSelector("div.panel-heading")).click();
	    driver.findElement(By.linkText("Emergency Patient Report")).click();
	    //check that it goes to correct page
	    assertEquals("iTrust - Please Select a Patient", driver.getTitle());
	    //does search
	    userSearch(driver, "2", "Andy", "Programmer");
	    //check page content 
	    assertEquals("iTrust - ER Report", driver.getTitle());
	    assertEquals("Blood Type: O-", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[6]")).getText());
	    assertEquals("Pollen 06/05/2007", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[5]/ul/li")).getText());
	    assertEquals("Penicillin 06/04/2007", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[5]/ul/li[2]")).getText());
	    assertEquals("647641512 Prioglitazone", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[8]/ul/li")).getText());
	    assertEquals("no immunizations on record", driver.findElement(By.cssSelector("strong")).getText());
	    assertLogged(TransactionType.EMERGENCY_REPORT_VIEW, 9000000006L, 2L, "");
	  }
	  
	  @Test
	  public void testHCPViewEmergencyReport1() throws Exception {
		WebDriver driver = login("9000000000", "pw");
	    assertEquals("iTrust - HCP Home", driver.getTitle());
	    assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
	    driver.findElement(By.cssSelector("h2.panel-title")).click();
	    driver.findElement(By.linkText("Emergency Patient Report")).click();
	    assertEquals("iTrust - Please Select a Patient", driver.getTitle());
	    userSearch(driver, "2", "Andy", "Programmer");
	    assertEquals("iTrust - ER Report", driver.getTitle());
	    assertEquals("Blood Type: O-", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[6]")).getText());
	    assertEquals("Pollen 06/05/2007", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[5]/ul/li")).getText());
	    assertEquals("Penicillin 06/04/2007", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[5]/ul/li[2]")).getText());
	    assertEquals("647641512 Prioglitazone", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[8]/ul/li")).getText());
	    assertEquals("no immunizations on record", driver.findElement(By.cssSelector("strong")).getText());
	    assertLogged(TransactionType.EMERGENCY_REPORT_VIEW, 9000000000L, 2L, "");
	  }
	  
	  @Test
	  public void testHCPViewEmergencyReport2() throws Exception {
		WebDriver driver = login("9000000000", "pw");
	    assertEquals("iTrust - HCP Home", driver.getTitle());
	    assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
	    driver.findElement(By.cssSelector("h2.panel-title")).click();
	    driver.findElement(By.linkText("Emergency Patient Report")).click();
	    assertEquals("iTrust - Please Select a Patient", driver.getTitle());
	    userSearch(driver, "1", "Random", "Person");
	    assertEquals("iTrust - ER Report", driver.getTitle());
	    assertEquals("Blood Type: AB+", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[6]")).getText());
	    assertEquals("No allergies on record", driver.findElement(By.cssSelector("strong")).getText());
	    assertEquals("No current prescriptions on record", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[8]/strong")).getText());
	    assertEquals("no immunizations on record", driver.findElement(By.xpath("//div[@id='iTrustContent']/ul/li[9]/strong")).getText());
	    assertLogged(TransactionType.EMERGENCY_REPORT_VIEW, 9000000000L, 1L, "");
	  }

}