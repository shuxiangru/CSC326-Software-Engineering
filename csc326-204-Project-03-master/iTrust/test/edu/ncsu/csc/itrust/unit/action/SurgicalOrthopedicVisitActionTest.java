package edu.ncsu.csc.itrust.unit.action;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.action.SurgicalOrthopedicVisitAction;
import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
/**
 * 
 * @author Yuxu Yang(yyang21)
 *
 */
public class SurgicalOrthopedicVisitActionTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private  SurgicalOrthopedicVisitAction sova;
	private SurgicalOrthopedicVisitBean bean;
	@Before
	public void setUp() throws Exception {
		sova = new SurgicalOrthopedicVisitAction(factory, (long)10, "1000");
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		bean = new SurgicalOrthopedicVisitBean();
		bean.setOrthopedicID((long) 5);
		bean.setPatientID(sova.getPid());
		bean.setSurgicalOrthopedicVisitDate("03/21/2015");
		bean.setAddedVisit(true);
		bean.setSurgicalNotes("aaaaasdasd");
		bean.setSurgery(0, true);
		bean.setSurgery(1, false);
		bean.setSurgery(2, true);
		bean.setSurgery(3, false);
		bean.setSurgery(4, true);
		bean.setSurgery(5, false);
		bean.setSurgery(6, true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ITrustException {
		sova.orderVisit(bean);
		sova.editVisit(bean);
		assertEquals(sova.getPid(), (long)1000);
		List<SurgicalOrthopedicVisitBean> t =  sova.getAllSurgicalOrthopedicVisits(1000);
		assertEquals(1, t.size());
		SurgicalOrthopedicVisitBean bean2 = t.get(0);
		assertTrue(bean2.getAddedVisit());
		assertTrue(bean2.getSurgicalNotes().equals("aaaaasdasd"));
		assertTrue(bean2.getSurgery(0));
		
		assertTrue(bean2.getSurgery(2));
		assertTrue(bean2.getSurgery(4));
		assertTrue(bean2.getSurgery(6));
			
		assertEquals(false, bean2.getSurgery(1));
		assertEquals(false, bean2.getSurgery(3));
		assertEquals(false, bean2.getSurgery(5));
		bean2.setAddedVisit(false);
		sova.editVisit(bean2);
		SurgicalOrthopedicVisitBean bean3 = sova.getVisit(1);
		assertFalse(bean3.getAddedVisit());
		assertTrue(bean3.getSurgicalNotes().equals("aaaaasdasd"));
		assertTrue(bean3.getSurgery(0));
		
		assertTrue(bean3.getSurgery(2));
		assertTrue(bean3.getSurgery(4));
		assertTrue(bean3.getSurgery(6));
			
		assertEquals(false, bean3.getSurgery(1));
		assertEquals(false, bean3.getSurgery(3));
		assertEquals(false, bean3.getSurgery(5));
	}

}
