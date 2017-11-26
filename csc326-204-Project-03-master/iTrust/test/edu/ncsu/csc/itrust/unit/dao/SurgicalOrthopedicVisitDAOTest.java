package edu.ncsu.csc.itrust.unit.dao;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PhysicalTherapyVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.SurgicalOrthopedicVisitDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
/**
 * 
 * @author Yuxu Yang(yyang21)
 *
 */
public class SurgicalOrthopedicVisitDAOTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private SurgicalOrthopedicVisitDAO sovDAO = factory.getSurgicalOrthopedicVisitDAO();
	private SurgicalOrthopedicVisitBean bean;
	@Before
	public void setUp() throws Exception {
		bean = new SurgicalOrthopedicVisitBean();
		bean.setOrthopedicID((long) 5);
		bean.setPatientID((long) 2);
		bean.setSurgicalOrthopedicVisitDate("03/21/2015");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws FileNotFoundException, SQLException, IOException, ITrustException {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		sovDAO.orderSurgicalOrthopedicVisit(bean);
		SurgicalOrthopedicVisitBean bean2 = sovDAO.getSurgicalOrthopedicVisit(1);
		List<SurgicalOrthopedicVisitBean> t =  sovDAO.getAllSurgicalOrthopedicVisits(2);
		assertEquals(1, t.size());
		bean2.setAddedVisit(true);
		bean2.setSurgicalNotes("aaaaasdasd");
		bean2.setSurgery(0, true);
		bean2.setSurgery(1, false);
		bean2.setSurgery(2, true);
		bean2.setSurgery(3, false);
		bean2.setSurgery(4, true);
		bean2.setSurgery(5, false);
		bean2.setSurgery(6, true);
		assertTrue(bean2.getSurgery(6));
		sovDAO.editSurgicalOrthopedicVisit(bean2);
		SurgicalOrthopedicVisitBean bean3 = sovDAO.getSurgicalOrthopedicVisit(1);
		
		assertTrue(bean3.getAddedVisit());
		assertTrue(bean3.getSurgicalNotes().equals("aaaaasdasd"));
		assertTrue(bean3.getSurgery(0));
		
		assertTrue(bean3.getSurgery(2));
		assertTrue(bean3.getSurgery(4));
		assertTrue(bean3.getSurgery(6));
			
		assertEquals(false, bean.getSurgery(1));
		assertEquals(false, bean.getSurgery(3));
		assertEquals(false, bean.getSurgery(5));
		
		
	}

}
