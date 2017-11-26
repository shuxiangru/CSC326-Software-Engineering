package edu.ncsu.csc.itrust.unit.dao;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createControl;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.easymock.classextension.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.FlagsBean;
import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
import edu.ncsu.csc.itrust.beans.loaders.OrthopedicVisitLoader;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
/**
 * 
 * @author Yuxu Yang(yyang21) Xiangru Shu (xshu3)
 *
 */
public class OrthopedicVisitLoaderTest {

	private IMocksControl ctrl;
	private ResultSet rs;
	private OrthopedicVisitLoader load;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ctrl = createControl();
		rs = ctrl.createMock(ResultSet.class);
		load = new OrthopedicVisitLoader();
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
		List<OrthopedicVisitBean> l = load.loadList(rs);
		assertEquals(0, l.size());	  
	}
	
	@Test
	public void test() throws ParseException, FileNotFoundException, SQLException, IOException {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		try {
			expect(rs.getLong("OrthopedicID")).andReturn((long) 1).once();
			expect(rs.getLong("OrthopedicVisitID")).andReturn((long) 2).once();
			expect(rs.getLong("PID")).andReturn((long)2000).once();
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date d = sdf.parse("2015-03-13");
			expect(rs.getDate("OrthopedicVisitDate")).andReturn(new java.sql.Date(d.getTime())).once();
			expect(rs.getString("InjuredPart")).andReturn("aaaa").once();
			byte[] a = "Any String you want".getBytes();
			byte[] b = "Any thing you want".getBytes();
			expect(rs.getString("MRIReport")).andReturn("bbbb").once();
			expect(rs.getShort("ACLInjury")).andReturn((short)1).once();
			expect(rs.getShort("MeniscusTear")).andReturn((short)1).once();
			expect(rs.getShort("RAhand")).andReturn((short)1).once();
			expect(rs.getShort("Chondromalacia")).andReturn((short)1).once();
			expect(rs.getShort("CPC")).andReturn((short)0).once();
			expect(rs.getShort("Whiplashinjury")).andReturn((short)1).once();
			expect(rs.getBytes("XRay")).andReturn(a).once();
			expect(rs.getBytes("MRI")).andReturn(b).once();
			ctrl.replay();

			OrthopedicVisitBean h = load.loadSingle(rs);

			assertEquals((long)2, h.getOrthopedicVisitID());
			assertEquals((long)1, h.getOrthopedicID());
			assertEquals((long)2000, h.getPatientID());
			assertEquals(d, h.getOrthopedicVisitDate());
			assertEquals("aaaa", h.getInjuredLimbJoint());
			assertEquals("bbbb", h.getMRIreport());
			assertEquals((short)1, h.getACLinjury());
			assertEquals((short)1, h.getMeniscusTear());
			assertEquals((short)1, h.getRAhand());
			assertEquals((short)1, h.getChondromalacia());
			assertEquals((short)0, h.getCPC());
			assertEquals((short)1, h.getWhiplashinjury());
			assertTrue(a.equals(h.getXRay()));
			assertTrue(b.equals(h.getMRI()));
			
			
		} catch (SQLException e) {
			//TODO
		}
	}

}
