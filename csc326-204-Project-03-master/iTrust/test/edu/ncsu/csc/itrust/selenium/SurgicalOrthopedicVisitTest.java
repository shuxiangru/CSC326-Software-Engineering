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
import edu.ncsu.csc.itrust.action.SurgicalOrthopedicVisitAction;
import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;
import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class SurgicalOrthopedicVisitTest extends iTrustSeleniumTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private SurgicalOrthopedicVisitAction sova;

	protected void setUp() throws Exception {
		sova = new SurgicalOrthopedicVisitAction(factory, (long) 10, "1000");
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

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('soAppointmentDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("soAppointmentDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement ptID = driver.findElement(By.name("orthopedicID"));
		ptID.sendKeys("9900000002");
		assertEquals(ptID.getAttribute("value"), "9900000002");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		List<SurgicalOrthopedicVisitBean> bl = sova.getAllSurgicalOrthopedicVisits(1);
		assertEquals(bl.size(), 1);
		SurgicalOrthopedicVisitBean SOVB = bl.get(0);
		assertFalse(SOVB.getAddedVisit());
		assertEquals(SOVB.getSurgicalOrthopedicVisitDateString(),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));
		assertEquals(SOVB.getPatientID(), 1);
		assertEquals(SOVB.getOrthopedicID(), 9900000002l);

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

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement ptID = driver.findElement(By.name("orthopedicID"));
		ptID.sendKeys("1");
		assertEquals(ptID.getAttribute("value"), "1");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		WebElement body = driver.findElement(By.xpath("//body"));
		assertTrue(body.getText().contains("MID provided for Surgical Orthopedic is not an hcp."));

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

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement ptID = driver.findElement(By.name("orthopedicID"));
		ptID.sendKeys("9000000000");
		assertEquals(ptID.getAttribute("value"), "9000000000");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		WebElement body = driver.findElement(By.xpath("//body"));
		assertTrue(body.getText().contains("MID provided for Surgical Orthopedic is not an orthopedic MID."));

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

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement ptID = driver.findElement(By.name("orthopedicID"));
		ptID.sendKeys("9900000002");
		assertEquals(ptID.getAttribute("value"), "9900000002");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		WebElement body = driver.findElement(By.xpath("//body"));
		assertTrue(body.getText().contains("Orthopedic Appointment Date field is required if Surgical Orthopedic Visit date is selected."));

		driver.quit();

	}

	public void testAddFailure4() throws Exception {
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

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('soAppointmentDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("soAppointmentDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());

		WebElement body = driver.findElement(By.xpath("//body"));
		assertTrue(body.getText().contains("MID for Orthopedic is required if Surgical Orthopedic Visit Date is selected."));

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

		assertEquals(driver.findElements(By.id("soAppointmentDate")).size(), 0);
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

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('soAppointmentDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("soAppointmentDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement ptID = driver.findElement(By.name("orthopedicID"));
		ptID.sendKeys("9900000002");
		assertEquals(ptID.getAttribute("value"), "9900000002");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Surgical Orthopedic Visits')]")).click();assertEquals("iTrust - Select Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document\"]")).click();
		assertEquals("iTrust - Document Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.name("TotalKneeReplacement")).click();
		driver.findElement(By.name("SpineSurgery")).click();
		driver.findElement(By.name("RotatorCuffRepair")).click();

		driver.findElement(By.name("SurgeryNotes")).sendKeys("IDOLMASTER PLATINUM STARS 28 JULY 2016");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Update Surgical Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Surgical Orthopedic Office Visit", driver.getTitle());

		List<SurgicalOrthopedicVisitBean> bl = sova.getAllSurgicalOrthopedicVisits(1);
		assertEquals(bl.size(), 1);
		SurgicalOrthopedicVisitBean SOVB = bl.get(0);
		assertTrue(SOVB.getAddedVisit());
		assertEquals(SOVB.getSurgicalOrthopedicVisitDateString(),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));
		assertEquals(SOVB.getPatientID(), 1);
		assertEquals(SOVB.getOrthopedicID(), 9900000002l);
		assertTrue(SOVB.getSurgery(0));
		assertTrue(SOVB.getSurgery(4));
		assertTrue(SOVB.getSurgery(6));
		assertEquals(SOVB.getSurgicalNotes(), "IDOLMASTER PLATINUM STARS 28 JULY 2016");

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

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('soAppointmentDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("soAppointmentDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement ptID = driver.findElement(By.name("orthopedicID"));
		ptID.sendKeys("9900000002");
		assertEquals(ptID.getAttribute("value"), "9900000002");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		System.out.println(
				driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Surgical Orthopedic Visits')]")).click();
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
		assertEquals("iTrust - Select Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document\"]")).click();
		assertEquals("iTrust - Document Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.name("TotalKneeReplacement")).click();
		driver.findElement(By.name("SpineSurgery")).click();
		driver.findElement(By.name("RotatorCuffRepair")).click();

		driver.findElement(By.name("SurgeryNotes")).sendKeys("IDOLMASTER PLATINUM STARS 28 JULY 2016");
		
		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Update Surgical Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"View\"]")).click();
		assertEquals("iTrust - View Surgical Orthopedic Office Visit", driver.getTitle());

		WebElement content = driver.findElement(By.id("iTrustContent"));
		System.out.println(content.getText());
		assertTrue(content.getText().contains(
				"Surgeries\nTotal Knee Replacement Spine Surgery Rotator Cuff Repair"));
		assertTrue(content.getText()
				.contains(String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900)));
		assertTrue(content.getText().contains("Surgery Notes\nIDOLMASTER PLATINUM STARS 28 JULY 2016"));

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

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('orthopedicVisitDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('soAppointmentDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("soAppointmentDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement ptID = driver.findElement(By.name("orthopedicID"));
		ptID.sendKeys("9900000002");
		assertEquals(ptID.getAttribute("value"), "9900000002");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Surgical Orthopedic Visits')]")).click();
		assertEquals("iTrust - Select Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document\"]")).click();
		assertEquals("iTrust - Document Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.name("TotalKneeReplacement")).click();
		driver.findElement(By.name("SpineSurgery")).click();
		driver.findElement(By.name("RotatorCuffRepair")).click();

		driver.findElement(By.name("SurgeryNotes")).sendKeys("IDOLMASTER PLATINUM STARS 28 JULY 2016");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Update Surgical Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Edit\"]")).click();
		assertEquals("iTrust - Edit Surgical Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By
				.xpath("//input[@type=\"button\"][@value=\"Select Date\"][@onclick=\"displayDatePicker('soAppointmentDate');\"]"))
				.click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		assertEquals(driver.findElement(By.name("soAppointmentDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		driver.findElement(By.name("TotalKneeReplacement")).click();
		driver.findElement(By.name("TotalJointReplacement")).click();

		driver.findElement(By.name("SurgeryNotes")).sendKeys("\nI HOPE LEON CAN BE PRODUCED THIS TIME");

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Update Surgical Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Surgical Orthopedic Office Visit", driver.getTitle());

		List<SurgicalOrthopedicVisitBean> bl = sova.getAllSurgicalOrthopedicVisits(1);
		assertEquals(bl.size(), 1);
		SurgicalOrthopedicVisitBean SOVB = bl.get(0);
		assertTrue(SOVB.getAddedVisit());
		assertEquals(SOVB.getSurgicalOrthopedicVisitDateString(),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));
		assertEquals(SOVB.getPatientID(), 1);
		assertEquals(SOVB.getOrthopedicID(), 9900000002l);
		assertFalse(SOVB.getSurgery(0));
		assertTrue(SOVB.getSurgery(1));
		assertTrue(SOVB.getSurgery(4));
		assertTrue(SOVB.getSurgery(6));
		assertEquals(SOVB.getSurgicalNotes(),
				"IDOLMASTER PLATINUM STARS 28 JULY 2016"
						+ (SOVB.getSurgicalNotes().charAt(38) == 13 ? Character.toString((char) 13) : "")
						+ "\nI HOPE LEON CAN BE PRODUCED THIS TIME");

		driver.quit();

	}

}