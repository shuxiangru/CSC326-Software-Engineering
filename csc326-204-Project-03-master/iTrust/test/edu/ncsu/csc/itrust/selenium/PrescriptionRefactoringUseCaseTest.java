package edu.ncsu.csc.itrust.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import edu.ncsu.csc.itrust.enums.TransactionType;

public class PrescriptionRefactoringUseCaseTest extends iTrustSeleniumTest {
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.hcp4();

		driver = new Driver();

		driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

		driver.get(ADDRESS);
	}

	@Test
	public void testAcceptanceScenario1() throws Exception {
		driver = login("9000000004", "pw");

		// driver.findElement(By.linkText("HCP 1")).click();

		// System.out.println(driver.getTitle());

		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();

		userSearch(driver, "1", "Random", "Person");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		// System.out.println("Title: "+driver.getTitle());
		driver.findElement(By.id("update")).click();

		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000004L, 1L,
				"Office visit");

		new Select(driver.findElement(By.id("medID")))
				.selectByVisibleText("081096 - Aspirin");
		driver.findElement(By.id("dosage")).clear();
		driver.findElement(By.id("dosage")).sendKeys("200");
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys("02/02/2011");
		driver.findElement(By.id("endDate")).clear();
		driver.findElement(By.id("endDate")).sendKeys("02/09/2011");
		driver.findElement(By.id("instructions")).clear();
		driver.findElement(By.id("instructions")).sendKeys(
				"Take daily with water.");
		driver.findElement(By.id("addprescription")).click();

		assertTrue(driver.getPageSource().contains(
				"Prescription information successfully updated"));

		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000004L, 1L,
				"Office visit");

		// driver.findElement(By.linkText("Remove")).click();
		// driver.findElement(By.linkText("Logout")).click();Ss
	}

	@Test
	public void testAcceptanceScenario2() throws Exception {
		driver = login("9000000000", "pw");

		// driver.findElement(By.linkText("HCP 1")).click();

		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();

		userSearch(driver, "25", "Trend", "Setter");

		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.id("update")).click();

		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 25L, "");

		new Select(driver.findElement(By.id("medID")))
				.selectByVisibleText("664662530 - Penicillin");
		driver.findElement(By.id("dosage")).clear();
		driver.findElement(By.id("dosage")).sendKeys("60");
		driver.findElement(By.id("startDate")).clear();

		driver.findElement(By.id("startDate")).sendKeys("02/26/2015");
		driver.findElement(By.id("endDate")).clear();
		driver.findElement(By.id("endDate")).sendKeys("02/26/2028");

		driver.findElement(By.id("instructions")).clear();
		driver.findElement(By.id("instructions")).sendKeys("Take frequently");
		driver.findElement(By.id("addprescription")).click();

		assertNotLogged(TransactionType.PRESCRIPTION_ADD, 9000000000L, 2L, "");
		assertEquals("iTrust - Document Office Visit", driver.getTitle());
		assertTrue(driver.getPageSource().contains("Penicillin"));
	}

	/**
	 * Test adding a prescription, no allergy/interaction
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAcceptanceScenario3() throws Exception {
		driver = login("9000000000", "pw");

		// driver.findElement(By.linkText("HCP 1")).click();
		
		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();

		userSearch(driver, "25", "Trend", "Setter");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.id("update")).click();

		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 25L, "");

		new Select(driver.findElement(By.id("medID")))
				.selectByVisibleText("664662530 - Penicillin");
		driver.findElement(By.id("dosage")).clear();
		driver.findElement(By.id("dosage")).sendKeys("150");
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys("03/02/2015");
		driver.findElement(By.id("endDate")).clear();
		driver.findElement(By.id("endDate")).sendKeys("03/14/2015");
		driver.findElement(By.id("instructions")).clear();
		driver.findElement(By.id("instructions")).sendKeys(
				"Take very frequently");
		driver.findElement(By.id("addprescription")).click();
		// assertTrue(driver.getPageSource().contains("information successfully updated"));
		assertTrue(driver.getPageSource().contains("Penicillin"));
	}

	public void testAcceptanceScenario4() throws Exception {
		driver = login("9000000000", "pw");

		// driver.findElement(By.linkText("HCP 1")).click();

		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();

		userSearch(driver, "1", "Random", "Person");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.id("update")).click();
		new Select(driver.findElement(By.id("medID")))
				.selectByVisibleText("619580501 - Adefovir");
		driver.findElement(By.id("dosage")).clear();
		driver.findElement(By.id("dosage")).sendKeys("600");
		driver.findElement(By.id("startDate")).clear();
		driver.findElement(By.id("startDate")).sendKeys("02/26/2015");
		driver.findElement(By.id("endDate")).clear();
		driver.findElement(By.id("endDate")).sendKeys("02/26/2025");

		driver.findElement(By.name("addprescription")).submit();

		assertEquals("iTrust - Document Office Visit", driver.getTitle());
		assertTrue(driver.getPageSource().contains("Adefovir"));
	}

	@Test
	public void testAcceptanceScenario5() throws Exception {
		gen.hcp5();
		driver = login("9000000005", "pw");

		// driver.findElement(By.linkText("HCP 1")).click();

		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();

		userSearch(driver, "1", "Random", "Person");

		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.id("update")).click();

		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000005L, 1L, "");

		new Select(driver.findElement(By.id("medID")))
				.selectByVisibleText("678771191 - Ibuprofen");
		driver.findElement(By.id("dosage")).clear();
		driver.findElement(By.id("dosage")).sendKeys("1000");
		driver.findElement(By.id("startDate")).clear();

		driver.findElement(By.id("startDate")).sendKeys("02/26/2015");
		driver.findElement(By.id("endDate")).clear();
		driver.findElement(By.id("endDate")).sendKeys("02/26/2028");

		driver.findElement(By.id("instructions")).clear();
		driver.findElement(By.id("instructions")).sendKeys("Take frequently");
		driver.findElement(By.id("addprescription")).click();

		assertEquals("iTrust - Document Office Visit", driver.getTitle());
		assertTrue(driver.getPageSource().contains("Ibuprofen"));
	}

	public void testEditPrescriptionLogging() throws Exception {

		driver = login("9000000000", "pw");

		// driver.findElement(By.linkText("HCP 1")).click();

		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();

		userSearch(driver, "1", "Random", "Person");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.name("notes")).clear();
		driver.findElement(By.name("notes")).sendKeys(
				"Showing signs of dehydration");
		driver.findElement(By.id("update")).click();
		new Select(driver.findElement(By.name("ICDCode")))
				.selectByVisibleText("79.10 - Echovirus");
		driver.findElement(By.id("add_diagnosis")).click();
		// driver.findElement(By.linkText("Logout")).click();
	}

	public void testIllegalCharacters() throws Exception {
		driver = login("9000000000", "pw");

		// driver.findElement(By.linkText("HCP 1")).click();

		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();

		userSearch(driver, "1", "Random", "Person");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.name("notes")).clear();
		driver.findElement(By.name("notes")).sendKeys("#!#!#!&");

		driver.findElement(By.id("update")).click();
		assertTrue(driver.getPageSource().contains("Information not valid"));

	}

	@Test
	public void testPrescriptionNoInstructions() throws Exception {
		driver = login("9000000000", "pw");

		// driver.findElement(By.linkText("HCP 1")).click();

		driver.findElement(By.xpath("//h2[text()='Office Visits']")).click();
		driver.findElement(By.linkText("Document Office Visit")).click();

		userSearch(driver, "1", "Random", "Person");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		driver.findElement(By.name("notes")).clear();
		// driver.findElement(By.name("notes")).sendKeys(
		// "Showing signs of dehydration");
		driver.findElement(By.id("update")).click();
		new Select(driver.findElement(By.name("ICDCode")))
				.selectByVisibleText("79.10 - Echovirus");
		driver.findElement(By.id("add_diagnosis")).click();

		assertTrue(driver.getPageSource().contains("Instructions"));

	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}