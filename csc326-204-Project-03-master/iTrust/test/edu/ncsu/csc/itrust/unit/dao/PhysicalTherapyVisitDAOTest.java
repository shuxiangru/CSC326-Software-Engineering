package edu.ncsu.csc.itrust.unit.dao;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PhysicalTherapyVisitDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class PhysicalTherapyVisitDAOTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private PhysicalTherapyVisitDAO ptvDAO = factory.getPhysicalTherapyVisitDAO();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws FileNotFoundException, SQLException, IOException, ITrustException, ParseException {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		PhysicalTherapyVisitBean ptvb = new PhysicalTherapyVisitBean();
		ptvb.setAddedVisit(true);
		ptvb.setBathScore((short)4);
		ptvb.setCalfTowelExercise(false);
		ptvb.setGastrocStretchExcercise(true);
		ptvb.setHeelSlideExercise(false);
		ptvb.setHipAbductionExercise(true);
		ptvb.setHouseWorkScore((short)3);
		ptvb.setJumpingScore((short)2);
		ptvb.setLiftScore((short)1);
		ptvb.setPatientID((long)300000);
		ptvb.setPhysicalTherapistID((long)2000);
		ptvb.setPhysicalTherapyVisitDate("03/21/2015");
		ptvb.setProprioceptionExercise(true);
		ptvb.setQuadSetExercise(false);
		ptvb.setRunningScore((short)2);
		ptvb.setSingleLegExercise(false);
		ptvb.setSquatScore((short)4);
		ptvb.setStairsScore((short)2);
		ptvb.setStandingScore((short)1);
		ptvb.setStraightLegExercise(false);
		ptvb.setTerminalKneeExercise(true);
		ptvb.setWalkingBlockScore((short)1);
		ptvb.setWalkingRoomScore((short)0);
		ptvb.setWallSlideExercise(true);
		ptvb.setAddedVisit(true);
		
		ptvDAO.orderPhysicalTherapyVisit(ptvb);
		
		PhysicalTherapyVisitBean test1 = ptvDAO.getPhysicalTherapyVisit(1);
		assertEquals(test1.getBathScore(), ptvb.getBathScore());
		assertEquals(test1.getBathScoreString(),"No difficulty");
		assertFalse(test1.getCalfTowelExercise());
		assertTrue(test1.getGastrocStretchExcercise());
		assertFalse(test1.getHeelSlideExercise());
		assertTrue(test1.getHipAbductionExercise());
		assertEquals(test1.getHouseWorkScore(), ptvb.getHouseWorkScore());
		assertTrue(test1.getAddedVisit());

		assertEquals(2, test1.getJumpingScore());
		assertEquals("Moderate", test1.getJumpingScoreString());
		assertEquals(1, test1.getLiftScore());
		assertEquals("Very difficult", test1.getLiftScoreString());
		assertEquals(300000, test1.getPatientID());
		assertEquals(2000, test1.getPhysicalTherapistID());
		assertEquals(test1.getPhysicalTherapyVisitDateString(), "03/21/2015");
		Date d = (Date) new SimpleDateFormat("MM/dd/yyyy").parse("03/21/2015");
		assertTrue(test1.getPhysicalTherapyVisitDate().equals(d));
		assertEquals(1, test1.getPhysicalTherapyVisitID());
		assertTrue(test1.getProprioceptionExercise());
		assertFalse(test1.getQuadSetExercise());
		assertEquals(2, test1.getRunningScore());
		assertEquals("Moderate", test1.getRunningScoreString());
		assertFalse(test1.getSingleLegExercise());
		assertEquals(4, test1.getSquatScore());
		assertEquals(test1.getSquatScoreString(),"No difficulty");
		assertEquals(2, test1.getStairsScore());
		assertEquals("Moderate", test1.getStairsScoreString());
		assertEquals(1, test1.getStandingScore());
		assertEquals("Very difficult", test1.getStandingScoreString());
		assertFalse(test1.getStraightLegExercise());
		assertTrue(test1.getTerminalKneeExercise());
		assertEquals(1, test1.getWalkingBlockScore());
		assertEquals(0, test1.getWalkingRoomScore());
		assertTrue(test1.getWallSlideExercise());
		
		List<PhysicalTherapyVisitBean> test2 = ptvDAO.getAllPhysicalTherapyVisits((long)300000);
		PhysicalTherapyVisitBean test3 = test2.get(0);
		assertEquals(test3.getBathScore(), ptvb.getBathScore());
		assertEquals(test3.getBathScoreString(),"No difficulty");
		assertFalse(test3.getCalfTowelExercise());
		assertTrue(test3.getGastrocStretchExcercise());
		assertFalse(test3.getHeelSlideExercise());
		assertTrue(test3.getHipAbductionExercise());
		assertEquals(test3.getHouseWorkScore(), ptvb.getHouseWorkScore());
		assertTrue(test3.getAddedVisit());

		assertEquals(2, test3.getJumpingScore());
		assertEquals("Moderate", test3.getJumpingScoreString());
		assertEquals(1, test3.getLiftScore());
		assertEquals("Very difficult", test3.getLiftScoreString());
		assertEquals(300000, test3.getPatientID());
		assertEquals(2000, test3.getPhysicalTherapistID());
		assertEquals(test3.getPhysicalTherapyVisitDateString(), "03/21/2015");
		Date t = (Date) new SimpleDateFormat("MM/dd/yyyy").parse("03/21/2015");
		assertTrue(test3.getPhysicalTherapyVisitDate().equals(t));
		assertTrue(test3.getProprioceptionExercise());
		assertFalse(test3.getQuadSetExercise());
		assertEquals(2, test3.getRunningScore());
		assertEquals("Moderate", test3.getRunningScoreString());
		assertFalse(test3.getSingleLegExercise());
		assertEquals(4, test3.getSquatScore());
		assertEquals(test3.getSquatScoreString(),"No difficulty");
		assertEquals(2, test3.getStairsScore());
		assertEquals("Moderate", test3.getStairsScoreString());
		assertEquals(1, test3.getStandingScore());
		assertEquals("Very difficult", test3.getStandingScoreString());
		assertFalse(test3.getStraightLegExercise());
		assertTrue(test3.getTerminalKneeExercise());
		assertEquals(1, test3.getWalkingBlockScore());
		assertEquals(0, test3.getWalkingRoomScore());
		assertTrue(test3.getWallSlideExercise());
		
		test1.setAddedVisit(false);
		test1.setBathScore((short)2);
		ptvDAO.editPhysicalTherapyVisit(test1);
		assertFalse(ptvDAO.getPhysicalTherapyVisit(1).getAddedVisit());
		assertEquals(2, ptvDAO.getPhysicalTherapyVisit(1).getBathScore());
	
		
	}

}