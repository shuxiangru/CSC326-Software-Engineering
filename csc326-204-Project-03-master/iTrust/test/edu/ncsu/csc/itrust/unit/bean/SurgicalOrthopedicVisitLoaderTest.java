package edu.ncsu.csc.itrust.unit.bean;

import static org.junit.Assert.*;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import org.easymock.classextension.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;
import edu.ncsu.csc.itrust.beans.loaders.SurgicalOrthopedicVisitLoader;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
/**
 * 
 * @author Xiangru Shu(xshu3) Yuxu Yang(yyang21)
 *
 */
public class SurgicalOrthopedicVisitLoaderTest {
	private IMocksControl ctrl;
	private ResultSet rs;
	private SurgicalOrthopedicVisitLoader load;
	@Before
	public void setUp() throws Exception {
		ctrl = createControl();
		rs = ctrl.createMock(ResultSet.class);
		load = new SurgicalOrthopedicVisitLoader();
	}

	@Test
	public void test() throws ParseException, FileNotFoundException, SQLException, IOException, ITrustException {
		//TestDataGenerator gen = new TestDataGenerator();
		//gen.clearAllTables();
		//gen.standardData();
		try {
			expect(rs.getLong("SurgicalOrthopedicVisitID")).andReturn((long) 2).once();
			expect(rs.getLong("OrthopedicID")).andReturn((long)3).once();
			expect(rs.getLong("OrthopedicVisitID")).andReturn((long)1).once();
			expect(rs.getLong("PID")).andReturn((long)800).once();
			
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date d = sdf.parse("2015-03-13");
			expect(rs.getDate("SurgicalOrthopedicVisitDate")).andReturn(new java.sql.Date(d.getTime())).once();
			
			expect(rs.getBoolean("TotalKneeReplacement")).andReturn(false).once();
			expect(rs.getBoolean("TotalJointReplacement")).andReturn(true).once();
			expect(rs.getBoolean("ACLReconstruction")).andReturn(true).once();
			expect(rs.getBoolean("AnkleReplacement")).andReturn(true).once();
			expect(rs.getBoolean("SpineSurgery")).andReturn(true).once();
			expect(rs.getBoolean("ArthroscopicSurgery")).andReturn(true).once();
			expect(rs.getBoolean("RotatorCuffRepair")).andReturn(true).once();
			expect(rs.getBoolean("AddedVisit")).andReturn(true).once();
			expect(rs.getString("SurgeryNotes")).andReturn("bbbb").once();
			
			
			ctrl.replay();

			SurgicalOrthopedicVisitBean h = load.loadSingle(rs);

			assertEquals((long)2, h.getSurgicalOrthopedicVisitID());
			assertEquals((long)3, h.getOrthopedicID());
			assertEquals((long)800, h.getPatientID());
			assertEquals(d, h.getSurgicalOrthopedicVisitDate());
			assertEquals(false, h.getSurgery(0));
			assertEquals(true, h.getSurgery(1));
			assertEquals(true, h.getSurgery(2));
			assertEquals(true, h.getSurgery(3));
			assertEquals(true, h.getSurgery(4));
			assertEquals(true, h.getSurgery(5));
			assertEquals(true, h.getSurgery(6));
			assertEquals(true, h.getAddedVisit());
			assertEquals("bbbb", h.getSurgicalNotes());
			
		} catch (SQLException e) {
			//TODO
		}
	}
}

