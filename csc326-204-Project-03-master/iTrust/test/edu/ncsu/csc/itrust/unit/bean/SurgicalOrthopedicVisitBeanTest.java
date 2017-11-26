package edu.ncsu.csc.itrust.unit.bean;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;
import edu.ncsu.csc.itrust.exception.ITrustException;



public class SurgicalOrthopedicVisitBeanTest {
	private SurgicalOrthopedicVisitBean bean;

@Before
public void setUp() throws Exception {
	bean = new SurgicalOrthopedicVisitBean();
	bean.setOrthopedicID((long) 5);
	bean.setPatientID((long) 2);
	bean.setSurgicalOrthopedicVisitID((long) 1);
	bean.setSurgicalOrthopedicVisitDate("03/21/2015");
	bean.setAddedVisit(true);
	bean.setSurgicalNotes("aaaa");
	bean.setSurgery(0, true);
	bean.setSurgery(1, false);
	bean.setSurgery(2, true);
	bean.setSurgery(3, false);
	bean.setSurgery(4, true);
	bean.setSurgery(5, false);
	bean.setSurgery(6, true);

}

@Test
public void test() throws ParseException, ITrustException {
	
	assertEquals((long) 5, bean.getOrthopedicID());
	assertEquals((long) 2, bean.getPatientID());
	assertEquals((long) 1, bean.getSurgicalOrthopedicVisitID());
	Date d = (Date) new SimpleDateFormat("MM/dd/yyyy").parse("03/21/2015");
	assertEquals(d, bean.getSurgicalOrthopedicVisitDate());
	assertTrue(bean.getAddedVisit());
	assertTrue(bean.getSurgicalNotes().equals("aaaa"));
	assertTrue(bean.getSurgery(0));
	
	assertTrue(bean.getSurgery(2));
	assertTrue(bean.getSurgery(4));
	assertTrue(bean.getSurgery(6));
		
	assertEquals(false, bean.getSurgery(1));
	assertEquals(false, bean.getSurgery(3));
	assertEquals(false, bean.getSurgery(5));

	
	}

}
