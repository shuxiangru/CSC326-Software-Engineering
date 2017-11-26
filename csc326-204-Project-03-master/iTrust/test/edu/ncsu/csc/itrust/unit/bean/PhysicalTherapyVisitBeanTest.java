package edu.ncsu.csc.itrust.unit.bean;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;

public class PhysicalTherapyVisitBeanTest {
		private PhysicalTherapyVisitBean ptvb;
		private boolean[] a = {true, true,true, true, true,true, true, true,true, true};
		private boolean[] b = {false,false,false,false,false,false,false,false,false,false};
	@Before
	public void setUp() throws Exception {
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
		ptvb.setPatientID((long)300000);
		ptvb.setPhysicalTherapistID((long)2000);
		ptvb.setPhysicalTherapyVisitDate("03/21/2015");
		ptvb.setPhysicalTherapyVisitID((long)300);
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
	public void test() throws ParseException {
		assertTrue(ptvb.getAddedVisit());
		assertEquals(4, ptvb.getBathScore());
		assertEquals(ptvb.getBathScoreString(),"No difficulty");
		assertFalse(ptvb.getCalfTowelExercise());
		assertTrue(ptvb.getGastrocStretchExcercise());
		assertFalse(ptvb.getHeelSlideExercise());
		assertTrue(ptvb.getHipAbductionExercise());
		assertEquals(3, ptvb.getHouseWorkScore());
		assertEquals("Little bit", ptvb.getHouseWorkScoreString());
		assertEquals(2, ptvb.getJumpingScore());
		assertEquals("Moderate", ptvb.getJumpingScoreString());
		assertEquals(1, ptvb.getLiftScore());
		assertEquals("Very difficult", ptvb.getLiftScoreString());
		assertEquals(300000, ptvb.getPatientID());
		assertEquals(2000, ptvb.getPhysicalTherapistID());
		assertEquals(ptvb.getPhysicalTherapyVisitDateString(), "03/21/2015");
		Date d = (Date) new SimpleDateFormat("MM/dd/yyyy").parse("03/21/2015");
		assertTrue(ptvb.getPhysicalTherapyVisitDate().equals(d));
		assertEquals(300, ptvb.getPhysicalTherapyVisitID());
		assertTrue(ptvb.getProprioceptionExercise());
		assertFalse(ptvb.getQuadSetExercise());
		assertEquals(2, ptvb.getRunningScore());
		assertEquals("Moderate", ptvb.getRunningScoreString());
		assertFalse(ptvb.getSingleLegExercise());
		assertEquals(4, ptvb.getSquatScore());
		assertEquals(ptvb.getSquatScoreString(),"No difficulty");
		assertEquals(2, ptvb.getStairsScore());
		assertEquals("Moderate", ptvb.getStairsScoreString());
		assertEquals(1, ptvb.getStandingScore());
		assertEquals("Very difficult", ptvb.getStandingScoreString());
		assertFalse(ptvb.getStraightLegExercise());
		assertTrue(ptvb.getTerminalKneeExercise());
		assertEquals(1, ptvb.getWalkingBlockScore());
		assertEquals(0, ptvb.getWalkingRoomScore());
		assertTrue(ptvb.getWallSlideExercise());
		ptvb.setExercises(b);
		assertFalse(ptvb.getWallSlideExercise());
		assertEquals(50, ptvb.getWellnessScore(),0.1);
	}

}
