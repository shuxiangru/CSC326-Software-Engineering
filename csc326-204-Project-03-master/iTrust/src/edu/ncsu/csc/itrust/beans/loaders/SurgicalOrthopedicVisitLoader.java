package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean;

public class SurgicalOrthopedicVisitLoader {
	public List<SurgicalOrthopedicVisitBean> loadList( ResultSet rs ) throws SQLException {
		List<SurgicalOrthopedicVisitBean> list = new ArrayList<SurgicalOrthopedicVisitBean>();
		while(rs.next() ) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public SurgicalOrthopedicVisitBean loadSingle(ResultSet rs) throws SQLException {
		SurgicalOrthopedicVisitBean bean = new SurgicalOrthopedicVisitBean();
		bean.setSurgicalOrthopedicVisitID(rs.getLong("SurgicalOrthopedicVisitID"));
		bean.setOrthopedicID(rs.getLong("OrthopedicID"));
		bean.setPatientID(rs.getLong("PID"));
		bean.setOrthopedicVisitID(rs.getLong("OrthopedicVisitID"));
		bean.setSurgicalOrthopedicVisitDate(
				new SimpleDateFormat("MM/dd/yyyy").format(
						new Date(rs.getDate("SurgicalOrthopedicVisitDate").getTime())));
		bean.setSurgery(0, rs.getBoolean("TotalKneeReplacement"));
		bean.setSurgery(1, rs.getBoolean("TotalJointReplacement"));
		bean.setSurgery(2, rs.getBoolean("ACLReconstruction"));
		bean.setSurgery(3, rs.getBoolean("AnkleReplacement"));
		bean.setSurgery(4, rs.getBoolean("SpineSurgery"));
		bean.setSurgery(5, rs.getBoolean("ArthroscopicSurgery"));
		bean.setSurgery(6, rs.getBoolean("RotatorCuffRepair"));
		bean.setSurgicalNotes(rs.getString("SurgeryNotes"));
		bean.setAddedVisit(rs.getBoolean("AddedVisit"));
		return bean;
	}
}
