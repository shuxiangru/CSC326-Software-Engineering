package edu.ncsu.csc.itrust.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import edu.ncsu.csc.itrust.enums.TransactionType;

public class PrescriptionDateTest extends iTrustSeleniumTest {

	protected void setUp() throws Exception 
	{
		super.setUp();
		gen.clearAllTables();
		gen.hcp0();
		gen.ndCodes();
		gen.patient1();
		gen.patient2();
		gen.patient4();
	}
	
	/**ADDRESS*/
	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	
	/**
	 * testeditOVPPrescription
	 * @throws Exception
	 */
	public void testeditOVPPrescription() throws Exception {
		// Create a new instance of the html unit driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        WebDriver driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
      
        //Check to make sure this is the correct page
        assertEquals("iTrust - HCP Home", driver.getTitle());

		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
        driver.findElement(By.linkText("Document Office Visit")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());
        
        userSearch(driver, "2", "Andy", "Programmer");
        driver.findElement(By.linkText("06/10/2007")).click();
        assertEquals("iTrust - Document Office Visit", driver.getTitle());
        
        new Select(driver.findElement(By.id("medID"))).selectByVisibleText("009042407 - Tetracycline");
        driver.findElement(By.cssSelector("option[value=\"009042407\"]")).click();
        driver.findElement(By.id("dosage")).sendKeys("5");
        driver.findElement(By.id("startDate")).clear();
        driver.findElement(By.id("startDate")).sendKeys("10/12/2013");
        driver.findElement(By.id("endDate")).clear();
        driver.findElement(By.id("endDate")).sendKeys("10/01/2013");
        driver.findElement(By.id("instructions")).sendKeys("Take thrice daily");
        driver.findElement(By.id("addprescription")).submit();
        assertTrue(driver.getPageSource().contains("Information not valid"));
        //assertEquals("Information not valid", driver.findElement(By.xpath("//div[@id='iTrustContent']/div[4]/div/h2")).getText());
        
        assertEquals("The start date of the prescription must be before the end date.", driver.findElement(By.className("errorList")).getText());
        
        //quit driver
        driver.quit();
	}

}