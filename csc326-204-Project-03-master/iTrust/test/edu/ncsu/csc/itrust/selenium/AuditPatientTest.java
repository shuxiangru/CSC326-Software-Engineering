package edu.ncsu.csc.itrust.selenium;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class AuditPatientTest extends iTrustSeleniumTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.patientDeactivate();
	}
	
	@Test
	public void testHCPDeactivatePatient() throws Exception {
		WebDriver driver = login("9000000000", "pw");
		
		driver.findElement(By.xpath("//h2[text()='Patient Info']")).click();
		driver.findElement(By.linkText("Audit Patients")).click();
		assertEquals(driver.getTitle(), "iTrust - Please Select a Patient");
		
		//searching for patient 2
		driver.findElement(By.id("searchBox")).sendKeys("2");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Andy".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[2]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Programmer".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@onclick=\"parent.location.href='getPatientID.jsp?UID_PATIENTID=2&forward=hcp/auditPage.jsp';\"]")).click();
		assertEquals(driver.getTitle(), "iTrust - Audit Page (UC62)");
		
		//typing out I understand
		driver.findElement(By.name("understand")).sendKeys("I UNDERSTAND");
		driver.findElement(By.name("understand")).submit();
		
		//asserting deletion
		assertEquals("Patient Successfully Deactivated", driver.findElement(By.className("iTrustMessage")).getText());
	}
	
	public void testHCPDeactivatePatientWrongConfirmation() throws Exception {
		WebDriver driver = login("9000000000", "pw");

		driver.findElement(By.xpath("//h2[text()='Patient Info']")).click();
		driver.findElement(By.linkText("Audit Patients")).click();
		assertEquals(driver.getTitle(), "iTrust - Please Select a Patient");
		
		//searching for patient 2
		driver.findElement(By.id("searchBox")).sendKeys("2");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Andy".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[2]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Programmer".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@onclick=\"parent.location.href='getPatientID.jsp?UID_PATIENTID=2&forward=hcp/auditPage.jsp';\"]")).click();
		assertEquals(driver.getTitle(), "iTrust - Audit Page (UC62)");
		
		//typing out I understand
		driver.findElement(By.name("understand")).sendKeys("iunderstand");
		driver.findElement(By.name("understand")).submit();
		
		//asserting deletion
		assertEquals("You must type \"I UNDERSTAND\" in the textbox.", driver.findElement(By.className("iTrustError")).getText());
	}
	
	@Ignore
	public void testHCPActivatePatient() throws Exception {
		WebDriver driver = login("9000000000", "pw");

		driver.findElement(By.xpath("//h2[text()='Patient Info']")).click();
		driver.findElement(By.linkText("Audit Patients")).click();
		assertEquals(driver.getTitle(), "iTrust - Please Select a Patient");
		
		//searching for patient 314159
		driver.findElement(By.id("allowDeactivated")).click();
		driver.findElement(By.id("searchBox")).sendKeys("314159");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Fake".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[2]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Baby".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@onclick=\"parent.location.href='getPatientID.jsp?UID_PATIENTID=314159&forward=hcp/auditPage.jsp';\"]")).click();
		assertEquals(driver.getTitle(), "iTrust - Audit Page (UC62)");
		
		//typing out I understand
		driver.findElement(By.name("understand")).sendKeys("I UNDERSTAND");
		driver.findElement(By.name("understand")).submit();
		
		//asserting activation
		assertEquals("Patient Successfully Activated", driver.findElement(By.className("iTrustMessage")).getText());
	}
		
	public void testHCPActivatePatientWrongConfirmation() throws Exception {
		WebDriver driver = login("9000000000", "pw");

		driver.findElement(By.xpath("//h2[text()='Patient Info']")).click();
		driver.findElement(By.linkText("Audit Patients")).click();
		assertEquals(driver.getTitle(), "iTrust - Please Select a Patient");
		
		//searching for patient 314159
		driver.findElement(By.id("allowDeactivated")).click();
		driver.findElement(By.id("searchBox")).sendKeys("314159");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Fake".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[2]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Baby".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@onclick=\"parent.location.href='getPatientID.jsp?UID_PATIENTID=314159&forward=hcp/auditPage.jsp';\"]")).click();
		assertEquals(driver.getTitle(), "iTrust - Audit Page (UC62)");
		
		//typing out I understand
		driver.findElement(By.name("understand")).sendKeys("iunderstand");
		driver.findElement(By.name("understand")).submit();
		
		//asserting activation
		assertEquals("You must type \"I UNDERSTAND\" in the textbox.", driver.findElement(By.className("iTrustError")).getText());
	}
}