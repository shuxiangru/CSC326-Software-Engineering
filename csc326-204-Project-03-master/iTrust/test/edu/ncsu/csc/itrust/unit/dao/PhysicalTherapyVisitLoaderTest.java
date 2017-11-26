package edu.ncsu.csc.itrust.unit.dao;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.easymock.classextension.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;

import edu.ncsu.csc.itrust.beans.loaders.PhysicalTherapyVisitLoader;
/**
 * 
 * @author Xiangru Shu (xshu3)
 *
 */
public class PhysicalTherapyVisitLoaderTest {
	private IMocksControl ctrl;
	private ResultSet rs;
	private PhysicalTherapyVisitLoader load;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ctrl = createControl();
		rs = ctrl.createMock(ResultSet.class);
		load = new PhysicalTherapyVisitLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test the ReviewsBeanLoader's loadList() method
	 * @throws SQLException 
	 */
	@Test
	public void testLoadList() throws SQLException 
	{
		List<PhysicalTherapyVisitBean> l = load.loadList(rs);
		assertEquals(0, l.size());	  
	}
	
	@Test
	public void test() throws ParseException, FileNotFoundException, SQLException, IOException {
		//TestDataGenerator gen = new TestDataGenerator();
		//gen.clearAllTables();
		//gen.standardData();
		try {
			expect(rs.getLong("physicalTherapyVisitID")).andReturn((long) 2).once();
			expect(rs.getLong("OrthopedicVisitID")).andReturn((long) 1).once();
			expect(rs.getLong("PhysicalTherapistID")).andReturn((long)2000).once();
			expect(rs.getLong("PID")).andReturn((long) 1).once();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date d = sdf.parse("2015-03-13");
			expect(rs.getDate("PhysicalTherapyVisitDate")).andReturn(new java.sql.Date(d.getTime())).once();
			
			

			expect(rs.getShort("HouseWorkScore")).andReturn((short)1).once();
			expect(rs.getShort("BathScore")).andReturn((short)1).once();
			expect(rs.getShort("WalkingRoomScore")).andReturn((short)1).once();
			expect(rs.getShort("SquatScore")).andReturn((short)1).once();
			expect(rs.getShort("LiftScore")).andReturn((short)0).once();
			expect(rs.getShort("WalkingBlockScore")).andReturn((short)1).once();
			expect(rs.getShort("StairsScore")).andReturn((short)0).once();
			expect(rs.getShort("StandingScore")).andReturn((short)0).once();
			expect(rs.getShort("RunningScore")).andReturn((short)0).once();
			expect(rs.getShort("JumpingScore")).andReturn((short)0).once();
			expect(rs.getBoolean("QuadSetExercise")).andReturn(true).once();
			expect(rs.getBoolean("HeelSlideExercise")).andReturn(true).once();
			expect(rs.getBoolean("CalfTowelExercise")).andReturn(false).once();
			expect(rs.getBoolean("StraightLegExercise")).andReturn(true).once();
			expect(rs.getBoolean("TerminalKneeExercise")).andReturn(false).once();
			expect(rs.getBoolean("GastrocStretchExercise")).andReturn(true).once();
			expect(rs.getBoolean("WallSlideExercise")).andReturn(false).once();
			expect(rs.getBoolean("ProprioceptionExercise")).andReturn(false).once();
			expect(rs.getBoolean("HipAbductionExercise")).andReturn(true).once();
			expect(rs.getBoolean("SingleLegExercise")).andReturn(true).once();
			expect(rs.getBoolean("AddedVisit")).andReturn(true).once();
			

			ctrl.replay();

			PhysicalTherapyVisitBean h = load.loadSingle(rs);

			assertEquals((long)2, h.getPhysicalTherapyVisitID());
			assertEquals((long)2000, h.getPhysicalTherapistID());
			assertEquals((long)1, h.getPatientID());
			assertEquals(d, h.getPhysicalTherapyVisitDate());

			assertEquals((short)1, h.getHouseWorkScore());
			assertEquals((short)1, h.getBathScore());
			assertEquals((short)1, h.getWalkingRoomScore());
			assertEquals((short)1, h.getSquatScore());
			assertEquals((short)0, h.getLiftScore());
			assertEquals((short)1, h.getWalkingBlockScore());
			assertEquals((short)0, h.getStairsScore());
			assertEquals((short)0, h.getStandingScore());
			assertEquals((short)0, h.getRunningScore());
			assertEquals((short)0, h.getJumpingScore());
			assertTrue(h.getQuadSetExercise());
			assertTrue(h.getHeelSlideExercise());
			assertEquals(false, h.getCalfTowelExercise());
			assertTrue(h.getStraightLegExercise());
			assertEquals(false, h.getTerminalKneeExercise());
			assertTrue(h.getGastrocStretchExcercise());
			assertEquals(false, h.getWallSlideExercise());
			assertEquals(false, h.getProprioceptionExercise());
			assertTrue(h.getHipAbductionExercise());
			assertTrue(h.getSingleLegExercise());
			assertTrue(h.getAddedVisit());

			
		} catch (SQLException e) {
			//TODO
		}
	}

}


