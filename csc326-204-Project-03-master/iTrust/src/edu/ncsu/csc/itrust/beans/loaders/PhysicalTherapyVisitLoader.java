package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean;

/**
 * Loader for Physical Therapy visit information. Loads information from the SQL table
 * into a bean to be used by the action class.
 * @author tralber2, xshu3, yyang21
 * @date 3/30/2016
 */
public class PhysicalTherapyVisitLoader {
	/**
	 * Takes a SQL result set and attempts to convert it to a list of 
	 * PhysicalTherapyVisitBeans.
	 * @param rs the ResultSet from querying for physical therapy visits. 
	 * @return the appropriate list of physical therapy visit beans.
	 * @throws SQLException if the resultset is improperly formatted.
	 */
	public List<PhysicalTherapyVisitBean> loadList(ResultSet rs) throws SQLException {
		List<PhysicalTherapyVisitBean> list = new ArrayList<PhysicalTherapyVisitBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;

	}
	/**
	 * Reads in each field from the resultset row and produces a bean from that row.
	 * @param rs
	 * @return a PhysicalTherapyBean
	 * @throws SQLException
	 */
	public PhysicalTherapyVisitBean loadSingle(ResultSet rs) throws SQLException {
		PhysicalTherapyVisitBean ptb = new PhysicalTherapyVisitBean();
		ptb.setPhysicalTherapyVisitID(rs.getLong("physicalTherapyVisitID"));
		ptb.setPhysicalTherapistID(rs.getLong("PhysicalTherapistID"));
		ptb.setPatientID(rs.getLong("PID"));
		ptb.setOrthopedicVisitID(rs.getLong("OrthopedicVisitID"));
		ptb.setPhysicalTherapyVisitDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("PhysicalTherapyVisitDate").getTime())));
		ptb.setHouseWorkScore(rs.getShort("HouseWorkScore"));
		ptb.setBathScore(rs.getShort("BathScore"));
		ptb.setWalkingRoomScore(rs.getShort("WalkingRoomScore"));
		ptb.setSquatScore(rs.getShort("SquatScore"));
		ptb.setLiftScore(rs.getShort("LiftScore"));
		ptb.setWalkingBlockScore(rs.getShort("WalkingBlockScore"));
		ptb.setStairsScore(rs.getShort("StairsScore"));
		ptb.setStandingScore(rs.getShort("StandingScore"));
		ptb.setRunningScore(rs.getShort("RunningScore"));
		ptb.setJumpingScore(rs.getShort("JumpingScore"));
		ptb.setQuadSetExercise(rs.getBoolean("QuadSetExercise"));
		ptb.setHeelSlideExercise(rs.getBoolean("HeelSlideExercise"));
		ptb.setCalfTowelExercise(rs.getBoolean("CalfTowelExercise"));
		ptb.setStraightLegExercise(rs.getBoolean("StraightLegExercise"));
		ptb.setTerminalKneeExercise(rs.getBoolean("TerminalKneeExercise"));
		ptb.setGastrocStretchExcercise(rs.getBoolean("GastrocStretchExercise"));
		ptb.setWallSlideExercise(rs.getBoolean("WallSlideExercise"));
		ptb.setProprioceptionExercise(rs.getBoolean("ProprioceptionExercise"));
		ptb.setHipAbductionExercise(rs.getBoolean("HipAbductionExercise"));
		ptb.setSingleLegExercise(rs.getBoolean("SingleLegExercise"));
		ptb.setAddedVisit(rs.getBoolean("AddedVisit"));
		return ptb;
	}
}
