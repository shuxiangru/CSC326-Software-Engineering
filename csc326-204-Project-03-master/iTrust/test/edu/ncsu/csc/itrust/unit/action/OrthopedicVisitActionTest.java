/**
 * 
 */
package edu.ncsu.csc.itrust.unit.action;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.action.OrthopedicVisitAction;
import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 * @author yuxuyang
 *
 */
public class OrthopedicVisitActionTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private OrthopedicVisitAction ova;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ova = new OrthopedicVisitAction(factory, (long)10, "1000");
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		OrthopedicVisitBean orb = new OrthopedicVisitBean();
		orb.setACLinjury((short)0);
		orb.setChondromalacia((short)1);
		orb.setCPC((short)-1);
		orb.setRAhand((short)0);
		orb.setWhiplashinjury((short)1);
		orb.setMeniscusTear((short)1);
		
		orb.setOrthopedicVisitID(1123901923);
		orb.setPatientID(ova.getPid());
		
		
		orb.setInjuredLimbJoint("It's funny!!!!!!!!");
		orb.setMRIreport("We need help");
		orb.setOrthopedicVisitDate("12/02/2012");
		
		byte[] a = "Any String you want".getBytes();
		orb.setMRI(a);
		byte[] b = "Any thing you want".getBytes();	
		orb.setXRay(b);
		ova.addBean(orb);
		List<OrthopedicVisitBean> bl = ova.getAllOrthopedicVisits(1000);
		assertEquals(bl.size(),1);
		OrthopedicVisitBean OVB = bl.get(0);
		assertEquals(OVB.getACLinjury(), orb.getACLinjury());
		assertEquals(OVB.getChondromalacia(), orb.getChondromalacia());
		assertEquals(OVB.getCPC(), orb.getCPC());
		assertTrue(OVB.getInjuredLimbJoint().equals(orb.getInjuredLimbJoint()));
		assertTrue(OVB.getMRIreport().equals(orb.getMRIreport()));
		assertTrue(new String (OVB.getXRay()).equals(new String(orb.getXRay())));
		assertTrue(new String (OVB.getMRI()).equals(new String(orb.getMRI())));
		assertTrue(OVB.getOrthopedicVisitDateString().equals(orb.getOrthopedicVisitDateString()));
		byte[] t = "Louis needs help".getBytes();
		orb.setMRI(t);
		ova.editBean(orb);
		List<OrthopedicVisitBean> nbl = ova.getAllOrthopedicVisits(1000);
		assertEquals(nbl.size(),1);
		OrthopedicVisitBean nOVB = nbl.get(0);
		assertTrue(new String (nOVB.getMRI()).equals(new String(t)));
		assertEquals( ova.getPid(), 1000);
		OrthopedicVisitBean nwOVB = ova.viewBean(1);
		assertEquals(nwOVB.getACLinjury(), orb.getACLinjury());
	}

}
