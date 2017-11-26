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
import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class PatientViewTest extends iTrustSeleniumTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private OrthopedicVisitAction ova;

	protected void setUp() throws Exception {
		ova = new OrthopedicVisitAction(factory, (long)10, "1000");
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testOrthopedicView() throws Exception {
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
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");
		
		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(1);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "True");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());
		
		System.out.println(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		driver.navigate().to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"View\"]")).click();
		assertEquals("iTrust - View Orthopedic Office Visit", driver.getTitle());
		
		WebElement content = driver.findElement(By.id("iTrustContent"));
		assertTrue(content.getText().contains(String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900)));
		assertTrue(content.getText().contains("CAKE"));
		assertFalse(content.findElements(By.xpath("//h4[text()=\"X-Ray Image\"]")).size() == 0);
		assertFalse(content.findElements(By.xpath("//img")).size() == 0);
		assertFalse(content.findElements(By.xpath("//h4[text()=\"MRI Report\"]")).size() == 0);
		assertTrue(content.getText().contains("CHOCOLATE"));
		assertFalse(content.findElements(By.xpath("//h4[text()=\"Anterior cruciate ligament injury\"]")).size() == 0);
		assertTrue(content.getText().contains("True"));
		
		driver.quit();

	}
	

	public void testOrthopedicViewDependant() throws Exception {
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
		driver.findElement(By.id("searchBox")).sendKeys("Chocolate Cake");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Chocolate".equals(
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
				if ("Cake".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1337\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Document Visit\"]")).click();
		assertEquals("iTrust - Add Orthopedic Office Visit", driver.getTitle());
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"Select Date\"]")).click();
		driver.findElement(By.xpath("//td[@class=\"dpDayHighlightTD\"]")).click();
		Date date = new Date();
		assertEquals(driver.findElement(By.name("orthopedicVisitDate")).getAttribute("value"), String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900));
		
		WebElement limbOrJoint = driver.findElement(By.name("limb&joint"));
		limbOrJoint.sendKeys("CAKE");
		assertEquals(limbOrJoint.getAttribute("value"), "CAKE");
		
		WebElement mriReport = driver.findElement(By.name("MRIReport"));
		mriReport.sendKeys("CHOCOLATE");
		assertEquals(mriReport.getText(), "CHOCOLATE");
		
		WebElement acli = driver.findElement(By.name("acli"));
		Select acliSelect = new Select(acli);
		acliSelect.selectByIndex(1);
		assertEquals(acliSelect.getFirstSelectedOption().getText(), "True");

		URL location = getClass().getResource("/Gengar.png");
		WebElement xrayImage = driver.findElement(By.name("XRAYImage"));
		xrayImage.sendKeys(location.toString());
		assertEquals(xrayImage.getAttribute("value"), location.toString());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"Add Orthopedic Visit\"]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());
		
		System.out.println(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		driver.navigate().to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.xpath("//h2[text()=\"Orthopedic\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Orthopedic Visits')]")).click();
		assertEquals("iTrust - Select Orthopedic Office Visit", driver.getTitle());

		driver.findElement(By.xpath("//input[@type=\"submit\"][@value=\"View\"]")).click();
		assertEquals("iTrust - View Orthopedic Office Visit", driver.getTitle());
		
		WebElement content = driver.findElement(By.id("iTrustContent"));
		assertTrue(content.getText().contains(String.format("%02d/%02d/%04d", date.getMonth()+1, date.getDate(), date.getYear()+1900)));
		assertTrue(content.getText().contains("CAKE"));
		assertFalse(content.findElements(By.xpath("//h4[text()=\"X-Ray Image\"]")).size() == 0);
		assertFalse(content.findElements(By.xpath("//img")).size() == 0);
		assertFalse(content.findElements(By.xpath("//h4[text()=\"MRI Report\"]")).size() == 0);
		assertTrue(content.getText().contains("CHOCOLATE"));
		assertFalse(content.findElements(By.xpath("//h4[text()=\"Anterior cruciate ligament injury\"]")).size() == 0);
		assertTrue(content.getText().contains("True"));
		
		driver.quit();

	}

	public void testPhysicalTherapyView() throws Exception {
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
		
		System.out.println(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		driver.navigate().to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		driver.findElement(By.xpath("//h2[text()=\"Physical Therapy\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Physical Therapy Visits')]")).click();
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

	public void testPhysicalTherapyViewDependant() throws Exception {
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
		driver.findElement(By.id("searchBox")).sendKeys("Chocolate Cake");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Chocolate".equals(
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
				if ("Cake".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1337\"]")).click();
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
		driver.findElement(By.id("searchBox")).sendKeys("Chocolate Cake");
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Chocolate".equals(
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
				if ("Cake".equals(
						driver.findElement(By.xpath("//div[@id='searchTarget']/table/tbody/tr[2]/td[3]")).getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@type=\"button\"][@value=\"1337\"]")).click();
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
		
		System.out.println(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		driver.navigate().to(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().indexOf("iTrust/")+7)+"logout.jsp");
		assertEquals("iTrust - Login", driver.getTitle());

		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("1");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		driver.findElement(By.xpath("//h2[text()=\"Physical Therapy\"]")).click();
		driver.findElement(By.xpath("//a[contains(text(), 'Physical Therapy Visits')]")).click();
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

}