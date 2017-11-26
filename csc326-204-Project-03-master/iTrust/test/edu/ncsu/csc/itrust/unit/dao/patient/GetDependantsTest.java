package edu.ncsu.csc.itrust.unit.dao.patient;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class GetDependantsTest extends TestCase {
	PatientDAO patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.patient1();
		gen.patient1337();
	}

	public void testGetPatient1() throws Exception {
		assertTrue(patientDAO.checkPatientExists(1337));
	}

	public void testGetPatient1337() throws Exception {
		assertTrue(patientDAO.checkPatientExists(1337));
	}

	public void testNotPatient200() throws Exception {
		assertFalse(patientDAO.checkPatientExists(200));
	}

	public void testGetAllPatients() throws Exception {
		assertEquals(2, patientDAO.getAllPatients().size());
	}

	public void testGetPatient1Dependants() throws Exception {
		assertEquals(1, patientDAO.getDependantPatients(1).size());
	}
}
