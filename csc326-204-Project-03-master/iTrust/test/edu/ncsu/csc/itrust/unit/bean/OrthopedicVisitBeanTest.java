package edu.ncsu.csc.itrust.unit.bean;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
/**
 * 
 * @author Yuxu Yang(yyang21)
 * @author Xiangru Shu(xshu3)
 *
 */
public class OrthopedicVisitBeanTest {

	
	private OrthopedicVisitBean orb;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ParseException {
		orb = new OrthopedicVisitBean();
		orb.setACLinjury((short)0);
		orb.setChondromalacia((short)1);
		orb.setCPC((short)-1);
		orb.setRAhand((short)0);
		orb.setWhiplashinjury((short)1);
		orb.setMeniscusTear((short)1);
		
		orb.setOrthopedicVisitID(1123901923);
		orb.setPatientID(12);
		
		
		orb.setInjuredLimbJoint("It's funny!!!!!!!!");
		orb.setMRIreport("We need help");
		orb.setOrthopedicVisitDate("12/02/2012");
		
		byte[] a = "Any String you want".getBytes();
		orb.setMRI(a);
		byte[] b = "Any thing you want".getBytes();	
		orb.setXRay(b);
		
		assertEquals(orb.getACLinjury(), 0);
		assertEquals(orb.getChondromalacia(), 1);
		assertEquals(orb.getCPC(), -1);
		assertEquals(orb.getRAhand(), 0);
		assertEquals(orb.getWhiplashinjury(), 1);
		assertEquals(orb.getMeniscusTear(), 1);
		
		assertEquals(orb.getOrthopedicVisitID(), 1123901923);
		assertEquals(orb.getOrthopedicVisitDateString(), "12/02/2012");
		Date d = (Date) new SimpleDateFormat("MM/dd/yyyy").parse("12/02/2012");
		assertEquals(orb.getOrthopedicVisitDate(), d);
		assertEquals(orb.getPatientID(), 12);
		assertEquals(orb.getMRIreport(), "We need help");
		assertEquals(orb.getInjuredLimbJoint(),"It's funny!!!!!!!!");
		assertTrue(orb.getMRI().equals(a));
		assertTrue(orb.getXRay().equals(b));
	}

}
