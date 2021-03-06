package edu.ncsu.csc.itrust.unit.dao.labprocedure;

import java.util.List;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class GetLabProceduresForPatientTest extends TestCase {
	private LabProcedureDAO lpDAO = TestDAOFactory.getTestInstance().getLabProcedureDAO();
	private TestDataGenerator gen;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.labProcedures();
	}

	public void testGetAllLabProceduresForDocOV() throws Exception {
		List<LabProcedureBean> procedures = lpDAO.getLabProceduresForPatient(1L);
		assertEquals(2, procedures.size()); // 2 because the one doesn't have rights
		assertEquals("10763-1", procedures.get(0).getLoinc());
		assertEquals("10640-1", procedures.get(1).getLoinc());
	}

	public void testFailGetLabProcedures() throws Exception {
		try {
			lpDAO.getLabProceduresForPatient(0L);
			fail("Exception should have been thrown");
		} catch (DBException e) {
			assertEquals("PatientMID cannot be null", e.getExtendedMessage());
		}
	}

}
