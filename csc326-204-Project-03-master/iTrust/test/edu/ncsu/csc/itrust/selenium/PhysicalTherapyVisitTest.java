package edu.ncsu.csc.itrust.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import edu.ncsu.csc.itrust.action.OrthopedicVisitAction;
import edu.ncsu.csc.itrust.action.PhysicalTherapyVisitAction;
import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class PhysicalTherapyVisitTest extends iTrustSeleniumTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private PhysicalTherapyVisitAction ptva;

	protected void setUp() throws Exception {
		ptva = new PhysicalTherapyVisitAction(factory, (long)10, "1000");
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * test adding an initial record for a patient w/ no prior pregs
	 * 
	 * @throws Exception
	 */
	public void testAddSuccess() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('ptAppointmentDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("ptAppointmentDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement ptID = driver.findElement(By.name("physicalTherapistID"));
		ptID.sendKeys("9900000003");
		assertEquals(ptID.getAttribute("value"), "9900000003");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());
		
		List<PhysicalTherapyVisitBean> bl = ptva.getAllPhysicalTherapyVisits(1);
		assertEquals(bl.size(),1);
		PhysicalTherapyVisitBean PTVB = bl.get(0);
		assertFalse(PTVB.getAddedVisit());
		assertEquals(PTVB.getPhysicalTherapyVisitDateString(), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		assertEquals(PTVB.getPatientID(), 1);
		assertEquals(PTVB.getPhysicalTherapistID(), 9900000003l);

		driver.quit();

	}
	
	public void testAddFailure1() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		WebElement ptID = driver.findElement(By.name("physicalTherapistID"));
		ptID.sendKeys("1");
		assertEquals(ptID.getAttribute("value"), "1");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());
		
		WebElement body = driver.findElement(By.xpath("//body"));
		assertTrue(body.getText().contains("MID provided for Physical Therapist is not an hcp."));

		driver.quit();

	}
	
	public void testAddFailure2() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		WebElement ptID = driver.findElement(By.name("physicalTherapistID"));
		ptID.sendKeys("9000000000");
		assertEquals(ptID.getAttribute("value"), "9000000000");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());
		
		WebElement body = driver.findElement(By.xpath("//body"));
		assertTrue(body.getText().contains("MID provided for Physical Therapist is not a physical therapist."));

		driver.quit();

	}

	public void testAddFailure3() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		WebElement ptID = driver.findElement(By.name("physicalTherapistID"));
		ptID.sendKeys("9900000003");
		assertEquals(ptID.getAttribute("value"), "9900000003");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		WebElement body = driver.findElement(By.xpath("//body"));
		assertTrue(body.getText().contains("Physical Therapy Appointment Date field is required."));

		driver.quit();

	}public void testAddFailure4() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('ptAppointmentDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("ptAppointmentDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		WebElement body = driver.findElement(By.xpath("//body"));
		assertTrue(body.getText().contains("MID for Physical Therapist is required."));

		driver.quit();

	}
	
	public void testPTCannotAdd() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000003");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());
		
		assertEquals(driver.findElements(By.id("appointmentDate")).size(), 0);
		driver.quit();

	}
	
	public void testDocument() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('ptAppointmentDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("ptAppointmentDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement ptID = driver.findElement(By.name("physicalTherapistID"));
		ptID.sendKeys("9900000003");
		assertEquals(ptID.getAttribute("value"), "9900000003");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());
		
		System.out.println(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		driver.navigate().to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000003");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		driver.findElement(By.xpath("//h2[text()=\"Physical Therapy\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Physical Therapy Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Physical Therapy Office Visit", driver.getTitle());
		
		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document\"]")).click();
		assertEquals("iTrust - Document Physical Therapy Office Visit", driver.getTitle());
		
		driver.findElement(By.xpath("//input[@name=\"HouseWorkScore\"][@value=\"4\"]")).click();
		driver.findElement(By.xpath("//input[@name=\"WalkingRoomScore\"][@value=\"2\"]")).click();
		driver.findElement(By.xpath("//input[@name=\"StandingScore\"][@value=\"3\"]")).click();
		driver.findElement(By.name("CalfTowelExercise")).click();
		driver.findElement(By.name("GastrocStretchExcercise")).click();
		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Update Physical Therapy Visit\"]")).click();
		assertEquals("iTrust - Select Physical Therapy Office Visit", driver.getTitle());
		
		List<PhysicalTherapyVisitBean> bl = ptva.getAllPhysicalTherapyVisits(1);
		assertEquals(bl.size(),1);
		PhysicalTherapyVisitBean PTVB = bl.get(0);
		assertTrue(PTVB.getAddedVisit());
		assertEquals(PTVB.getPhysicalTherapyVisitDateString(), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		assertEquals(PTVB.getPatientID(), 1);
		assertEquals(PTVB.getPhysicalTherapistID(), 9900000003l);
		assertEquals(PTVB.getHouseWorkScore(), 4);
		assertEquals(PTVB.getWalkingRoomScore(), 2);
		assertEquals(PTVB.getStandingScore(), 3);
		assertTrue(PTVB.getCalfTowelExercise());
		assertTrue(PTVB.getGastrocStretchExcercise());
		assertEquals(PTVB.getWellnessScore(), 22.5f);

		driver.quit();

	}
	
	public void testView() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('ptAppointmentDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("ptAppointmentDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement ptID = driver.findElement(By.name("physicalTherapistID"));
		ptID.sendKeys("9900000003");
		assertEquals(ptID.getAttribute("value"), "9900000003");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());
		
		System.out.println(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		driver.navigate().to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000003");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		driver.findElement(By.xpath("//h2[text()=\"Physical Therapy\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Physical Therapy Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Physical Therapy Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document\"]")).click();
		assertEquals("iTrust - Document Physical Therapy Office Visit", driver.getTitle());
		
		driver.findElement(By.xpath("//input[@name=\"HouseWorkScore\"][@value=\"4\"]")).click();
		driver.findElement(By.xpath("//input[@name=\"WalkingRoomScore\"][@value=\"2\"]")).click();
		driver.findElement(By.xpath("//input[@name=\"StandingScore\"][@value=\"3\"]")).click();
		driver.findElement(By.name("CalfTowelExercise")).click();
		driver.findElement(By.name("GastrocStretchExcercise")).click();
		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Update Physical Therapy Visit\"]")).click();
		assertEquals("iTrust - Select Physical Therapy Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"View\"]")).click();
		assertEquals("iTrust - View Physical Therapy Office Visit", driver.getTitle());
		
		WebElement content = driver.findElement(By.id("iTrustContent"));
		assertTrue(content.getText().contains("Performing normal house work No difficulty Getting into or out of bath Unable Walking between rooms Moderate Squatting? Unable Lift an object from floor Unable Walking two blocks? Unable Getting up or down stairs Unable Standing for 1 hour Little bit Jumping Unable Running on uneven ground Unable"));
		assertTrue(content.getText().contains(String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900)));
		assertTrue(content.getText().contains("22.5"));
		assertTrue(content.getText().contains("Calf Towel Stretch Gastroc Stretch"));

		driver.quit();

	}
	
	public void testEdit() throws Exception {
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String baseUrl = "http://localhost:8080/iTrust/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.setJavascriptEnabled(true);
		driver.get(baseUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('ptAppointmentDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("ptAppointmentDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement ptID = driver.findElement(By.name("physicalTherapistID"));
		ptID.sendKeys("9900000003");
		assertEquals(ptID.getAttribute("value"), "9900000003");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());
		
		System.out.println(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		driver.navigate().to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000003");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		driver.findElement(By.xpath("//h2[text()=\"Physical Therapy\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Physical Therapy Visits')]")).click();
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());

		// driver.findElement(By.id("searchBox")).sendKeys("1");
		driver.findElement(By.id("searchBox")).sendKeys("Random Person");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Random".equals(
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
				if ("Person".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1\"]")).click();
		assertEquals("iTrust - Select Physical Therapy Office Visit", driver.getTitle());
		
		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document\"]")).click();
		assertEquals("iTrust - Document Physical Therapy Office Visit", driver.getTitle());
		
		driver.findElement(By.xpath("//input[@name=\"HouseWorkScore\"][@value=\"4\"]")).click();
		driver.findElement(By.xpath("//input[@name=\"WalkingRoomScore\"][@value=\"2\"]")).click();
		driver.findElement(By.xpath("//input[@name=\"StandingScore\"][@value=\"3\"]")).click();
		driver.findElement(By.name("CalfTowelExercise")).click();
		driver.findElement(By.name("GastrocStretchExcercise")).click();
		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Update Physical Therapy Visit\"]")).click();
		assertEquals("iTrust - Select Physical Therapy Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Edit\"]")).click();
		assertEquals("iTrust - Edit Physical Therapy Office Visit", driver.getTitle());
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('ptAppointmentDate');\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("ptAppointmentDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		driver.findElement(By.xpath("//input[@name=\"WalkingBlockScore\"][@value=\"1\"]")).click();
		driver.findElement(By.xpath("//input[@name=\"WalkingRoomScore\"][@value=\"4\"]")).click();
		driver.findElement(By.xpath("//input[@name=\"StandingScore\"][@value=\"0\"]")).click();
		driver.findElement(By.name("CalfTowelExercise")).click();
		driver.findElement(By.name("QuadSetExercise")).click();
		driver.findElement(By.name("HipAbductionExercise")).click();
		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Update Physical Therapy Visit\"]")).click();
		assertEquals("iTrust - Select Physical Therapy Office Visit", driver.getTitle());
		
		List<PhysicalTherapyVisitBean> bl = ptva.getAllPhysicalTherapyVisits(1);
		assertEquals(bl.size(),1);
		PhysicalTherapyVisitBean PTVB = bl.get(0);
		assertTrue(PTVB.getAddedVisit());
		assertEquals(PTVB.getPhysicalTherapyVisitDateString(), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		assertEquals(PTVB.getPatientID(), 1);
		assertEquals(PTVB.getPhysicalTherapistID(), 9900000003l);
		assertEquals(PTVB.getHouseWorkScore(), 4);
		assertEquals(PTVB.getWalkingRoomScore(), 4);
		assertEquals(PTVB.getStandingScore(), 0);
		assertEquals(PTVB.getWalkingBlockScore(), 1);
		assertFalse(PTVB.getCalfTowelExercise());
		assertTrue(PTVB.getGastrocStretchExcercise());
		assertTrue(PTVB.getQuadSetExercise());
		assertTrue(PTVB.getHipAbductionExercise());
		assertEquals(PTVB.getWellnessScore(), 22.5f);

		driver.quit();

	}
	
}