package edu.ncsu.csc.itrust.unit.dao;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.OrthopedicVisitDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
/**
 * 
 * @author Yuxu Yang(yyang21) Xiangru Shu (xshu3)
 *
 */
public class OrthopedicVisitDAOTest {
	
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private OrthopedicVisitDAO hdDAO = factory.getOrthopedicVisitDAO();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ITrustException, FileNotFoundException, SQLException, IOException {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		OrthopedicVisitBean a = new OrthopedicVisitBean();
		a.setACLinjury((short)1);
		a.setChondromalacia((short)2);
		a.setCPC((short)3);
		a.setInjuredLimbJoint("what the heck");
		a.setMeniscusTear((short)0);
		byte[] ab = "Any String you want tt".getBytes();
		byte[] cd = "Any thing you want sadasdasdasdasdasdas".getBytes();
		a.setMRI(ab);
		a.setXRay(cd);
		
		a.setOrthopedicVisitID((long)2);
		a.setPatientID((long)2000);
		a.setMRIreport("aaaa");
		a.setRAhand((short)1);
		a.setWhiplashinjury((short)1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		
		a.setOrthopedicVisitDate("07/22/2015");
		hdDAO.addOrthopedicVisitRecord(a);
		
		
		
		List<OrthopedicVisitBean> l = hdDAO.getAllOrthopedicVisits(2000);
		OrthopedicVisitBean b = l.get(0);
		assertEquals((short)1, b.getACLinjury());
		assertEquals((short)2, b.getChondromalacia());
		assertEquals((short)3, b.getCPC());
		assertEquals("what the heck", b.getInjuredLimbJoint());
		assertEquals((short)0, b.getMeniscusTear());
		assertEquals((long)1, b.getOrthopedicVisitID());
		assertEquals((short)1, b.getRAhand());
		assertEquals((short)1, b.getWhiplashinjury());
		assertEquals(2000, b.getPatientID());
		assertEquals("07/22/2015",b.getOrthopedicVisitDateString());
		
		a.setInjuredLimbJoint("Yangyuxu");
		assertEquals("Yangyuxu", a.getInjuredLimbJoint());
		hdDAO.editOrthopedicVisitRecord(a);
		OrthopedicVisitBean k = hdDAO.getOrthopedicVisitRecord(a.getOrthopedicVisitID());
		assertEquals("Yangyuxu", k.getInjuredLimbJoint());
		
	}

}
