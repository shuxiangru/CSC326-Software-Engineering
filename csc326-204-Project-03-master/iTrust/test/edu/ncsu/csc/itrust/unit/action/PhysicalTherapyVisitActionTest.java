package edu.ncsu.csc.itrust.unit.action;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.action.PhysicalTherapyVisitAction;
import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class PhysicalTherapyVisitActionTest {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private  PhysicalTherapyVisitAction ptva;
	private PhysicalTherapyVisitBean ptvb;
	@Before
	public void setUp() throws Exception {
		ptva = new PhysicalTherapyVisitAction(factory, (long)10, "1000");
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		ptvb = new PhysicalTherapyVisitBean();
		ptvb.setAddedVisit(true);
		ptvb.setBathScore((short)4);
		ptvb.setCalfTowelExercise(false);
		ptvb.setGastrocStretchExcercise(true);
		ptvb.setHeelSlideExercise(false);
		ptvb.setHipAbductionExercise(true);
		ptvb.setHouseWorkScore((short)3);
		ptvb.setJumpingScore((short)2);
		ptvb.setLiftScore((short)1);
		ptvb.setPatientID(ptva.getPid());
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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ITrustException, ParseException {
		ptva.orderVisit(ptvb);
		ptva.orderVisit((long)250121, "12/21/2013", 0l);
		List<PhysicalTherapyVisitBean> test1 = ptva.getAllPhysicalTherapyVisits(ptva.getPid());
		assertEquals(2, test1.size());
		PhysicalTherapyVisitBean bean1 = test1.get(0);
		PhysicalTherapyVisitBean bean2 = test1.get(1);
		assertTrue(bean1.getAddedVisit());
		assertEquals(4, bean1.getBathScore());
		assertEquals(bean1.getBathScoreString(),"No difficulty");
		assertFalse(bean1.getCalfTowelExercise());
		assertTrue(bean1.getGastrocStretchExcercise());
		assertFalse(bean1.getHeelSlideExercise());
		assertTrue(bean1.getHipAbductionExercise());
		assertEquals(3, bean1.getHouseWorkScore());
		assertEquals("Little bit", bean1.getHouseWorkScoreString());
		assertEquals(2, bean1.getJumpingScore());
		assertEquals("Moderate", bean1.getJumpingScoreString());
		assertEquals(1, bean1.getLiftScore());
		assertEquals("Very difficult", bean1.getLiftScoreString());
		assertEquals(ptva.getPid(), bean1.getPatientID());
		assertEquals(2000, bean1.getPhysicalTherapistID());
		assertEquals(bean1.getPhysicalTherapyVisitDateString(), "03/21/2015");
		Date d = (Date) new SimpleDateFormat("MM/dd/yyyy").parse("03/21/2015");
		assertTrue(bean1.getPhysicalTherapyVisitDate().equals(d));
		assertTrue(bean1.getProprioceptionExercise());
		assertFalse(bean1.getQuadSetExercise());
		assertEquals(2, bean1.getRunningScore());
		assertEquals("Moderate", bean1.getRunningScoreString());
		assertFalse(bean1.getSingleLegExercise());
		assertEquals(4, bean1.getSquatScore());
		assertEquals(bean1.getSquatScoreString(),"No difficulty");
		assertEquals(2, bean1.getStairsScore());
		assertEquals("Moderate", bean1.getStairsScoreString());
		assertEquals(1, bean1.getStandingScore());
		assertEquals("Very difficult", bean1.getStandingScoreString());
		assertFalse(bean1.getStraightLegExercise());
		assertTrue(bean1.getTerminalKneeExercise());
		assertEquals(1, bean1.getWalkingBlockScore());
		assertEquals(0, bean1.getWalkingRoomScore());
		assertTrue(bean1.getWallSlideExercise());
		bean1.setGastrocStretchExcercise(false);
		ptva.editVisit(bean1);
		assertFalse(ptva.getAllPhysicalTherapyVisits(ptva.getPid()).get(0).getGastrocStretchExcercise());
		assertEquals((long)250121, bean2.getPhysicalTherapistID());
		assertFalse(bean2.getAddedVisit());
		
	}

}
