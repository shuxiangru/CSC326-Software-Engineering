package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.OrthopedicVisitBean;
/**
 * Loader for Orthopedic visit information. Loads information from the SQL table
 * into a bean to be used by the action class.
 * @author xshu3, yyang21
 * @date 3/20/2016
 */
public class OrthopedicVisitLoader {
	/**
	 * Takes a SQL result set and attempts to convert it to a list of 
	 * OrthopedicVisitBeans.
	 * @param rs the ResultSet from querying for orthopedic visits. 
	 * @return the appropriate list of orthopedic visit beans.
	 * @throws SQLException if the resultset is improperly formatted.
	 */
	public List<OrthopedicVisitBean> loadList(ResultSet rs) throws SQLException {
		List<OrthopedicVisitBean> list = new ArrayList<OrthopedicVisitBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;

	}
	
	/**
	 * Reads in each field from the resultset row and produces a bean from that row.
	 * @param rs
	 * @return an OrthopedicBean
	 * @throws SQLException
	 */
	public OrthopedicVisitBean loadSingle(ResultSet rs) throws SQLException {
		OrthopedicVisitBean ov = new OrthopedicVisitBean();
		ov.setOrthopedicVisitID(rs.getLong("OrthopedicVisitID"));
		ov.setOrthopedicID(rs.getLong("OrthopedicID"));
		ov.setPatientID(rs.getLong("PID"));
		ov.setOrthopedicVisitDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("OrthopedicVisitDate").getTime())));
		ov.setInjuredLimbJoint(rs.getString("InjuredPart"));
		ov.setXRay(rs.getBytes("XRay"));
		ov.setMRI(rs.getBytes("MRI"));
		ov.setMRIreport(rs.getString("MRIReport"));
		ov.setACLinjury(rs.getShort("ACLInjury"));
		ov.setMeniscusTear(rs.getShort("MeniscusTear"));
		ov.setRAhand(rs.getShort("RAhand"));
		ov.setChondromalacia(rs.getShort("Chondromalacia"));
		ov.setCPC(rs.getShort("CPC"));
		ov.setWhiplashinjury(rs.getShort("Whiplashinjury"));

		return ov;
	}
	
	public PreparedStatement loadParameters(PreparedStatement ps, OfficeVisitBean p) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
	
	

	
	
}
