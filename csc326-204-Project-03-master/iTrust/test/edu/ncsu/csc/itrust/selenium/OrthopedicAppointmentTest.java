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
import edu.ncsu.csc.itrust.beans.ApptRequestBean;
import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptRequestDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class OrthopedicAppointmentTest extends iTrustSeleniumTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private OrthopedicVisitAction ova;
	private ApptRequestDAO apptDAO; 

	protected void setUp() throws Exception {
		ova = new OrthopedicVisitAction(factory, (long) 10, "1000");
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		apptDAO = factory.getApptRequestDAO();
	}

	/**
	 * test adding an initial record for a patient w/ no prior pregs
	 * 
	 * @throws Exception
	 */
	public void testRequestOrthopedicSuccess() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000002", "pw");

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

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");

		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(2);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "False");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(2);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(6);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		ApptRequestBean apptb = apptDAO.getApptRequestsForPatient(1).get(0);
		assertNotNull(apptb);
		assertTrue(apptb.isPending());
		assertFalse(apptb.isAccepted());
		assertEquals(apptb.getRequestedAppt().getApptType(), "Orthopedic");
		assertEquals(apptb.getRequestedAppt().getHcp(), 9900000002l);
		assertEquals(apptb.getRequestedAppt().getDate().toString(), "2093-06-10 01:00:00.0");

		driver.quit();

	}

	public void testRequestPhysicalTherapySuccess() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000003", "pw");

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

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");

		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(2);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "False");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(2);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(7);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		ApptRequestBean apptb = apptDAO.getApptRequestsForPatient(1).get(0);
		assertNotNull(apptb);
		assertTrue(apptb.isPending());
		assertFalse(apptb.isAccepted());
		assertEquals(apptb.getRequestedAppt().getApptType(), "Physical Therapy");
		assertEquals(apptb.getRequestedAppt().getHcp(), 9900000003l);
		assertEquals(apptb.getRequestedAppt().getDate().toString(), "2093-06-10 01:00:00.0");

		driver.quit();

	}

	public void testRequestOrthopedicFailure() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("1", "pw");
		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(1);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(6);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("ERROR: HCP for Orthopedic appointment must have Orthopedic Specialty",
				driver.findElement(By.xpath("//span[@class='iTrustError']")).getText());

		assertEquals(apptDAO.getApptRequestsForPatient(1).size(), 0);

		driver.quit();

	}

	public void testRequestPhysicalTherapyFailure() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("1", "pw");
		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(1);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(7);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("ERROR: HCP for Physical Therapy appointment must be a Physical Therapist",
				driver.findElement(By.xpath("//span[@class='iTrustError']")).getText());

		assertEquals(apptDAO.getApptRequestsForPatient(1).size(), 0);

		driver.quit();

	}

	public void testRequestAccept() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000002", "pw");

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

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");

		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(2);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "False");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(2);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(6);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		driver.findElement(By.xpath("//a[text()='Approve']")).click();
		assertTrue(driver.findElement(By.id("iTrustContent")).getText()
				.contains("The appointment request you selected has been accepted and scheduled."));

		ApptRequestBean apptb = apptDAO.getApptRequestsForPatient(1).get(0);
		assertNotNull(apptb);
		assertFalse(apptb.isPending());
		assertTrue(apptb.isAccepted());
		assertEquals(apptb.getRequestedAppt().getApptType(), "Orthopedic");
		assertEquals(apptb.getRequestedAppt().getHcp(), 9900000002l);
		assertEquals(apptb.getRequestedAppt().getDate().toString(), "2093-06-10 01:00:00.0");

		driver.quit();

	}

	public void testRequestReject() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000002", "pw");

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

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");

		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(2);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "False");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(2);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(6);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		driver.findElement(By.xpath("//a[text()='Reject']")).click();
		assertTrue(driver.findElement(By.id("iTrustContent")).getText()
				.contains("The appointment request you selected has been rejected."));

		ApptRequestBean apptb = apptDAO.getApptRequestsForPatient(1).get(0);
		assertNotNull(apptb);
		assertFalse(apptb.isPending());
		assertFalse(apptb.isAccepted());
		assertEquals(apptb.getRequestedAppt().getApptType(), "Orthopedic");
		assertEquals(apptb.getRequestedAppt().getHcp(), 9900000002l);
		assertEquals(apptb.getRequestedAppt().getDate().toString(), "2093-06-10 01:00:00.0");

		driver.quit();

	}

	public void testPhysicalTherapyRequestAcceptSuccess() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000002", "pw");

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

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(3);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(7);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000003");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		driver.findElement(By.xpath("//a[text()='Approve']")).click();
		assertTrue(driver.findElement(By.id("iTrustContent")).getText()
				.contains("The appointment request you selected has been accepted and scheduled."));

		ApptRequestBean apptb = apptDAO.getApptRequestsForPatient(1).get(0);
		assertNotNull(apptb);
		assertFalse(apptb.isPending());
		assertTrue(apptb.isAccepted());
		assertEquals(apptb.getRequestedAppt().getApptType(), "Physical Therapy");
		assertEquals(apptb.getRequestedAppt().getHcp(), 9900000003l);
		assertEquals(apptb.getRequestedAppt().getDate().toString(), "2093-06-10 01:00:00.0");

		driver.quit();

	}

	public void testPhysicalTherapyRequestAcceptFailure() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000003", "pw");

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

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");

		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(2);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "False");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(2);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(7);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000003");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		driver.findElement(By.xpath("//a[text()='Approve']")).click();
		assertTrue(driver.findElement(By.id("iTrustContent")).getText()
				.contains("Physical Therapy appointments can only be accepted if there is an order for that patient"));

		ApptRequestBean apptb = apptDAO.getApptRequestsForPatient(1).get(0);
		assertNotNull(apptb);
		assertTrue(apptb.isPending());
		assertFalse(apptb.isAccepted());
		assertEquals(apptb.getRequestedAppt().getApptType(), "Physical Therapy");
		assertEquals(apptb.getRequestedAppt().getHcp(), 9900000003l);
		assertEquals(apptb.getRequestedAppt().getDate().toString(), "2093-06-10 01:00:00.0");

		driver.quit();

	}

	public void testRequestView() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000002", "pw");

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

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");

		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(2);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "False");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(2);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(6);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[1]")).getText(),
				"Solid Snake");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[2]")).getText(),
				"Orthopedic");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[3]")).getText(),
				"06/10/2093 01:00 AM");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[4]")).getText(),
				"Pending");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[5]")).getText(),
				"No Comment");

		driver.quit();

	}

	public void testRequestViewAccepted() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000002", "pw");

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

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");

		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(2);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "False");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(2);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(6);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		driver.findElement(By.xpath("//a[text()='Approve']")).click();
		assertTrue(driver.findElement(By.id("iTrustContent")).getText()
				.contains("The appointment request you selected has been accepted and scheduled."));

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[1]")).getText(),
				"Solid Snake");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[2]")).getText(),
				"Orthopedic");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[3]")).getText(),
				"06/10/2093 01:00 AM");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[4]")).getText(),
				"Accepted");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[5]")).getText(),
				"No Comment");

		driver.quit();

	}

	public void testRequestViewRejected() throws Exception {
		HtmlUnitDriver driver = (HtmlUnitDriver) login("9900000002", "pw");

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

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"),
				String.format("%02d/%02d/%04d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900));

		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");

		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");

		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(2);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "False");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Request an Appointment')]")).click();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());

		Select hcp = new Select(driver.findElement(By.name("lhcp")));
		hcp.selectByIndex(2);
		Select type = new Select(driver.findElement(By.name("apptType")));
		type.selectByIndex(6);
		driver.findElement(By.name("startDate")).sendKeys("06/10/2093");
		driver.findElement(By.name("request")).submit();
		assertEquals("iTrust - Request an Appointment", driver.getTitle());
		assertEquals("Your appointment request has been saved and is pending.",
				driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("9900000002");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		driver.findElement(By.xpath("//a[text()='Reject']")).click();
		assertTrue(driver.findElement(By.id("iTrustContent")).getText()
				.contains("The appointment request you selected has been rejected."));

		driver.navigate()
				.to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/") + 7) + "logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Appointments\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Appointment Requests')]")).click();
		assertEquals("iTrust - View My Appointment Requests", driver.getTitle());

		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[1]")).getText(),
				"Solid Snake");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[2]")).getText(),
				"Orthopedic");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[3]")).getText(),
				"06/10/2093 01:00 AM");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[4]")).getText(),
				"Rejected");
		assertEquals(driver.findElement(By.xpath("//*[@id='iTrustContent']/div/table/tbody/tr[2]/td[5]")).getText(),
				"No Comment");

		driver.quit();

	}

}